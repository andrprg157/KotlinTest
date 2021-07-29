package com.nearby.wardrobetest.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Wishlist {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int topId;

    private int bottomId;

    public Wishlist(int id, int topId, int bottomId) {
        this.id = id;
        this.topId = topId;
        this.bottomId = bottomId;
    }

    public int getId() {
        return id;
    }

    public int getTopId() {
        return topId;
    }

    public int getBottomId() {
        return bottomId;
    }
}
