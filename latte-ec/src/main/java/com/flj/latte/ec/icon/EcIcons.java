package com.flj.latte.ec.icon;

import com.joanzapata.iconify.Icon;

/**
 * Created by 傅令杰 on 2017/3/29
 */

public enum EcIcons implements Icon {
    icon_scan('\ue602'),
    icon_audio('\ue603'),
    icon_ali_pay('\ue606');


    private char character;

    EcIcons(char character) {
        this.character = character;
    }

    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return character;
    }
}
