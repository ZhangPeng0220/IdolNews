package com.idol.idolnews.homeMvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idol.idolnews.API.MyService;
import com.idol.idolnews.ArticalDetailActivity;
import com.idol.idolnews.Banner.Banner;
import com.idol.idolnews.bean.MultipleItem;
import com.idol.idolnews.R;
import com.idol.idolnews.bean.BeforeDailyEntity;
import com.idol.idolnews.bean.LatestDailyEntity;
import com.idol.idolnews.bean.TopStoriesEntity;
import com.idol.idolnews.common.BaseFragment;
import com.idol.idolnews.loader.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Retrofit;

/**
 * Created by 上官若枫 on 2017/10/17.
 */

public class HomeFragment extends BaseFragment<HomePresenter,HomeModel> implements Banner.OnBannerClickListener,HomeContract.View {

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



    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        unbinder = ButterKnife.bind(this, getContentView());
    }

    public void initView(){

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        mPresenter.getLatestDaily();

        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mPresenter.getLatestDaily();
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
    public void setMoreDate(){
        multipleItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getBeforeDaily(date);
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
                        getContext().startActivity(intent);
                    }

                }

            }
        });

    }
    public void getBeforeDate(BeforeDailyEntity entity){
        moreList = new ArrayList<>();
        date = entity.getDate();
        MultipleItem item0 = new MultipleItem(MultipleItem.TEXT,null,date);
        moreList.add(item0);
        for(int i = 0;i < entity.getStories().size();i++){
            MultipleItem item1 = new MultipleItem(MultipleItem.IMG,entity.getStories().get(i),null);
            moreList.add(item1);
        }


    }
    public void setBeforeData(){
        multipleItemAdapter.addData(moreList);
        multipleItemAdapter.loadMoreComplete();
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

    @Override
    public void getData(LatestDailyEntity entity) {
        setTopData(entity);
        MultipleItem item0 = new MultipleItem(MultipleItem.TEXT,null,date);
        list.add(item0);
        for(int i = 0;i < entity.getStories().size();i++){
            MultipleItem item1 = new MultipleItem(MultipleItem.IMG,entity.getStories().get(i),null);
            list.add(item1);
        }
        date = entity.getDate();
    }
    public void setLastData(){
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
}
