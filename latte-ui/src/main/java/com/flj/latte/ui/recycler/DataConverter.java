package com.flj.latte.ui.recycler;

import java.util.ArrayList;

/**
 *  数据转换的约束
 */

public abstract class DataConverter {//还需要一个存储数据对象，就是entity

    protected final ArrayList<MultipleItemEntity> ENTITIES = new ArrayList<>();
    private String mJsonData = null;

    public abstract ArrayList<MultipleItemEntity> convert();

    public DataConverter setJsonData(String json) {
        this.mJsonData = json;
        return this;
    }

    protected String getJsonData() {
        if (mJsonData == null || mJsonData.isEmpty()) {
            throw new NullPointerException("DATA IS NULL!");
        }
        return mJsonData;
    }

    public void clearData(){
        ENTITIES.clear();
    }
}
