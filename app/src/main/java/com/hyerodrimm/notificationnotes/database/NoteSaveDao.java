package com.hyerodrimm.notificationnotes.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteSaveDao {
    @Query("SELECT * FROM note_saves")
    List<NoteSave> getAll();

    @Insert
    long[] insertAll(NoteSave... noteSaves);

    @Insert
    long insert(NoteSave noteSave);
}
