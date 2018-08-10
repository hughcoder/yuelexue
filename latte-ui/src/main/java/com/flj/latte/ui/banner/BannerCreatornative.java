package com.flj.latte.ui.banner;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.flj.latte.ui.R;

import java.util.ArrayList;

/**
 * Created by home on 2018/1/3.
 */

public class BannerCreatornative {
    public static void setDefaultnative(ConvenientBanner<Integer> convenientBanner,
                                  ArrayList<Integer> banners,
                                  OnItemClickListener clickListener) {

        convenientBanner
                .setPages(new HolderCreatornative(), banners)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(clickListener)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);
    }
}
