package com.flj.latte.ec.sign;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flj.latte.app.AccountManager;
import com.flj.latte.ec.main.EcBottomDelegate;

/**
 *
 */

public class SignHandler {

    private static final String TAG=SignHandler.class.getSimpleName();

    public static void onSignIn(ISignListener signListener) {
        //已经注册并登录成功了
        AccountManager.setSignState(true);
        signListener.onSignInSuccess();
        EcBottomDelegate ecBottomDelegate=new EcBottomDelegate();


    }


    public static void onSignUp(String response, ISignListener signListener) {

        final JSONObject profileJson = JSON.parseObject(response).getJSONObject("state");
        final String state=profileJson.toString();
        Log.e(TAG,state);

        //已经注册并登录成功了
        AccountManager.setSignState(true);
        signListener.onSignUpSuccess();
    }
}
