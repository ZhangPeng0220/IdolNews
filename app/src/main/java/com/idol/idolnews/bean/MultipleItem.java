package com.idol.idolnews.bean;

import android.util.Log;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.idol.idolnews.Utils.DateUtils;

import java.util.Date;

/**
 * Created by 上官若枫 on 2017/10/20.
 */

public class MultipleItem implements MultiItemEntity {
    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int MUL = 3;
    private int itemType;
    private StoriesEntity entity;
    private ThemeContentListEntity themeContentListEntity;
    private String date;
    private String formatDate;
    private Date dateTime ;

    public String getData() {
        Log.i("时间快艇",date);
        if (date  == null) {
            return null;
        }
        dateTime = DateUtils.str2date(date, "yyyyMMdd");
        formatDate = DateUtils.date2str(dateTime);
        return formatDate;
    }

    public MultipleItem(int itemType, StoriesEntity entity, String date) {
        this.itemType = itemType;
        this.entity = entity;
        this.date = date;
        //Log.i("日期不对1",data);
    }
    public MultipleItem(int itemType, ThemeContentListEntity entity) {
        this.itemType = itemType;
        this.themeContentListEntity = entity;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
    public StoriesEntity getEntity(){
        return entity;
    }
    public ThemeContentListEntity getThemeContentListEntity(){return themeContentListEntity;}
}
