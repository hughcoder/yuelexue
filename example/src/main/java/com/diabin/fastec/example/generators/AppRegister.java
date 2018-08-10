package com.diabin.fastec.example.generators;

import com.flj.latte.annotations.AppRegisterGenerator;
import com.flj.latte.wechat.templates.AppRegisterTemplate;

/**
 * Created by 傅令杰 on 2017/4/22
 */
@SuppressWarnings("unused")
@AppRegisterGenerator(
        packageName = "com.diabin.fastec.example",
        registerTemplate = AppRegisterTemplate.class
)
public interface AppRegister {
}
