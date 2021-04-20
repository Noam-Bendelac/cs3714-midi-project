package com.example.midiproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class demo extends AppCompatActivity implements View.OnClickListener {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    public void onClick(View view) {
        MidiPlayer md = MidiPlayer.getInstance(this);
        md.playNoteOn(0, 0);
    }
}
