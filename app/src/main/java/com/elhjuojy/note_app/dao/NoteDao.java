package com.elhjuojy.note_app.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.elhjuojy.note_app.entities.Notes;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notes> getAllNotes();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Notes notes);

    @Delete
    void deleteNote(Notes note);
}
