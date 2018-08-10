package com.flj.latte.ec.main.index;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.diabin.latte.ec.R;
import com.flj.latte.ec.main.index.IndexItemFields;
import com.flj.latte.ec.main.index.IndexItemType;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.ui.recycler.MultipleRecyclerAdapter;
import com.flj.latte.ui.recycler.MultipleViewHolder;

import java.util.List;

/**
 * Created by home on 2018/1/3.
 */

public class IndexHotadapter extends MultipleRecyclerAdapter{
    private static final String TAG="ImageUrl";
    //设置图片加载策略
    private static final RequestOptions RECYCLER_OPTIONS =
            new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();

    protected IndexHotadapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(IndexItemType.ITEM_INDEX, R.layout.item_hot_course);
    }

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()){
            case IndexItemType.ITEM_INDEX:
                //取数据
                final String name=entity.getField(MultipleFields.NAME);
                final String imageUrl=entity.getField(MultipleFields.IMAGE_URL);
               final String visit_num=entity.getField(IndexItemFields.VISIT_NUM);
                final String appointment_num=entity.getField(IndexItemFields.APPOINTMENT_NUM);
               final int id=entity.getField(MultipleFields.ID);
               //取控件
                final AppCompatImageView hotView=holder.getView(R.id.image_hot_course);
               final AppCompatTextView visitText=holder.getView(R.id.tv_hot_visit_num);
                final AppCompatTextView appointmentText=holder.getView(R.id.hot_course_appointment);
                final AppCompatTextView nameText=holder.getView(R.id.tv_hot_title);

                nameText.setText(name);
                visitText.setText(visit_num);
                appointmentText.setText(appointment_num);
                Log.e(TAG,""+imageUrl);
                Glide.with(mContext)
                       .load(imageUrl)
                        .apply(RECYCLER_OPTIONS)
                       .into(hotView);
                break;
                default:
                    break;
        }
    }
}
