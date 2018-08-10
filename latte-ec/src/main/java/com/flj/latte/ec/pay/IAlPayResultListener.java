package com.flj.latte.ec.pay;

/**
 * Created by 傅令杰
 */

public interface IAlPayResultListener {

    void onPaySuccess();

    void onPaying();

    void onPayFail();

    void onPayCancel();

    void onPayConnectError();
}
