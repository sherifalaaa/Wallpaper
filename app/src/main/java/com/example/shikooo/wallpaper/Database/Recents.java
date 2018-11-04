package com.example.shikooo.wallpaper.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by shikooo on 8/15/2018.
 */

@Entity(tableName = "recents")
public class Recents {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "imageurl")
    private String imageurl;

    @NonNull
    @ColumnInfo(name = "categoryId")
    private String categoryId;

    @ColumnInfo(name = "saveTime")
    private String saveTime;

    @ColumnInfo(name = "key")
    private String key;


    public Recents(@NonNull String imageurl, @NonNull String categoryId,String saveTime) {
        this.imageurl = imageurl;
        this.categoryId = categoryId;
        this.saveTime = saveTime;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
