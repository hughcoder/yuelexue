package com.flj.latte.ui.banner;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;

/**
 * Created by home on 2018/1/3.
 */

public class HolderCreatornative implements CBViewHolderCreator<Holder> {
    @Override
    public ImageHoldernative createHolder() {
        return new ImageHoldernative();
    }
}
