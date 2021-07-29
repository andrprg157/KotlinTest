package com.nearby.wardrobetest.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Topwear.class,Bottomwear.class,Wishlist.class},version = 1,exportSchema = false)
public abstract class ApparelDatabase extends RoomDatabase {

    private static ApparelDatabase apparelInstance;

    public abstract AllDao allDao();

    public static synchronized ApparelDatabase getInstance(Context context){
        if (apparelInstance == null){
            apparelInstance = Room.databaseBuilder(context.getApplicationContext(),ApparelDatabase.class,
                    "Wardrobe")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return apparelInstance;
    }

}
