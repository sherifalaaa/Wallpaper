package com.example.shikooo.wallpaper.Database.LocalDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.shikooo.wallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by shikooo on 8/15/2018.
 */


@Dao
public interface RecentsDAO {

    @Query("SELECT * FROM recents ORDER BY saveTime DESC LIMIT 10")
    Flowable<List<Recents>> getAllRecents();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecents (Recents... recents);

    //may its need a list of data
    @Update
    void updateRecents (Recents... recents);

    @Delete
    void deleteRecents (Recents... recents);

    @Query("DELETE FROM recents")
    void deleteAllRecents();


}
