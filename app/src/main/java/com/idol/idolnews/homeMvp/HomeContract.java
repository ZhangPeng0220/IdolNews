package com.idol.idolnews.homeMvp;


import com.idol.idolnews.bean.BeforeDailyEntity;
import com.idol.idolnews.bean.LatestDailyEntity;
import com.mvp_base.BaseModel;
import com.mvp_base.BasePresenter;
import com.mvp_base.BaseView;

import rx.Observable;

/**
 * Created by Administrator on 2017/1/4.
 */

public interface HomeContract {

    interface Model extends BaseModel {
        Observable<LatestDailyEntity> getLatestDaily();

        Observable<BeforeDailyEntity> getBeforeDaily(String date);

        //Observable<ThemeContentListEntity> getThemeContentList(int id);
    }

    interface View extends BaseView {
        void getData(LatestDailyEntity entity);
        void setLastData();
        void getBeforeDate(BeforeDailyEntity entity);
        void setBeforeData();
       // void loadBeforeDaily(BeforeDailyEntity beforeDailyEntity);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getLatestDaily();

        abstract void getBeforeDaily(String date);

        abstract void getOtherThemeList(int id);
    }
}
