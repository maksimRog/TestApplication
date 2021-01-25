package com.mraha.weatherforecast.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {AppData.class}, version =1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDataDAO appDataDAO();
}
