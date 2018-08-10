package com.diabin.fastec.example;

import android.app.Application;
import android.support.annotation.Nullable;

import com.diabin.fastec.example.event.ShareEvent;
import com.diabin.fastec.example.event.TestEvent;
import com.flj.latte.app.Latte;
import com.flj.latte.ec.icon.FontEcModule;
import com.flj.latte.net.interceptors.DebugInterceptor;
import com.flj.latte.net.rx.AddCookieInterceptor;
import com.flj.latte.util.callback.CallbackManager;
import com.flj.latte.util.callback.CallbackType;
import com.flj.latte.util.callback.IGlobalCallback;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 傅令杰 on 2017/3/29
 */
public class ExampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Latte.init(this)
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEcModule())
                .withLoaderDelayed(1000)
           //    .withApiHost("http://www.xiufm.com/RestServer/api/")
                 .withApiHost("http://101.132.73.58/api/")
                .withInterceptor(new DebugInterceptor("test", R.raw.test))
                .withWeChatAppId("你的微信AppKey")
                .withWeChatAppSecret("你的微信AppSecret")
                .withJavascriptInterface("latte")
                .withWebEvent("test", new TestEvent())
                .withWebEvent("share", new ShareEvent())
                //添加Cookie同步拦截器
                .withWebHost("https://www.baidu.com/")
                .withInterceptor(new AddCookieInterceptor())
                .configure();

        //开启极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        CallbackManager.getInstance()
                .addCallback(CallbackType.TAG_OPEN_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        if (JPushInterface.isPushStopped(Latte.getApplicationContext())) {
                            //开启极光推送
                            JPushInterface.setDebugMode(true);
                            JPushInterface.init(Latte.getApplicationContext());
                        }
                    }
                })
                .addCallback(CallbackType.TAG_STOP_PUSH, new IGlobalCallback() {
                    @Override
                    public void executeCallback(@Nullable Object args) {
                        if (!JPushInterface.isPushStopped(Latte.getApplicationContext())) {
                            JPushInterface.stopPush(Latte.getApplicationContext());
                        }
                    }
                });
    }

}
