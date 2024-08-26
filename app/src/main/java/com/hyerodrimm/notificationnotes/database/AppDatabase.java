package com.hyerodrimm.notificationnotes.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NoteSave.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteSaveDao noteSaveDao();
}
