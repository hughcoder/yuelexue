package com.flj.latte.ec.main.index;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.diabin.latte.ec.R;
import com.flj.latte.app.YLXconst;
import com.flj.latte.delegates.bottom.BottomItemDelegate;

import com.flj.latte.ec.main.EcBottomDelegate;
import com.flj.latte.net.RestClient;
import com.flj.latte.net.callback.ISuccess;
import com.flj.latte.ui.banner.BannerCreatornative;
import com.flj.latte.ui.recycler.BaseDecoration;
import com.flj.latte.ui.recycler.MultipleFields;
import com.flj.latte.ui.recycler.MultipleItemEntity;
import com.flj.latte.util.callback.CallbackManager;
import com.flj.latte.util.callback.CallbackType;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class IndexDelegate extends BottomItemDelegate implements OnItemClickListener {

    private static final String TAG = IndexDelegate.class.getCanonicalName();

    private RecyclerView mRecyclerView = null;
 //   private SwipeRefreshLayout mRefreshLayout = null;
    private RecyclerView artRecyclerView=null;

  //  private RefreshHandler mRefreshHandler = null;
    private ConvenientBanner mBanner=null;
    //确保初始化一次Banner，防止重复Item加载
    boolean mIsInitBanner = false;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView=$(R.id.hot_course_rv);
        artRecyclerView=$(R.id.art_course_rv);
       // mRefreshLayout = $(R.id.srl_index);
        mBanner=$(R.id.convenientBanner);

        final IconTextView mIconScan = $(R.id.icon_index_scan);
        final AppCompatEditText mSearchView = $(R.id.et_search_view);
        $(R.id.icon_index_scan).setOnClickListener(view -> startScanWithCheck(getParentDelegate()));

        //在mRefreshHandler里对adapter就行设置
     //  mRefreshHandler = RefreshHandler.create(mRefreshLayout, mRecyclerView, new IndexDataConverter());

        CallbackManager.getInstance()
                .addCallback(CallbackType.ON_SCAN, args ->
                        Toast.makeText(getContext(), "得到的二维码是" + args, Toast.LENGTH_LONG).show());
      //  mSearchView.setOnFocusChangeListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Toolbar toolbar = view.findViewById(R.id.tb_index);
        toolbar.getBackground().setAlpha(0);
        sp=getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        String s=sp.getString(YLXconst.YLX_TOKEN,"失败");
        Log.e(TAG,s);
    }
 /**  private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshLayout.setProgressViewOffset(true, 120, 300);
    }
*/
    private void initBanner() {
        if (!mIsInitBanner) {
            final int[] banners = new int[]{R.drawable.pic_home_banner, R.drawable.pic_home_banner, R.drawable.pic_home_banner};
            ArrayList<Integer> bannerlist=new ArrayList<>();
            int size=banners.length;
            for(int i=0;i<size;i++){
                bannerlist.add(banners[i]);
            }
            BannerCreatornative.setDefaultnative(mBanner, bannerlist, this);
            mIsInitBanner = true;
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
      //  initRefreshLayout();
        initBanner();
    //    mRefreshHandler.firstPage("index.php");
                RestClient.builder()
                .url("course/hot/list")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final LinearLayoutManager manager=new LinearLayoutManager(getContext());
                        manager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置横向
                        mRecyclerView.setLayoutManager(manager);
                        mRecyclerView.addItemDecoration(
                               BaseDecoration.create(ContextCompat.getColor(getContext(),R.color.app_background),8));
                        final List<MultipleItemEntity> data=
                                new IndexHotConverter().setJsonData(response).convert();
                      //String aa= data.get(0).getField(MultipleFields.IMAGE_URL);
                     // Toast.makeText(getContext(),""+aa,Toast.LENGTH_LONG).show();
                        final IndexHotadapter hotadapter=new IndexHotadapter(data);
                        //art课程介绍
                        mRecyclerView.setAdapter(hotadapter);
                    }
                })
                .build()
                .get();
                //art请求
             RestClient.builder()
                   //  .url("course/list")
                     .url("course/hot/list")
                     .loader(getContext())
                     .success(new ISuccess() {
                         @Override
                         public void onSuccess(String response2) {
                             final LinearLayoutManager artmanager=new LinearLayoutManager(getContext());
                             artmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
                             artRecyclerView.setLayoutManager(artmanager);

                            final List<MultipleItemEntity> data=
                                new IndexArtConverter().setJsonData(response2).convert();
                           final IndexArtadapter artadapter=new IndexArtadapter(data);
                         artRecyclerView.setAdapter(artadapter);
                         }
                     })
                     .build()
                     .get();
             final EcBottomDelegate ecBottomDelegate=getParentDelegate();
             mRecyclerView.addOnItemTouchListener(IndexHotItemClickListener.create(ecBottomDelegate));


    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }
/**
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            getParentDelegate().start(new SearchDelegate());
        }
    }
    */

    @Override
    public void onItemClick(int position) {

    }

}
