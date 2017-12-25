package com.idol.idolnews;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.idol.idolnews.API.ThemesEntity;
import com.idol.idolnews.ThemeMvp.ThemeFragment;
import com.idol.idolnews.Utils.HtmlUtil;
import com.idol.idolnews.drawer.DrawerHeaderItem;
import com.idol.idolnews.drawer.DrawerHomeItem;
import com.idol.idolnews.drawer.HYDrawerMenuAdapter;
import com.idol.idolnews.drawer.drawerMvp.DrawerMainContract;
import com.idol.idolnews.drawer.drawerMvp.DrawerMainModel;
import com.idol.idolnews.drawer.drawerMvp.DrawerMainPresenter;
import com.idol.idolnews.homeMvp.HomeFragment;
import com.mvp_base.BaseFrameActivity;
import com.mvp_base.BaseView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseFrameActivity<DrawerMainPresenter,DrawerMainModel> implements BaseView, HYDrawerMenuAdapter.onItemClickListener, Toolbar.OnMenuItemClickListener ,DrawerMainContract.View{

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.drawer_menu_rc)
    RecyclerView rc;
    @BindView(R.id.navigation_view)
    LinearLayout navigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    private ArrayList mMainMenuList;
    private HYDrawerMenuAdapter mMenuAdapter;
    private HomeFragment homeFragment;
    private List<ThemesEntity.OthersEntity> others;
    private ThemeFragment otherFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mMainMenuList = new ArrayList<>();
        mMainMenuList.add(new DrawerHeaderItem());
        mMainMenuList.add(new DrawerHomeItem());
        mPresenter.getOtherThemes();
        initToolBar();
        initView();
        mMenuAdapter = new HYDrawerMenuAdapter(this, mMainMenuList);
        rc.setLayoutManager(layoutManager);
        rc.setAdapter(mMenuAdapter);
        mMenuAdapter.setOnItemClickListener(this);//监听接口回调
    }

    private void initToolBar() {
        toolBar.inflateMenu(R.menu.menu_main);
        toolBar.setTitle("首页");
        toolBar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_actionbar_menu));
        toolBar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_actionbar_menu_overflow));
        toolBar.setOnMenuItemClickListener(this);
        //toolBar.setNavigationOnClickListener(this);
    }



    @Override
    public void onDrawerHeaderItemClick() {
        Toast.makeText(this, "点击登录事件", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemViewClick(View view, RecyclerView.ViewHolder holder, int position) {
        mMenuAdapter.setSelection(position);
        mMenuAdapter.notifyDataSetChanged();
        if (position == 1) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(currentFragment)
                    .show(homeFragment)
                    .commit();
            currentFragment = homeFragment;
        } else {
            int id = others.get(position - 2).getId();
            otherFragment.setId(id);
            if (otherFragment.isAdded()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(currentFragment)
                        .show(otherFragment)
                        .commit();
                otherFragment.mPresenter.getLatestDaily(id);
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(currentFragment)
                        .add(R.id.container, otherFragment)
                        .show(otherFragment)
                        .commit();
            }
            currentFragment = otherFragment;

        }

        Toast.makeText(this, "点击切换界面", Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawers();

    }

    @Override
    public void onFollowIVClick(View view, RecyclerView.ViewHolder holder, int position, int offset) {

    }

    public void initView() {
        homeFragment = new HomeFragment();
        otherFragment = new ThemeFragment();
        currentFragment = homeFragment;
        getSupportFragmentManager()
                .beginTransaction()
                .hide(currentFragment)
                .add(R.id.container, homeFragment)
                .show(homeFragment)
                .commit();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch_model:
                int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (mode == Configuration.UI_MODE_NIGHT_YES) {
                    //SettingUtil.getInstance().setIsNightMode(false);如果是夜间模式，转为白天
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    HtmlUtil.model = 0;
                } else {
                    //SettingUtil.getInstance().setIsNightMode(true);如果是白天，转为夜间
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    HtmlUtil.model = 1;
                }
                //getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                recreate();

        }
        return false;
    }

    @Override
    public void loadOtherThemeList(ThemesEntity themesEntity) {
        //处理请求结果
        others = themesEntity.getOthers();
        mMainMenuList.addAll(others);
        mMenuAdapter.notifyDataSetChanged();
    }
}
