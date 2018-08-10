package com.flj.latte.ec.main.audition.OverAuditionDelegate;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.diabin.latte.ec.R;
import com.flj.latte.ec.main.audition.AuditionItemFields;
import com.flj.latte.ec.main.audition.AuditionItemType;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.ui.recycler.MultipleRecyclerAdapter;
import com.flj.latte.ui.recycler.MultipleViewHolder;
import com.flj.latte.util.DialogSelectItemUtil;
import com.flj.latte.util.callback.SortPopCallBack;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by home on 2018/2/6.
 */

public class OverAuditionadapter extends MultipleRecyclerAdapter {
    private DialogSelectItemUtil mdialog;

    private static final String TAG= com.flj.latte.ec.main.audition.OverBookDelegate.OverBookadapter.class.getSimpleName();
    private static String change_time=null;

    //设置图片加载策略
    private static final RequestOptions RECYCLER_OPTIONS =
            new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();

    protected OverAuditionadapter(List<MultipleItemEntity> data) {
        super(data);
        addItemType(AuditionItemType.OverBook_Item, R.layout.item_overbook);

    }

    private void showdialog(LinkedHashMap<Integer,String> linkedHashMap){
        if(mdialog==null){
            List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
            Log.e(TAG,""+linkedHashMap.size());
            for(int i=0;i<linkedHashMap.size();i++){

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("title", linkedHashMap.get(i));
                map.put("value", ""+(i+1));
                data.add(map);
            }
            mdialog=new DialogSelectItemUtil(mContext,zixunItemClick,data);
        }
        mdialog.showDialog();
    }
    private SortPopCallBack zixunItemClick = new SortPopCallBack() {
        @Override
        public void itemClick(int position, Map<String, Object> map) {
            // TODO Auto-generated method stub
            mdialog.dismissDialog();
            if(map!=null) {
                Toast.makeText(mContext,(String)map.get("title"),Toast.LENGTH_SHORT).show();
                change_time=(String)map.get("title");
            }
        }
    };

    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()){
            case AuditionItemType.OverBook_Item:
                //取数据
                final String address=entity.getField(AuditionItemFields.AUDITION_ADDRESS);
                final String appointtime=entity.getField(AuditionItemFields.AUDITION_APPOINTTIME);
                final String course_name=entity.getField(AuditionItemFields.AUDITION_COURSE_NAME);
                final String image_url=entity.getField(AuditionItemFields.AUDITION_IMAGEURL);

                //取控件
                final AppCompatImageView imageView=holder.getView(R.id.iv_overbook);
                final AppCompatTextView tv_course_name=holder.getView(R.id.tv_course_name);
                final AppCompatTextView tv_appointtime=holder.getView(R.id.tv_overbook_time);
                final AppCompatTextView tv_tv_overbook_address=holder.getView(R.id.tv_overbook_address);
                final AppCompatTextView tv_leave_time=holder.getView(R.id.leave_time);

                //预约时间分解

                final AppCompatButton btn_change_overbook_time=holder.getView(R.id.btn_change_overbook_time);






                tv_course_name.setText(course_name);
                tv_appointtime.setText(appointtime);
                tv_tv_overbook_address.setText(address);
                Glide.with(mContext)
                        .load(image_url)
                        .apply(RECYCLER_OPTIONS)
                        .into(imageView);
                break;
            default:
                break;
        }
    }
}