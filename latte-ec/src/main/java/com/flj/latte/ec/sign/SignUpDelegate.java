package com.flj.latte.ec.sign;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.diabin.latte.ec.R;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.util.AMUtils;
import com.flj.latte.util.downtimer.DownTimer;
import com.flj.latte.util.downtimer.DownTimerListener;
import com.flj.latte.util.log.LatteLogger;

/**
 * 注册
 */

public class SignUpDelegate extends LatteDelegate implements DownTimerListener {
    private static final String TAG=SignUpDelegate.class.getSimpleName();
    private String type="P";

    private TextInputEditText mName = null;

    private TextInputEditText mPhone = null;
    private TextInputEditText mPassword = null;
    private TextInputEditText mRePassword = null;
    private EditText code_value=null;

    private ISignListener mISignListener = null;
    private ImageView mImgBackground;

    private AppCompatButton mGetCode=null;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }

    private void onClickSignUp() {
        if (checkForm()) {
            RestClient.builder()
                    .url("/api/user/register")
                    .params("mobile", mPhone.getText().toString())
                    .params("code",code_value.getText().toString())
                    .params("type",type)
                    .params("password", mPassword.getText().toString())
                    .params("repassword",mRePassword.getText().toString())
                    .params("user_name", mName.getText().toString())
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            Log.e(TAG,""+response);

                           // SignHandler.onSignUp(response, mISignListener);
                          //  Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                        }
                    })
                    .build()
                    .post();
        }
    }
    //验证码请求网络
    private void getPhonecode(){
        //倒计时
        DownTimer downTimer = new DownTimer();
        downTimer.setListener(this);
        downTimer.startDown(60 * 1000);
        RestClient.builder()
                .url("/api/sms/send/code")
                .params("mobile",mPhone.getText().toString())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e(TAG,""+response);
                     //   Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
                    }
                })
                .build()
                .post();

    }

    private void onClickLink() {
        getSupportDelegate().start(new SignInDelegate());
    }

    private boolean checkForm() {
        final String name = mName.getText().toString();

        final String phone = mPhone.getText().toString();
        final String password = mPassword.getText().toString();
        final String rePassword = mRePassword.getText().toString();

        boolean isPass = true;


        if (name.isEmpty()) {
            mName.setError("请输入姓名");
            isPass = false;
        } else {
            mName.setError(null);
        }

        if (phone.isEmpty() || phone.length() != 11|| !AMUtils.isMobile(phone.toString().trim())) {
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

        if (rePassword.isEmpty() || rePassword.length() < 6 || !(rePassword.equals(password))) {
            mRePassword.setError("密码验证错误");
            isPass = false;
        } else {
            mRePassword.setError(null);
        }

        return isPass;
    }
    private void addEditTextListener(){
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==11&&AMUtils.isMobile(s.toString().trim())&&isBright){
                    mGetCode.setClickable(true);
                    mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_up;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mName = $(R.id.edit_sign_up_name);
        mPhone = $(R.id.edit_sign_up_phone);
        mPassword = $(R.id.edit_sign_up_password);
        mRePassword = $(R.id.edit_sign_up_re_password);
        code_value=$(R.id.reg_code);

        mGetCode=$(R.id.reg_getcode);
        mGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhonecode();
            }
        });
        mGetCode.setClickable(false);

        $(R.id.btn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignUp();
            }
        });

        $(R.id.tv_link_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLink();
            }
        });
       //获取背景，延迟0.2S启动动画
        mImgBackground = $(R.id.rg_img_backgroud);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation( getContext(),R.anim.translate_anim);
                mImgBackground.startAnimation(animation);
            }
        }, 200);
         addEditTextListener();
    }

    boolean isBright = true;
    @Override
    public void onTick(long millisUntilFinished) {
        mGetCode.setText(String.valueOf(millisUntilFinished / 1000) + "s");
        mGetCode.setClickable(false);
        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
        isBright = false;
    }

    @Override
    public void onFinish() {
        mGetCode.setText(R.string.get_code);
        mGetCode.setClickable(true);
        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
        isBright = true;
    }
}
