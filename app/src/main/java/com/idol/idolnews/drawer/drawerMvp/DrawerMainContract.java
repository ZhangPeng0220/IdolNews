package com.idol.idolnews.drawer.drawerMvp;

import com.idol.idolnews.API.ThemesEntity;
import com.mvp_base.BaseModel;
import com.mvp_base.BasePresenter;
import com.mvp_base.BaseView;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/31.
 */

public interface DrawerMainContract {

    interface Model extends BaseModel {
        Observable<ThemesEntity> getOtherThemeList();
    }

    interface View extends BaseView {
        void loadOtherThemeList(ThemesEntity themesEntity);
    }

    abstract class Presenter extends BasePresenter<Model,View> {
        abstract void getOtherThemes();
    }
}
