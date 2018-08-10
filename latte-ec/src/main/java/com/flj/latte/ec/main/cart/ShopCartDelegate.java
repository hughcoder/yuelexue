package com.flj.latte.ec.main.cart;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewStubCompat;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.diabin.latte.ec.R;
import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.ec.main.EcBottomDelegate;
import com.flj.latte.ec.pay.FastPay;
import com.flj.latte.ec.pay.IAlPayResultListener;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.util.log.LatteLogger;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by 傅令杰
 */

public class ShopCartDelegate extends BottomItemDelegate implements View.OnClickListener, ISuccess, ICartItemListener, IAlPayResultListener {

    private ShopCartAdapter mAdapter = null;
    //购物车数量标记
    private int mCurrentCount = 0;
    private int mTotalCount = 0;
    private double mTotalPrice = 0.00;

    private RecyclerView mRecyclerView = null;
    private IconTextView mIconSelectAll = null;
    private ViewStubCompat mStubNoItem = null;
    private AppCompatTextView mTvTotalPrice = null;

    void onClickSelectAll() {
        final int tag = (int) mIconSelectAll.getTag();
        if (tag == 0) {
            final Context context = getContext();
            if (context != null) {
                mIconSelectAll.setTextColor
                        (ContextCompat.getColor(context, R.color.app_main));
            }
            mIconSelectAll.setTag(1);
            mAdapter.setIsSelectedAll(true);
            //更新RecyclerView的显示状态
            mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
        } else {
            mIconSelectAll.setTextColor(Color.GRAY);
            mIconSelectAll.setTag(0);
            mAdapter.setIsSelectedAll(false);
            mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
        }
    }

    void onClickRemoveSelectedItem() {
        final List<MultipleItemEntity> data = mAdapter.getData();
        //要删除的数据
        final List<MultipleItemEntity> deleteEntities = new ArrayList<>();
        for (MultipleItemEntity entity : data) {
            final boolean isSelected = entity.getField(ShopCartItemFields.IS_SELECTED);
            if (isSelected) {
                deleteEntities.add(entity);
            }
        }
        for (MultipleItemEntity entity : deleteEntities) {
            int removePosition;
            final int entityPosition = entity.getField(ShopCartItemFields.POSITION);
            if (entityPosition > mCurrentCount - 1) {
                removePosition = entityPosition - (mTotalCount - mCurrentCount);
            } else {
                removePosition = entityPosition;
            }
            if (removePosition <= mAdapter.getItemCount()) {
                mAdapter.remove(removePosition);
                mCurrentCount = mAdapter.getItemCount();
                //更新数据
                mAdapter.notifyItemRangeChanged(removePosition, mAdapter.getItemCount());
            }
        }
        checkItemCount();
    }

    void onClickClear() {
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        checkItemCount();
    }

    //创建订单，注意，和支付是没有关系的
    private void createOrder() {
        final String orderUrl = "你的生成订单的API";
        final WeakHashMap<String, Object> orderParams = new WeakHashMap<>();
        //加入你的参数
        RestClient.builder()
                .url(orderUrl)
                .loader(getContext())
                .params(orderParams)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //进行具体的支付
                        LatteLogger.d("ORDER", response);
                        final int orderId = JSON.parseObject(response).getInteger("result");
                        FastPay.create(ShopCartDelegate.this)
                                .setPayResultListener(ShopCartDelegate.this)
                                .setOrderId(orderId)
                                .beginPayDialog();
                    }
                })
                .build()
                .post();

    }

    @SuppressWarnings("RestrictedApi")
    private void checkItemCount() {
        final int count = mAdapter.getItemCount();
        if (count == 0) {
            final View stubView = mStubNoItem.inflate();
            final AppCompatTextView tvToBuy =
                    stubView.findViewById(R.id.tv_stub_to_buy);
            tvToBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "你该购物啦！", Toast.LENGTH_SHORT).show();
                    //切换到首页
                    final int indexTab = 0;
                    final EcBottomDelegate ecBottomDelegate = getParentDelegate();
                    final BottomItemDelegate indexDelegate = ecBottomDelegate.getItemDelegates().get(indexTab);
                    ecBottomDelegate
                            .getSupportDelegate()
                            .showHideFragment(indexDelegate, ShopCartDelegate.this);
                    ecBottomDelegate.changeColor(indexTab);
                }
            });
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_shop_cart;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_shop_cart);
        mIconSelectAll = $(R.id.icon_shop_cart_select_all);
        mStubNoItem = $(R.id.stub_no_item);
        mTvTotalPrice = $(R.id.tv_shop_cart_total_price);
        mIconSelectAll.setTag(0);
        $(R.id.icon_shop_cart_select_all).setOnClickListener(this);
        $(R.id.tv_top_shop_cart_remove_selected).setOnClickListener(this);
        $(R.id.tv_top_shop_cart_clear).setOnClickListener(this);
        $(R.id.tv_shop_cart_pay).setOnClickListener(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .url("shop_cart.php")
                .loader(getContext())
                .success(this)
                .build()
                .get();
    }

    @Override
    public void onSuccess(String response) {
        final ArrayList<MultipleItemEntity> data =
                new ShopCartDataConverter()
                        .setJsonData(response)
                        .convert();
        mAdapter = new ShopCartAdapter(data);
        mAdapter.setCartItemListener(this);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        mTotalPrice = mAdapter.getTotalPrice();
        mTvTotalPrice.setText(String.valueOf(mTotalPrice));
        checkItemCount();
    }

    @Override
    public void onItemClick(double itemTotalPrice) {
        final double price = mAdapter.getTotalPrice();
        mTvTotalPrice.setText(String.valueOf(price));
    }

    @Override
    public void onPaySuccess() {

    }

    @Override
    public void onPaying() {

    }

    @Override
    public void onPayFail() {

    }

    @Override
    public void onPayCancel() {

    }

    @Override
    public void onPayConnectError() {

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.icon_shop_cart_select_all) {
            onClickSelectAll();

        } else if (i == R.id.tv_top_shop_cart_remove_selected) {
            onClickRemoveSelectedItem();

        } else if (i == R.id.tv_top_shop_cart_clear) {
            onClickClear();

        } else if (i == R.id.tv_shop_cart_pay) {
            createOrder();

        }
    }
}
