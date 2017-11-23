package com.idol.idolnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idol.idolnews.API.MyService;
import com.idol.idolnews.entity.ThemeContentListEntity;
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
 * Created by 53478 on 2017/11/7.
 */

public class ThemeFragment extends Fragment  {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    Unbinder unbinder;
    private List<MultipleItem> list;
    private Retrofit retrofit;
    private MyService service;
    private MultiThemeAdapter themeAdapter;
    private String backImage,backText;
    private ImageView imageView;
    private TextView textView;
    private ThemeContentListEntity mentity;
    private int id;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }
    public void initView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getDate();
        swipeLayout.setRefreshing(true);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        list.clear();

                        getDate();

                    }
                }
        );
    }
    public void getDate(){
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//新的配置
                .baseUrl("http://news-at.zhihu.com")
                .build();
        service = retrofit.create(MyService.class);
        service.getThemeContentList(id)
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .doOnNext(new Action1<ThemeContentListEntity>() {

                    @Override
                    public void call(ThemeContentListEntity latestDailyEntity) {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<ThemeContentListEntity>() {
                    @Override
                    public void onCompleted() {
                        themeAdapter = new MultiThemeAdapter(getContext(),list);
                        themeAdapter.addHeaderView(getHeader());
                        GlideImageLoader.getInstance().displayImage(getContext(),backImage,imageView);
                        textView.setText(backText);
                        themeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
                        recyclerView.setAdapter(themeAdapter);
                        themeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                if(position > 0){
                                    if(list.get(position).getItemType() == MultipleItem.IMG){
                                        Intent intent = new Intent();
                                        intent.setClass(getContext(),ArticalDetailActivity.class);
                                        intent.putExtra("articleId",list.get(position).getEntity().getId());
                                        getContext().startActivity(intent);
                                    }

                                }

                            }
                        });
                        swipeLayout.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ThemeContentListEntity entity) {
                        list = new ArrayList<>();
                        backImage = entity.getImage();
                        backText = entity.getDescription();
                        MultipleItem item0 = new MultipleItem(MultipleItem.TEXT,entity);
                        list.add(item0);
                        for(int i = 0;i < entity.getStories().size();i++){
                            MultipleItem item1 = new MultipleItem(MultipleItem.IMG,entity.getStories().get(i),null);
                            list.add(item1);
                        }

                    }
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setId(int id) {
        this.id = id;
    }
    public View getHeader(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.theme_header,null);
        imageView = (ImageView) view.findViewById(R.id.theme_header_iv);
        textView = (TextView) view.findViewById(R.id.theme_header_tv);
        return view;
    }

}
