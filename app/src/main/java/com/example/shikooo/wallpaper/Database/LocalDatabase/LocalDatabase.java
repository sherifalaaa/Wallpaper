package com.example.shikooo.wallpaper.Database.LocalDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.shikooo.wallpaper.Database.Recents;

import static com.example.shikooo.wallpaper.Database.LocalDatabase.LocalDatabase.DATABASE_VERSION;

/**
 * Created by shikooo on 8/15/2018.
 */

@Database(entities = {Recents.class},version = DATABASE_VERSION)
public abstract class LocalDatabase extends RoomDatabase {

        public static final int DATABASE_VERSION = 6;
        public static final String DATABASE_NAME = "Wallpaper";

        public abstract RecentsDAO recentsDAO ();

        public static LocalDatabase instance;

        public static LocalDatabase getInstance (Context context)
        {
            if(instance ==null)
            {
                instance = Room.databaseBuilder(context.getApplicationContext(),LocalDatabase.class,DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return instance;
        }

}
