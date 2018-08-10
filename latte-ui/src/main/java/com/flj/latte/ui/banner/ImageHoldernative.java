package com.flj.latte.ui.banner;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;

/**
 * Created by home on 2018/1/3.
 */

public class ImageHoldernative implements Holder<Integer> {
    private AppCompatImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new AppCompatImageView(context);
        imageView.setScaleType(AppCompatImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        imageView.setImageResource(data);
    }
}
