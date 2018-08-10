package com.flj.latte.ui.recycler;

import android.util.Log;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * 和bean一样
 */

public class MultipleItemEntity implements MultiItemEntity {
private static final String TAG=MultiItemEntity.class.getSimpleName();
    //数据转换层
    private final ReferenceQueue<LinkedHashMap<Object, Object>> ITEM_QUEUE = new ReferenceQueue<>();
    private final LinkedHashMap<Object, Object> MULTIPLE_FIELDS = new LinkedHashMap<>();//因为有序
    private final SoftReference<LinkedHashMap<Object, Object>> FIELDS_REFERENCE =
            new SoftReference<>(MULTIPLE_FIELDS, ITEM_QUEUE);

    MultipleItemEntity(LinkedHashMap<Object, Object> fields) {
        FIELDS_REFERENCE.get().putAll(fields);
    }

    public static MultipleEntityBuilder builder(){
        return new MultipleEntityBuilder();
    }

    //控制RecyclerView每一个的样式（不同的有不同的特征）
    @Override
    public int getItemType() {
        if(FIELDS_REFERENCE.get().get(MultipleFields.ITEM_TYPE) == null){
            Log.e(TAG,"get(MultipleFields.ITEM_TYPE) == null");
        }
     return (int) FIELDS_REFERENCE.get().get(MultipleFields.ITEM_TYPE);
    }

    @SuppressWarnings("unchecked")
    public final <T> T getField(Object key){
        return (T) FIELDS_REFERENCE.get().get(key);
    }

    public final LinkedHashMap<?,?> getFields(){
        return FIELDS_REFERENCE.get();
    }

    public final MultipleItemEntity setField(Object key,Object value){
        FIELDS_REFERENCE.get().put(key,value);
        return this;
    }
}
