package com.example.shikooo.wallpaper.Model;

/**
 * Created by shikooo on 8/12/2018.
 */

public class WallpaperItem {

    public String imageurl;
    public String categoryId;
    public Long viewCount;

    public WallpaperItem(String imageurl, String categoryId, Long viewCount) {
        this.imageurl = imageurl;
        this.categoryId = categoryId;
        this.viewCount = viewCount;
    }
    public WallpaperItem(String imageurl, String categoryId) {
        this.imageurl = imageurl;
        this.categoryId = categoryId;
    }

    public WallpaperItem() {
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

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
