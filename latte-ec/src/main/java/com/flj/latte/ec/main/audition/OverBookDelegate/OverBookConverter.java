package com.flj.latte.ec.main.audition.OverBookDelegate;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.flj.latte.ec.main.audition.AuditionItemFields;
import com.flj.latte.ec.main.audition.AuditionItemType;
import com.flj.latte.ui.recycler.DataConverter;
import com.flj.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by home on 2018/3/8.
 */

public class OverBookConverter extends DataConverter {
    private static final String TAG=OverBookConverter.class.getSimpleName();
    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<MultipleItemEntity> convert() {
        final JSONObject data_2=JSON.parseObject(getJsonData()).getJSONObject("data");
        for(int i=0;i<2;i++){
            final JSONObject data=data_2.getJSONObject(""+i);
            final JSONObject course=data.getJSONObject("course");
            final String address=course.getString("address");
            final String appoint_time=data.getString("appoint_time");
            final String course_name=course.getString("course_name");
            final String course_iv_url=course.getString("image_url_small");
            final JSONArray array=data.getJSONArray("audition");
            LinkedHashMap<Integer,String> autions=new LinkedHashMap();
            for(int j=0;j<array.size();j++){
                final String audition=array.getString(j);
                Log.e(TAG,audition);
                autions.put(j,audition);
            }
            Log.e(TAG,""+autions.size());


            final MultipleItemEntity entity=MultipleItemEntity.builder()
                    .setItemType(AuditionItemType.OverBook_Item)
                    .setField(AuditionItemFields.AUDITION_ADDRESS,address)
                    .setField(AuditionItemFields.AUDITION_APPOINTTIME,appoint_time)
                    .setField(AuditionItemFields.AUDITION_COURSE_NAME,course_name)
                    .setField(AuditionItemFields.AUDITION_IMAGEURL,course_iv_url)
                    .setField(AuditionItemFields.AUDITION_TIMES,autions)
                    .build();
            ENTITIES.add(entity);

        }
        return ENTITIES;
    }
}
