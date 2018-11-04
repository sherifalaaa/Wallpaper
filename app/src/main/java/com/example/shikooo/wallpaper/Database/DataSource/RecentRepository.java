package com.example.shikooo.wallpaper.Database.DataSource;

import com.example.shikooo.wallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by shikooo on 8/15/2018.
 */

public class RecentRepository implements IRecentsDataSource {

    private IRecentsDataSource mLocalDatabaseSources;
    private static RecentRepository instance;

    public RecentRepository(IRecentsDataSource mLocalDatabaseSources) {
        this.mLocalDatabaseSources = mLocalDatabaseSources;
    }

    public static RecentRepository getInstance (IRecentsDataSource mLocalDatabaseSources)
    {
        if (instance == null)
        {
            instance = new RecentRepository(mLocalDatabaseSources);
        }
        return instance;
    }

    @Override
    public Flowable<List<Recents>> getAllRecents() {
        return mLocalDatabaseSources.getAllRecents();
    }

    @Override
    public void insertRecents(Recents... recents) {
        mLocalDatabaseSources.insertRecents(recents);
    }

    @Override
    public void updateRecents(Recents... recents) {
        mLocalDatabaseSources.updateRecents();
    }

    @Override
    public void deleteRecents(Recents... recents) {
        mLocalDatabaseSources.deleteRecents();
    }

    @Override
    public void deleteAllRecents() {
        mLocalDatabaseSources.deleteAllRecents();
    }
}
