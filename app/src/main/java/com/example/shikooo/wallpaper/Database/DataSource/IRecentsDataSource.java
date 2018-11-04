package com.example.shikooo.wallpaper.Database.DataSource;

import com.example.shikooo.wallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by shikooo on 8/15/2018.
 */

public interface IRecentsDataSource {

    Flowable<List<Recents>> getAllRecents();
    void insertRecents (Recents... recents);
    void updateRecents (Recents... recents);
    void deleteRecents (Recents... recents);
    void deleteAllRecents();



}
