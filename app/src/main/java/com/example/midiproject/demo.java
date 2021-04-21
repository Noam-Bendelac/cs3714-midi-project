package com.example.midiproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class demo extends AppCompatActivity implements View.OnClickListener {

    private Button buttonc;
    private Button buttond;
    private Button buttone;
    private Button buttonf;
    private Button buttong;
    private Button buttona;
    private Button buttonb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        buttonc = findViewById(R.id.buttonC);
        buttonc.setOnClickListener(this);

        buttond = findViewById(R.id.buttonD);
        buttond.setOnClickListener(this);

        buttone = findViewById(R.id.buttonE);
        buttone.setOnClickListener(this);

        buttonf = findViewById(R.id.buttonF);
        buttonf.setOnClickListener(this);

        buttong = findViewById(R.id.buttonG);
        buttong.setOnClickListener(this);

        buttona = findViewById(R.id.buttonA);
        buttona.setOnClickListener(this);

        buttonb = findViewById(R.id.buttonB);
        buttonb.setOnClickListener(this);


    }

    public void onClick(View view) {
        MidiPlayer md = MidiPlayer.getInstance(this);
        switch(view.getId()){
            case R.id.buttonC:
                md.playNoteOn(0, 0);
                break;
            case R.id.buttonD:
                md.playNoteOn(1, 0);
                break;
            case R.id.buttonE:
                md.playNoteOn(2, 0);
                break;
            case R.id.buttonF:
                md.playNoteOn(3, 0);
                break;
            case R.id.buttonG:
                md.playNoteOn(4, 0);
                break;
            case R.id.buttonA:
                md.playNoteOn(5, 0);
                break;
            case R.id.buttonB:
                md.playNoteOn(6, 0);
                break;
        }

    }
}
