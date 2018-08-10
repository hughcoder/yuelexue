package com.diabin.fastec.example.event;

import android.webkit.WebView;
import android.widget.Toast;

import com.flj.latte.delegates.web.event.Event;

/**
 * Created by 傅令杰
 */

public class TestEvent extends Event {
    @Override
    public String execute(String params) {
        Toast.makeText(getContext(), getAction(), Toast.LENGTH_LONG).show();
        if (getAction().equals("test")) {
            final WebView webView = getWebView();
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.evaluateJavascript("nativeCall();", null);
                }
            });
        }
        return null;
    }
}
