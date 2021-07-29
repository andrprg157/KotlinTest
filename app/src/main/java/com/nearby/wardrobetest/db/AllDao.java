package com.nearby.wardrobetest.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AllDao {

    @Insert
    void insertTopWear(Topwear topwear);

    @Insert
    void insertBottomWear(Bottomwear bottomwear);

    @Insert
    void insertWishlist(Wishlist wishlist);

    @Delete
    void deleteWishlist(Wishlist wishlist);

    @Query("Select * from Topwear order by id desc")
    LiveData<List<Topwear>> getTopWear();

    @Query("Select * from Bottomwear order by id desc")
    LiveData<List<Bottomwear>> getBottomWear();

    @Query("Select * from Wishlist where topId = :tid and bottomId = :bid")
    LiveData<Wishlist> getWishData(int tid, int bid);
}
