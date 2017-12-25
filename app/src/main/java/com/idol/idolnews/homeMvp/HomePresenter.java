package com.idol.idolnews.homeMvp;

import com.idol.idolnews.bean.BeforeDailyEntity;
import com.idol.idolnews.bean.LatestDailyEntity;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 53478 on 2017/12/12.
 */

public class HomePresenter extends HomeContract.Presenter{
    @Override
    void getLatestDaily() {
        mModel.getLatestDaily()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LatestDailyEntity>() {
                    @Override
                    public void onCompleted() {
                        mView.setLastData();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LatestDailyEntity entity) {
                        mView.getData(entity);
                    }
                });
    }

    @Override
    void getBeforeDaily(String date) {
        mModel.getBeforeDaily(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BeforeDailyEntity>() {
                    @Override
                    public void onCompleted() {
                        mView.setBeforeData();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BeforeDailyEntity entity) {
                        mView.getBeforeDate(entity);
                    }
                });
    }

    @Override
    void getOtherThemeList(int id) {

    }
}
