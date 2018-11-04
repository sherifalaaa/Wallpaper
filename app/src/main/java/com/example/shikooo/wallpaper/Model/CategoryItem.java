package com.example.shikooo.wallpaper.Model;

/**
 * Created by shikooo on 8/9/2018.
 */

public class CategoryItem {

    public String name;
    public String imageurl;

    public CategoryItem(String name, String imageurl) {
        this.name = name;
        this.imageurl = imageurl;
    }

    public CategoryItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
