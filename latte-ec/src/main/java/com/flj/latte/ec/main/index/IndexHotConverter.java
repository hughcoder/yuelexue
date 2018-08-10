package com.flj.latte.ec.main.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flj.latte.ec.main.index.IndexItemFields;
import com.flj.latte.ec.main.index.IndexItemType;
import com.flj.latte.ui.recycler.DataConverter;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;

import java.util.ArrayList;

/**
 * Created by home on 2018/1/3.
 */

public class IndexHotConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("data");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data=array.getJSONObject(i);
            final int id = data.getInteger("course_no");
            final String imageUrl=data.getString("image_url_small");
            final String visit_num=data.getString("visit_num");
            final String appointment_num=data.getString("appointment_num");
            final String name=data.getString("course_name");


            final MultipleItemEntity entity= MultipleItemEntity.builder()
                    .setItemType(IndexItemType.ITEM_INDEX)
                     .setField(MultipleFields.ID, id)
                    .setField(MultipleFields.NAME, name)
                    .setField(MultipleFields.IMAGE_URL,imageUrl)
                    .setField(IndexItemFields.VISIT_NUM,visit_num)
                    .setField(IndexItemFields.APPOINTMENT_NUM,appointment_num)
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
