package com.idol.idolnews.API;

import com.idol.idolnews.entity.BeforeDailyEntity;
import com.idol.idolnews.entity.LatestDailyEntity;
import com.idol.idolnews.entity.StoryContentEntity;
import com.idol.idolnews.entity.ThemeContentListEntity;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 上官若枫 on 2017/10/11.
 */

public interface MyService {
    @GET("api/4/themes" )
    Observable<ThemesEntity> getThemes();
    @GET(" api/4/news/latest")
    Observable<LatestDailyEntity> getLatestDaily();
    /**
     * 获取以前的文章列表
     * @return
     */
    @GET("api/4/news/before/{date}")
    Observable<BeforeDailyEntity> getBeforeDaily(@Path("date") String date);
    /**
     * 获取主题日报内容列表
     * @param themeId 主题日报id
     */
    @GET("api/4/theme/{themeId}")
    Observable<ThemeContentListEntity> getThemeContentList(@Path("themeId") int themeId);
    @GET("api/4/news/{id}")
    Observable<StoryContentEntity>getStoryContent(@Path("id") int themeId);

}
