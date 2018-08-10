package com.flj.latte.ec.main.index.search;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.StringUtils;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.diabin.latte.ec.R;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.util.storage.LattePreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 傅令杰
 */

public class SearchDelegate extends LatteDelegate {

    private AppCompatEditText mSearchEdit = null;

    void onClickSearch() {

        RestClient.builder()
                .url("search.php?key=")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final String searchItemText = mSearchEdit.getText().toString();
                        saveItem(searchItemText);
                        mSearchEdit.setText("");
                        //展示一些东西
                        //弹出一段话
                    }
                })
                .build()
                .get();
    }

    @SuppressWarnings("unchecked")
    private void saveItem(String item) {
        if (!StringUtils.isEmpty(item) && !StringUtils.isSpace(item)) {
            List<String> history;
            final String historyStr =
                    LattePreference.getCustomAppProfile(SearchDataConverter.TAG_SEARCH_HISTORY);
            if (StringUtils.isEmpty(historyStr)) {
                history = new ArrayList<>();
            } else {
                history = JSON.parseObject(historyStr, ArrayList.class);
            }
            history.add(item);
            final String json = JSON.toJSONString(history);

            LattePreference.addCustomAppProfile(SearchDataConverter.TAG_SEARCH_HISTORY, json);
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final RecyclerView recyclerView = $(R.id.rv_search);
        mSearchEdit = $(R.id.et_search_view);

        $(R.id.tv_top_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSearch();
            }
        });

        $(R.id.icon_top_search_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportDelegate().pop();
            }
        });

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        final List<MultipleItemEntity> data = new SearchDataConverter().convert();
        final SearchAdapter adapter = new SearchAdapter(data);
        recyclerView.setAdapter(adapter);

        final DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {
            @Override
            public Divider getVerticalDivider(int position) {
                return null;
            }

            @Override
            public Divider getHorizontalDivider(int position) {
                return new Divider.Builder()
                        .size(2)
                        .margin(20, 20)
                        .color(Color.GRAY)
                        .build();
            }
        });

        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_search;
    }


}
