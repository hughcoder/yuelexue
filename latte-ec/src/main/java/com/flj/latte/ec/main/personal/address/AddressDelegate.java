package com.flj.latte.ec.main.personal.address;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.diabin.latte.ec.R;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.util.log.LatteLogger;

import java.util.List;

/**
 * Created by 傅令杰
 */

public class AddressDelegate extends LatteDelegate implements ISuccess {

    private RecyclerView mRecyclerView = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_address;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_address);

      RestClient.builder()
                .url("address.php")
                .loader(getContext())
                .success(this)
                .build()
                .get();

    }

    @Override
    public void onSuccess(String response) {
        LatteLogger.d("AddressDelegate", response);

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        final List<MultipleItemEntity> data =
                new AddressDataConverter().setJsonData(response).convert();
        final AddressAdapter addressAdapter = new AddressAdapter(data);
        mRecyclerView.setAdapter(addressAdapter);



    }
}
