package com.elhjuojy.note_app.listeners;

import com.elhjuojy.note_app.entities.Notes;

public interface NotesListener {
    void onNoteClicked(Notes note , int position );
}
