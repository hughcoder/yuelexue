package com.flj.latte.ec.course_detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.diabin.latte.ec.R;
import com.flj.latte.app.YLXconst;
import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.ui.date.DateDialogUtil;
import com.flj.latte.util.expandtext.StretchUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by home on 2018/2/1.
 */

public class CourseDetailDelegate extends LatteDelegate {
    private static final String TAG = CourseDetailDelegate.class.getSimpleName();

    private TextView tv_info;
    private ImageView iv_arrow;
    private TextView tv_mark;

    private TextView tv_info2;
    private ImageView iv_arrow2;
    private TextView tv_mark2;
    private RelativeLayout relativeLayout = null;
    private AppCompatButton due_time = null;
    private TextView course_detail_booktime = null;
    private TextView time_select = null;

    //课程类
    private AppCompatTextView couse_title = null;
    private AppCompatTextView couse_address = null;
    private AppCompatImageView couse_img = null;
    private AppCompatTextView suitable_content = null;


    private static final String YLX_COURSE_NO = "YLX_COURSE_NO";
    private int course_no = -1;
    private RecyclerView rv_eir;
    private List<Integer> test = new ArrayList<>();

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    //设置图片加载策略
    private static final RequestOptions RECYCLER_OPTIONS =
            new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();

    @Override
    public Object setLayout() {
        return R.layout.delegate_course_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        couse_title = $(R.id.tv_course_title);
        couse_address = $(R.id.course_address);
        couse_img = $(R.id.iv_course_detail);
        suitable_content = $(R.id.tv_suitable_content);
        course_detail_booktime = $(R.id.tv_course_detail_booktime);
        sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        String token = sp.getString(YLXconst.YLX_TOKEN, "");
        initinfoui();
        initcontentui();
        relativeLayout = $(R.id.course_detail_booktime);
        // rv_eir=$(R.id.rv_teacher_background);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DateDialogUtil dateDialogUtil = new DateDialogUtil();
                dateDialogUtil.setDateListener(new DateDialogUtil.IDateListener() {
                    @Override
                    public void onDateChange(String date) {
                        final TextView textView = v.findViewById(R.id.tv_course_detail_booktime);
                        textView.setText(date);
                    }
                });
                dateDialogUtil.showDialog(getContext());
            }
        });
        initdata();
        time_select = $(R.id.tv_time_select);
        due_time = $(R.id.due_time);
        due_time.setOnClickListener(view -> {          //lamda表达式
            String time = course_detail_booktime.getText().toString();
            if (time.length() == 5) {
                Toast.makeText(getContext(), "未输入时间", Toast.LENGTH_LONG).show();
            } else {
                String appoint_time = time + " " + time_select.getText().toString();
                Log.e(TAG,appoint_time);
                String student_name = "钱力哥";
                int student_age = 8;
                RestClient.builder()
                        .url("/api/appointment/save")
                        .params("course_no", course_no)
                        .params("appoint_time", appoint_time)
                        .params("student_name", student_name)
                        .params("student_age", student_age)
                        .params("token", token)
                        // .params("token",)
                        .success(response -> {
                            Log.e(TAG, "" + response);
                            //JSON数据解析
                            if (response != null) {
                                    final JSONObject data = JSON.parseObject(response).getJSONObject("data");
                                    final String new_token = data.getString("token");
                                    sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                                    Log.e(TAG, new_token);
                                    //   editor.putString(YLXconst.YLX_TOKEN,new_token);
                                    // editor.apply();
                                    //   final String new_token2=sp.getString(YLXconst.YLX_TOKEN,"");
                                    Toast.makeText(getContext(), "预约成功", Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "预约成功");

                                }
                        })
                        .build()
                        .post();
            }
        });
    }


    private void initinfoui() {
        tv_info = $(R.id.tv_course_detail_info);
        iv_arrow = $(R.id.iv_arrow);
        tv_mark = $(R.id.tv_mark);
        StretchUtil.getInstance(tv_info, tv_mark, 3, iv_arrow, R.drawable.arrow_up, R.drawable.arrow_down).initStretch();
    }

    private void initcontentui() {
        tv_info2 = $(R.id.tv_course_detail_content);
        iv_arrow2 = $(R.id.iv_arrow2);
        tv_mark2 = $(R.id.tv_mark2);
        StretchUtil.getInstance(tv_info2, tv_mark2, 3, iv_arrow2, R.drawable.arrow_up, R.drawable.arrow_down).initStretch();
    }

    public static CourseDetailDelegate create(@NonNull int course_no) {
        final Bundle args = new Bundle();
        args.putInt(YLX_COURSE_NO, course_no);
        final CourseDetailDelegate delegate = new CourseDetailDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            course_no = args.getInt(YLX_COURSE_NO);
        }
    }


    private void initdata() {
        RestClient.builder()
                .url("/api/course/detail")
                .loader(getContext())
                .params("course_no", course_no)
                .success(response -> {           // lambda表达式替代匿名内部类
                    Log.e(TAG, "" + course_no);
                    Log.e(TAG, "" + response);
                    final JSONObject data =
                            JSON.parseObject(response).getJSONObject("data");
                    final JSONObject courseBusinessData = data.getJSONObject("courseBusinessData");
                    final JSONArray jsonArray = data.getJSONArray("audition");
                    //其实这个预约时间我还没用上
                    List<String> ordertimes = new ArrayList<>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        final String ordertime = jsonArray.getString(i);
                        ordertimes.add(ordertime);
                    }
                    int test=ordertimes.size();
                    Log.e(TAG,""+test);
                    initinfo(data);
                })
                .build()
                .get();
    }

    private void initinfo(JSONObject data) {
        final String course_name = data.getString("course_name");
        final String address = data.getString("address");
        final String image_url_big = data.getString("image_url_big");
        final String description = data.getString("description");
        final String course_content = data.getString("course_content");
        final String suitable_content_value = data.getString("suitable_content");

        suitable_content.setText(suitable_content_value);
        couse_title.setText(course_name);
        couse_address.setText(address);
        tv_info.setText(description);
        tv_info2.setText(course_content);
        Glide.with(getContext())
                .load(image_url_big)
                .apply(RECYCLER_OPTIONS)
                .into(couse_img);
    }
}
