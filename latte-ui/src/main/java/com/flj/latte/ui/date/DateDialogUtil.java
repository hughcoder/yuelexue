package com.flj.latte.ui.date;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by 傅令杰
 */

public class DateDialogUtil {

    public interface IDateListener {

        void onDateChange(String date);
    }

    private IDateListener mDateListener = null;

    public void setDateListener(IDateListener listener) {
        this.mDateListener = listener;
    }

    public void showDialog(Context context) {
        final LinearLayout ll = new LinearLayout(context);
        final DatePicker picker = new DatePicker(context);
        final LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

        picker.setLayoutParams(lp);
         Calendar inttime=Calendar.getInstance();

        picker.init(inttime.get(Calendar.YEAR), inttime.get(Calendar.MONTH), inttime.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                final String data = format.format(calendar.getTime());
                if(mDateListener!=null){
                    mDateListener.onDateChange(data);
                }
            }
        });

        ll.addView(picker);

        new AlertDialog.Builder(context)
                .setTitle("选择日期")
                .setView(ll)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

}
