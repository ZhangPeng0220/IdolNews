package com.idol.idolnews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.idol.idolnews.API.MyService;
import com.idol.idolnews.entity.StoryContentEntity;
import com.idol.idolnews.loader.GlideImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ArticalDetailActivity extends AppCompatActivity {


    @BindView(R.id.detail_bar_image)
    ImageView detailBarImage;
    @BindView(R.id.detail_bar_title)
    TextView detailBarTitle;
    @BindView(R.id.detail_bar_copyright)
    TextView detailBarCopyright;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.wv_detail_content)
    WebView detailContentWV;
    @BindView(R.id.nsv_scroller)
    NestedScrollView nsvScroller;
    @BindView(R.id.activity_editor_detail)
    CoordinatorLayout activityEditorDetail;
    private Retrofit retrofit;
    private MyService service;
    private int articleId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
       // appBar.setExpanded(false);
        initWebViewClient();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (detailContentWV != null) {
            int scrollY = nsvScroller.getScrollY();
            CacheUtils.putInt(this, articleId+"", scrollY);//保存访问的位置
        }
    }

    public void initData() {
        Intent intent = getIntent();
        articleId = intent.getIntExtra("articleId", 0);
        if (articleId != 0) {
            getData(articleId);
        } else {
            Toast.makeText(this, "数据加载出错", Toast.LENGTH_SHORT).show();
        }
    }
    public void getData(int id){
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//新的配置
                .baseUrl("http://news-at.zhihu.com")
                .build();
        service = retrofit.create(MyService.class);
        service.getStoryContent(id)
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                .doOnNext(new Action1<StoryContentEntity>() {

                    @Override
                    public void call(StoryContentEntity latestDailyEntity) {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<StoryContentEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(StoryContentEntity entity) {
                        String imgUrl = entity.getImage();
                        GlideImageLoader.getInstance().displayImage(ArticalDetailActivity.this, imgUrl, detailBarImage);
                        detailBarTitle.setText(entity.getTitle());
                        detailBarCopyright.setText(entity.getImage_source());
                        String htmlData = HtmlUtil.createHtmlData(entity.getBody(), entity.getCss(), entity.getJs());
                        Log.i("被说话",htmlData);
                        detailContentWV.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                    }
                });
    }
    private void initWebViewClient() {
        WebSettings settings = detailContentWV.getSettings();
       /* if (SharePreferencesHelper.getInstance(this).getBoolean(Constants.WebViewSetting.SP_NO_IMAGE,false)) {
            settings.setBlockNetworkImage(true);
        }
        if (SharePreferencesHelper.getInstance(this).getBoolean(Constants.WebViewSetting.SP_AUTO_CACHE,true)) {
            settings.setAppCacheEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            if (NetUtils.isConnected(this)) {
                settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
            }
        }*/
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
       // detailContentWV.setBackgroundColor();
        detailContentWV.setWebViewClient(new WebViewClientEmb() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
    public class WebViewClientEmb extends WebViewClient {
        private int position;
        // 在WebView中而不是系统默认浏览器中显示页面
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            System.out.println("Url---------->"+url);
            return true;
        }

        // 页面载入前调用
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            position = CacheUtils.getInt(ArticalDetailActivity.this,articleId+"", 0);
            if(position >0){
                appBar.setExpanded(false);
            }

        }

        // 页面载入完成后调用
        @Override
        public void onPageFinished(WebView webView, String url) {

            nsvScroller.scrollTo(0, position);//webview加载完成后直接定位到上次访问的位置
            //mLoadingDialog.dismiss();
        }
    }
}
