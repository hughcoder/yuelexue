package com.flj.latte.ui.banner;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.flj.latte.ui.R;

import java.util.ArrayList;

/**
 * 自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
 */

public class BannerCreator {

    public static void setDefault(ConvenientBanner<String> convenientBanner,
                                  ArrayList<String> banners,
                                  OnItemClickListener clickListener) {

        convenientBanner
                .setPages(new HolderCreator(), banners)//！！有问题，源码是泛型
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(clickListener)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);

    }
}
