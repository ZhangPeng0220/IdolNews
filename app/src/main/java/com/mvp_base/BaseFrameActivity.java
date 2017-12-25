package com.mvp_base;

import android.os.Bundle;

import com.idol.idolnews.Utils.TUtil;
import com.idol.idolnews.common.BaseActivity;

/**
 * Created by Administrator on 2016/12/28.
 */

public abstract class BaseFrameActivity<P extends BasePresenter, M extends BaseModel> extends BaseActivity implements BaseView{
    public P mPresenter;

    public M mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (this instanceof BaseView) {
            mPresenter.attachVM(this, mModel);
        }

    }

    @Override
    protected void onDestroy() {
       /// if (mPresenter != null) mPresenter.detachVM();
        super.onDestroy();
    }


}
