package com.flj.latte.ec.main.personal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.diabin.latte.ec.R;
import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.ec.main.personal.address.AddressDelegate;
import com.flj.latte.ec.main.personal.list.ListAdapter;
import com.flj.latte.ec.main.personal.list.ListBean;
import com.flj.latte.ec.main.personal.list.ListItemType;
import com.flj.latte.ec.main.personal.order.OrderListDelegate;
import com.flj.latte.ec.main.personal.profile.UserProfileDelegate;
import com.flj.latte.ec.main.personal.settings.SettingsDelegate;
import com.flj.latte.ec.sign.SignInDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class PersonalDelegate extends BottomItemDelegate {
   private static final String TAG=PersonalDelegate.class.getSimpleName();

    public static final String ORDER_TYPE = "ORDER_TYPE";
    private Bundle mArgs = null;

    private SharedPreferences sp;

    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }

    private void onClickAllOrder() {
        mArgs.putString(ORDER_TYPE, "all");
        startOrderListByType();
    }

    private void onClickAvatar() {
        getParentDelegate().getSupportDelegate().start(new UserProfileDelegate());
    }

    private void startOrderListByType() {
        final OrderListDelegate delegate = new OrderListDelegate();
        delegate.setArguments(mArgs);
        getParentDelegate().getSupportDelegate().start(delegate);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = new Bundle();
        sp=getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        final RecyclerView rvSettings = $(R.id.rv_personal_setting);
        $(R.id.tv_all_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAllOrder();
            }
        });
        $(R.id.img_user_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAvatar();
            }
        });



        final ListBean system = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(1)
                .setDelegate(new SettingsDelegate())
                .setText("系统设置")
                .build();
        final ListBean exit = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setDelegate(new SignInDelegate())
                .setId(2)
                .setText("退出")
                .build();



        final List<ListBean> data = new ArrayList<>();

        data.add(system);
        data.add(exit);

        //设置RecyclerView
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvSettings.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        rvSettings.setAdapter(adapter);
        rvSettings.addOnItemTouchListener(new PersonalClickListener(this));
    }
}
