package com.idol.idolnews.drawer.drawerMvp;

import com.idol.idolnews.API.Networks;
import com.idol.idolnews.API.ThemesEntity;

import rx.Observable;

/**
 * Created by Administrator on 2016/12/31.
 */

public class DrawerMainModel implements DrawerMainContract.Model{
    @Override
    public Observable<ThemesEntity> getOtherThemeList() {
        return Networks.getInstance().getThemeApi().getThemes();
    }
}
