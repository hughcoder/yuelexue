package com.flj.latte.ec.main.audition.OverdueDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.diabin.latte.ec.R;
import com.flj.latte.app.YLXconst;
import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;

/**
 * Created by home on 2018/1/31.
 */

public class OverdueDelegate extends BottomItemDelegate {
    private static final String TAG=OverdueDelegate.class.getSimpleName();
    private static final String appoint_status_past="past";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @Override
    public Object setLayout() {
        return R.layout.delegate_overdue;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        sp=getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        String token=sp.getString(YLXconst.YLX_TOKEN,"失败");
        RestClient.builder()
                .loader(getContext())
                .url("/api/appointment/list")
                .params("token",token)
                .params("past",appoint_status_past)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        if(response!=null){
                            Log.e(TAG,response);
                        }
                    }
                })
                .build()
                .get();
    }
}
