package com.flj.latte.ec.main.index;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.course_detail.CourseDetailDelegate;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;

/**
 * Created by home on 2018/2/2.
 */

public class IndexHotItemClickListener extends SimpleClickListener {
    private final LatteDelegate DELEGATE;
    private IndexHotItemClickListener(LatteDelegate delegate){this.DELEGATE=delegate;}
    public static SimpleClickListener create(LatteDelegate delegate){
        return new IndexHotItemClickListener(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            final MultipleItemEntity entity= (MultipleItemEntity) baseQuickAdapter.getData().get(position);
            final int course_no=entity.getField(MultipleFields.ID);
          final CourseDetailDelegate delegate= CourseDetailDelegate.create(course_no);
           DELEGATE.start(delegate);
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
