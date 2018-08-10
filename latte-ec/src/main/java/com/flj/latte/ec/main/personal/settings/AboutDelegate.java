package com.flj.latte.ec.main.personal.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.diabin.latte.ec.R;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;

/**
 * Created by 傅令杰
 */

public class AboutDelegate extends LatteDelegate {

    @Override
    public Object setLayout() {
        return R.layout.delegate_about;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final AppCompatTextView textView = $(R.id.tv_course_detail_info);

        RestClient.builder()
                .url("about.php")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final String info = JSON.parseObject(response).getString("data");
                        textView.setText(info);
                    }
                })
                .build()
                .get();
    }
}
