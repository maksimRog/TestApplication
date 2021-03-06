package com.mraha.weatherforecast.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AppDataDAO {
    @Query("SELECT * FROM appdata")
    List<AppData> getAll();

    @Insert
    void insertAll(AppData... data);
}
