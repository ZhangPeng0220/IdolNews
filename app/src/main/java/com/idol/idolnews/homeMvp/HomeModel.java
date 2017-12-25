package com.idol.idolnews.homeMvp;

import com.idol.idolnews.API.Networks;
import com.idol.idolnews.bean.BeforeDailyEntity;
import com.idol.idolnews.bean.LatestDailyEntity;

import rx.Observable;

/**
 * Created by 53478 on 2017/12/12.
 */

public class HomeModel implements HomeContract.Model {
    @Override
    public Observable<LatestDailyEntity> getLatestDaily() {
        return Networks.getInstance().getThemeApi().getLatestDaily();
    }

    @Override
    public Observable<BeforeDailyEntity> getBeforeDaily(String date) {
        return Networks.getInstance().getThemeApi().getBeforeDaily(date);
    }
}
