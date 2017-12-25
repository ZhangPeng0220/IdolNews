package com.idol.idolnews.ThemeMvp;

import com.idol.idolnews.bean.ThemeContentListEntity;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 53478 on 2017/12/12.
 */

public class ThemePresenter extends ThemeContract.Presenter{

    @Override
   public void getLatestDaily(int id) {
        mModel.getData(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ThemeContentListEntity>() {
                    @Override
                    public void onCompleted() {
                        mView.setData();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ThemeContentListEntity entity) {
                        mView.getData(entity);
                    }
                });
    }
}


