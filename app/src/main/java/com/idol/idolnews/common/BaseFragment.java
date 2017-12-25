package com.idol.idolnews.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idol.idolnews.Utils.TUtil;
import com.mvp_base.BaseModel;
import com.mvp_base.BasePresenter;
import com.mvp_base.BaseView;

/**
 * Created by Administrator on 2016/12/31.
 */

public class BaseFragment<P extends BasePresenter, M extends BaseModel> extends Fragment implements BaseFuncIml,BaseView {
    private View mContentView;
    private ViewGroup container;

    public P mPresenter;

    public M mModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (this instanceof BaseView) {
            mPresenter.attachVM(this, mModel);
        }
        initData();
        initView();
        initListener();
        initLoad();
        this.container = container;
        return mContentView;
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContentView(int viewId) {
        this.mContentView = getActivity().getLayoutInflater().inflate(viewId, container, false);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initLoad() {

    }

    protected void openActivity(Class<? extends BaseActivity> toActivity) {
        openActivity(toActivity, null);
    }

    protected void openActivity(Class<? extends BaseActivity> toActivity, Bundle parameters) {
        Intent intent = new Intent(getActivity(), toActivity);
        if (parameters != null) {
            intent.putExtras(parameters);
        }
        startActivity(intent);
    }

}
