package com.idol.idolnews.ThemeMvp;

import com.idol.idolnews.API.Networks;
import com.idol.idolnews.bean.ThemeContentListEntity;

import rx.Observable;

/**
 * Created by 53478 on 2017/12/12.
 */

public class ThemeModel implements ThemeContract.Model {

    @Override
    public Observable<ThemeContentListEntity> getData(int id) {
        return Networks.getInstance().getThemeApi().getThemeContentList(id);
    }
}
