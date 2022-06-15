package com.elhjuojy.note_app.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.elhjuojy.note_app.dao.NoteDao;
import com.elhjuojy.note_app.entities.Notes;

@Database(entities = Notes.class,version = 1,exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    private static  NotesDatabase notesDatabase;

    public static  synchronized NotesDatabase getDatabse(Context context){
        if(notesDatabase==null){
            notesDatabase = Room.databaseBuilder(
                    context,
                    NotesDatabase.class,
                    "notes_db"
            ).build();
        }
        return  notesDatabase;
    }

    public abstract NoteDao noteDao();


}
