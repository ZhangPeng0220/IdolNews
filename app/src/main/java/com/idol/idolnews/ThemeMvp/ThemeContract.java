package com.idol.idolnews.ThemeMvp;

import com.idol.idolnews.bean.ThemeContentListEntity;
import com.mvp_base.BaseModel;
import com.mvp_base.BasePresenter;
import com.mvp_base.BaseView;

import rx.Observable;

/**
 * Created by 53478 on 2017/12/13.
 */

public interface ThemeContract {
    interface Model extends BaseModel {
      Observable<ThemeContentListEntity> getData(int id);

        //Observable<BeforeDailyEntity> getBeforeDaily(String date);

        //Observable<ThemeContentListEntity> getThemeContentList(int id);
    }

    interface View extends BaseView {
        void getData(ThemeContentListEntity entity);
        void setData();

    }

    abstract class Presenter extends BasePresenter<Model, View> {
       public   abstract void getLatestDaily(int id);

        /*abstract void getBeforeDaily(String date);

        abstract void getOtherThemeList(int id);*/
    }
}
