package com.flj.latte.ec.main.audition;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.diabin.latte.ec.R;
import com.flj.latte.delegates.bottom.BottomItemDelegate;
import com.flj.latte.ec.main.audition.OverAuditionDelegate.OverAuditionDelegate;
import com.flj.latte.ec.main.audition.OverBookDelegate.OverBookDelegate;
import com.flj.latte.ec.main.audition.OverdueDelegate.OverdueDelegate;

/**
 * Created by home on 2018/1/30.
 */

public class AuditionDelegate extends BottomItemDelegate {
    private AppCompatButton overbook = null;
    private AppCompatButton overaudition = null;
    private AppCompatButton overdue = null;

    private Fragment fr_overbook, fr_overaudition, fr_overdue;

    @Override
    public Object setLayout() {
        return R.layout.delegate_audition;

    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        overbook = $(R.id.btn_overbook);
        overaudition = $(R.id.btn_overaudition);
        overdue = $(R.id.btn_overdue);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initialize();
    }

    private void initialize() {

        overbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                overbook.setTextColor(ContextCompat.getColor(_mActivity,R.color.app_main));
                setFragmentSelect(0);
            }
        });
        overaudition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                overaudition.setTextColor(ContextCompat.getColor(_mActivity,R.color.app_main));
                setFragmentSelect(1);
            }
        });
        overdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOtherTabs();
                overdue.setTextColor(ContextCompat.getColor(_mActivity,R.color.app_main));
                setFragmentSelect(2);
            }
        });
        setFragmentSelect(0);
    }

    private void setFragmentSelect(int i) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFrament(transaction);
        switch (i) {
            case 0:
                if (fr_overbook == null) {
                    fr_overbook = new OverBookDelegate();
                    transaction.add(R.id.main_fragment, fr_overbook);
                } else {
                    transaction.show(fr_overbook);
                }
                break;
            case 1:
                if (fr_overaudition == null) {
                    fr_overaudition = new OverAuditionDelegate();
                    transaction.add(R.id.main_fragment, fr_overaudition);
                } else {
                    transaction.show(fr_overaudition);
                }
                break;
            case 2:
                if (fr_overdue == null) {
                    fr_overdue = new OverdueDelegate();
                    transaction.add(R.id.main_fragment, fr_overdue);
                } else {
                    transaction.show(fr_overdue);
                }
                break;
            default:
                break;
        }
        transaction.commit();

    }

    private void hideFrament(FragmentTransaction transaction) {
        if (fr_overbook != null) {
            transaction.hide(fr_overbook);
        }
        if (fr_overaudition != null) {
            transaction.hide(fr_overaudition);
        }
        if (fr_overdue != null) {
            transaction.hide(fr_overdue);
        }
    }
    private void resetOtherTabs(){
        overbook.setTextColor(ContextCompat.getColor(_mActivity,R.color.app_color));
        overaudition.setTextColor(ContextCompat.getColor(_mActivity,R.color.app_color));
        overdue.setTextColor(ContextCompat.getColor(_mActivity,R.color.app_color));
    }

}
