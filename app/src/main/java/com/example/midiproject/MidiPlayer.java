package com.example.midiproject;

import android.content.Context;
import android.media.MediaPlayer;

public class MidiPlayer {

    // Number of notes on the keyboard
    final static int NUM_NOTES = 16;

    // Array of notes being played
    private MediaPlayer keyboard[] = new MediaPlayer[NUM_NOTES];

    // 16 Different note sounds
    static final int[] NOTES = new int[] {
            R.raw.piano_c
    };

    private static MidiPlayer instance;
    private static Context context;

    public static MidiPlayer getInstance(Context ctx) {
        if (instance == null) {
            instance = new MidiPlayer(ctx);
        }
        return instance;
    }

    public MidiPlayer(Context ctx) {
        context = ctx;
    }

    /**
     *
     *
     * @param pitch
     * @param velocity
     */
    public void playNoteOn(int pitch, int velocity) {
        keyboard[0] = MediaPlayer.create(context, NOTES[0]);
        keyboard[0].start();
        //player.setOnCompletionListener(this);
    }

    /**
     *
     * @param pitch
     */
    public void sendNoteOff(int pitch) {
        if (keyboard[0] != null) {
            keyboard[0].stop();
        }
    }
}
