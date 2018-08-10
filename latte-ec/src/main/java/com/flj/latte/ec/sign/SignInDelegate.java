package com.flj.latte.ec.sign;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.diabin.latte.ec.R;
import com.flj.latte.app.YLXconst;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.main.EcBottomDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.IFailure;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.util.CommonUtils;
import com.flj.latte.util.log.LatteLogger;
import com.flj.latte.wechat.LatteWeChat;
import com.flj.latte.wechat.callbacks.IWeChatSignInCallback;


/**
 * 登陆
 */

public class SignInDelegate extends LatteDelegate implements View.OnClickListener {
    private static final String TAG=SignInDelegate.class.getSimpleName();
    private TextInputEditText mPhone = null;
    private TextInputEditText mPassword = null;
    private ISignListener mISignListener = null;
    private String type="p";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }

    private void onClickSignIn() {
        if (checkForm()) {
            RestClient.builder()
                    .url("user/login")
                    .params("mobile", mPhone.getText().toString())
                    .params("password", mPassword.getText().toString())
                    .params("type",type)//type=p
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            Log.e(TAG,""+response);
                            //数据持久化
                            final JSONObject data= JSON.parseObject(response).getJSONObject("data");
                            final String user_name=data.getString("userName");
                            final String mobile=data.getString("mobile");
                            final String token=data.getString("token");
                            Log.e(TAG,token);
                            editor.putString(YLXconst.YLX_TOKEN,token);
                            editor.apply();
                            String b=sp.getString(YLXconst.YLX_TOKEN,"");
                            Log.e(TAG,b);
                            SignHandler.onSignIn(mISignListener);
                           // Toast.makeText(getContext(),"通过",Toast.LENGTH_LONG).show();

                        }
                    })
                    .failure(new IFailure() {
                        @Override
                        public void onFailure() {
                          if(!CommonUtils.isNetworkConnected(getContext())){
                              Toast.makeText(getContext(),"网络不可用",Toast.LENGTH_LONG).show();
                          }
                        }
                    })

                    .build()
                    .post();
        }
    }

    private void onClickWeChat() {
        LatteWeChat
                .getInstance()
                .onSignSuccess(new IWeChatSignInCallback() {
                    @Override
                    public void onSignInSuccess(String userInfo) {
                       // Toast.makeText(getContext(), userInfo, Toast.LENGTH_LONG).show();
                    }
                })
                .signIn();
    }

    private void onClickLink() {
        getSupportDelegate().start(new SignUpDelegate());
    }

    private boolean checkForm() {
        final String phone = mPhone.getText().toString();
        final String password = mPassword.getText().toString();

        boolean isPass = true;

        if (phone.isEmpty() || phone.length() != 11) {
            mPhone.setError("手机号码错误");
            isPass = false;
        } else {
            mPhone.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError("请填写至少6位数密码");
            isPass = false;
        } else {
            mPassword.setError(null);
        }

        return isPass;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_in;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mPhone = $(R.id.edit_sign_in_phone);
        mPassword = $(R.id.edit_sign_in_password);
        $(R.id.btn_sign_in).setOnClickListener(this);
        $(R.id.tv_link_sign_up).setOnClickListener(this);
        $(R.id.icon_sign_in_wechat).setOnClickListener(this);

        sp=getContext().getSharedPreferences("config",Context.MODE_PRIVATE);
        editor=sp.edit();
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_sign_in) {
            onClickSignIn();
        } else if (i == R.id.tv_link_sign_up) {
            onClickLink();
        } else if (i == R.id.icon_sign_in_wechat) {
            onClickWeChat();
        }
    }
}
