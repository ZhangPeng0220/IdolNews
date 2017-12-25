package com.idol.idolnews.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2017/1/10.
 */
@Entity
public class TopStoriesEntity  {
    /**
     * title : 商场和很多人家里，竹制家具越来越多（多图）
     * image : http://p2.zhimg.com/9a/15/9a1570bb9e5fa53ae9fb9269a56ee019.jpg
     * ga_prefix : 052315
     * type : 0
     * id : 3930883
     */

    private String title;
    private String image;
    private String ga_prefix;
    private int type;
    @Id
    private int id;

    @Generated(hash = 57242088)
    public TopStoriesEntity(String title, String image, String ga_prefix, int type,
            int id) {
        this.title = title;
        this.image = image;
        this.ga_prefix = ga_prefix;
        this.type = type;
        this.id = id;
    }

    @Generated(hash = 666542542)
    public TopStoriesEntity() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
