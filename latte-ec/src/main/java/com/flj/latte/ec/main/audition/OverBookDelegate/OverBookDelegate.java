package com.flj.latte.ec.main.audition.OverBookDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.diabin.latte.ec.R;
import com.flj.latte.app.YLXconst;
import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.BaseDecoration;
import com.flj.latte.ui.recycler.MultipleItemEntity;

import java.util.List;

/**
 * Created by home on 2018/1/31.
 */

public class OverBookDelegate extends BottomItemDelegate {
    private final String TAG=OverBookDelegate.class.getSimpleName();
         private SharedPreferences sp;
         private SharedPreferences.Editor editor;
         private RecyclerView rv_overbook;
         private AppCompatTextView tv_overbook_mask;

    @Override
    public Object setLayout() {
        return R.layout.delegate_overbook;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        rv_overbook=$(R.id.rv_overbook);
        tv_overbook_mask=$(R.id.tv_overbook_mask);

        sp=getContext().getSharedPreferences("config",Context.MODE_PRIVATE);
        String token=sp.getString(YLXconst.YLX_TOKEN,"");
        Log.e(TAG,token);
        String appoint="appoint";
        RestClient.builder()
                .loader(getContext())
                .url("/api/appointment/list")
                .params("appoint_status",appoint)
                .params("token",token)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                         if(response!=null){
                             tv_overbook_mask.setVisibility(View.GONE);
                             Log.e(TAG,""+response);
                             LinearLayoutManager manager=new LinearLayoutManager(getContext());
                             rv_overbook.addItemDecoration(
                                     BaseDecoration.create(ContextCompat.getColor(getContext(),R.color.app_background),8));
                             rv_overbook.setLayoutManager(manager);
                             final List<MultipleItemEntity> data=
                                     new OverBookConverter().setJsonData(response).convert();
                             final OverBookadapter overBookadapter=new OverBookadapter(data);
                              rv_overbook.setAdapter(overBookadapter);

                         }
                    }
                })
                .build()
                .get();

    }

}
