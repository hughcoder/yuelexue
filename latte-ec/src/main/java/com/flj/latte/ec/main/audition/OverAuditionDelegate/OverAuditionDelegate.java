package com.flj.latte.ec.main.audition.OverAuditionDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.diabin.latte.ec.R;
import com.flj.latte.app.YLXconst;
import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;

/**
 * Created by home on 2018/1/31.
 */

public class OverAuditionDelegate extends BottomItemDelegate {
    private static final String TAG=OverAuditionDelegate.class.getSimpleName();

    private static final String appoint_status_audition="audition";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public Object setLayout() {
        return R.layout.delegate_overaudition;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }



    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        sp=getContext().getSharedPreferences("config",Context.MODE_PRIVATE);
        String token=sp.getString(YLXconst.YLX_TOKEN,"失败");
        Log.e(TAG,token);
        RestClient.builder()
               .url("/api/appointment/list")
               .loader(getContext())
               .params("appoint_status",appoint_status_audition)
               .params("token",token)
               .success(new ISuccess() {
                   @Override
                   public void onSuccess(String response) {
                       Log.e(TAG,response);

                   }
               })
               .build()
               .get();

    }
}
