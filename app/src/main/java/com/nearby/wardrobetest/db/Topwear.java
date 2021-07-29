package com.nearby.wardrobetest.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Topwear {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String path;

    public Topwear(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }
}
