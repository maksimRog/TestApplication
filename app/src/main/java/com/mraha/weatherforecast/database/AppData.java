package com.mraha.weatherforecast.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AppData {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    public String data;
}
