package com.hyerodrimm.notificationnotes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteSaveDao {
    @Query("SELECT * FROM note_saves")
    public List<NoteSave> getAll();

//    @Query("SELECT * FROM note_saves WHERE is_favourite=0")
//    public List<NoteSave> getAllNonFavourites();
//
//    @Query("SELECT * FROM note_saves WHERE is_favourite=1")
//    public List<NoteSave> getAllFavourites();

    @Insert
    public long[] insertAll(NoteSave... noteSaves);

    @Insert
    public long insert(NoteSave noteSave);

    @Delete
    public void delete(NoteSave noteSave);
}
