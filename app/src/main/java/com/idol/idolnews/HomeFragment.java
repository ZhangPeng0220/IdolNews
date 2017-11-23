package com.idol.idolnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idol.idolnews.API.MyService;
import com.idol.idolnews.Banner.Banner;
import com.idol.idolnews.entity.BeforeDailyEntity;
import com.idol.idolnews.entity.LatestDailyEntity;
import com.idol.idolnews.entity.TopStoriesEntity;
import com.idol.idolnews.loader.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 上官若枫 on 2017/10/17.
 */

public class HomeFragment extends Fragment implements Banner.OnBannerClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    private List<MultipleItem> list;
    private List<MultipleItem> moreList;
    Unbinder unbinder;
    private  MultipleItemAdapter multipleItemAdapter;
    private  Banner banner;
    private  List<String> imageList;
    private  List<Integer> idList;
    private  List<String> titleList;
    private List<TopStoriesEntity> topList;
    private Retrofit retrofit;
    private MyService service;
    private String date;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    public void initView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        getData();
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getData();
                    }
                }
        );
    }
    public View getHeader(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.home_header,null);
        banner = (Banner) view.findViewById(R.id.banner);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void getData(){
         retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//新的配置
                .baseUrl("http://news-at.zhihu.com")
                .build();
        service = retrofit.create(MyService.class);
        service.getLatestDaily()
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .doOnNext(new Action1<LatestDailyEntity>() {

                    @Override
                    public void call(LatestDailyEntity latestDailyEntity) {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<LatestDailyEntity>() {
                    @Override
                    public void onCompleted() {
                        multipleItemAdapter = new MultipleItemAdapter(getContext(),list);

                        multipleItemAdapter.addHeaderView(getHeader());
                        setMoreDate();
                        banner.setImages(imageList)
                                .setBannerTitles(titleList)
                                .setImageLoader(GlideImageLoader.getInstance())
                                .setOnBannerClickListener(HomeFragment.this)
                                .start();

                        multipleItemAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
                        recyclerView.setAdapter(multipleItemAdapter);
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LatestDailyEntity entity) {
                        setTopData(entity);
                        MultipleItem item0 = new MultipleItem(MultipleItem.TEXT,null,date);
                        list.add(item0);
                        for(int i = 0;i < entity.getStories().size();i++){
                            MultipleItem item1 = new MultipleItem(MultipleItem.IMG,entity.getStories().get(i),null);
                            list.add(item1);
                        }
                        date = entity.getDate();
                        //getBeforeDate(entity.getDate());
                    }
                });
    }
    public void setMoreDate(){
        multipleItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getBeforeDate();
            }
        },recyclerView);
        multipleItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position > 0){
                    if(list.get(position).getItemType() == MultipleItem.IMG){
                        Intent intent = new Intent();
                        intent.setClass(getContext(),ArticalDetailActivity.class);
                        intent.putExtra("articleId",list.get(position).getEntity().getId());
                        Log.i("我的鞋子",list.get(position).getEntity().getId()+"");
                        getContext().startActivity(intent);
                    }

                }

            }
        });

    }
    public List<MultipleItem> getBeforeDate(){
        service.getBeforeDaily(date)
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .doOnNext(new Action1<BeforeDailyEntity>() {

                    @Override
                    public void call(BeforeDailyEntity beforeDailyEntity) {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<BeforeDailyEntity>() {
                    @Override
                    public void onCompleted() {
                        multipleItemAdapter.addData(moreList);
                        multipleItemAdapter.loadMoreComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "加载数据失败", Toast.LENGTH_LONG).show();
                        multipleItemAdapter.loadMoreFail();
                    }

                    @Override
                    public void onNext(BeforeDailyEntity entity) {
                        moreList = new ArrayList<>();
                        date = entity.getDate();
                        MultipleItem item0 = new MultipleItem(MultipleItem.TEXT,null,date);
                        moreList.add(item0);
                        for(int i = 0;i < entity.getStories().size();i++){
                            MultipleItem item1 = new MultipleItem(MultipleItem.IMG,entity.getStories().get(i),null);
                            moreList.add(item1);
                        }
                        Log.i("idol123",date+"888");
                    }
                });
        return moreList;
    }
    public void setTopData(LatestDailyEntity entity) {
        imageList = new ArrayList();
        titleList = new ArrayList();
        idList = new ArrayList();
        topList = entity.getTop_stories();
        for(int i = 0;i < topList.size();i++){
            imageList.add(topList.get(i).getImage());
            titleList.add(topList.get(i).getTitle());
            idList.add(topList.get(i).getId());
        }
    }


    @Override
    public void OnBannerClick(int position) {
        Intent intent = new Intent();
        intent.setClass(getContext(),ArticalDetailActivity.class);
        intent.putExtra("articleId",idList.get(position));
        getContext().startActivity(intent);
    }
}
