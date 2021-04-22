package com.example.midiproject.model;

import android.widget.Button;

public class Note {
    Button button;
    String note;

    public Note(Button button, String note){
        this.button = button;
        this.note = convertNote(note);
    }

    private String convertNote(String note) {
        note.toLowerCase();
        note.replace('#', '_');
        return note;
    }

    public Button getButton() {
        return button;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        button.setText(note);
        this.note = convertNote(note);
    }
}
