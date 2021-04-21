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
            R.raw.piano_c,
            R.raw.piano_d,
            R.raw.piano_e,
            R.raw.piano_f,
            R.raw.piano_g,
            R.raw.piano_a,
            R.raw.piano_b

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
        switch(pitch){
            case 0:
                keyboard[0] = MediaPlayer.create(context, NOTES[0]);
                keyboard[0].start();
                break;
            case 1:
                keyboard[1] = MediaPlayer.create(context, NOTES[1]);
                keyboard[1].start();
                break;
            case 2:
                keyboard[2] = MediaPlayer.create(context, NOTES[2]);
                keyboard[2].start();
                break;
            case 3:
                keyboard[3] = MediaPlayer.create(context, NOTES[3]);
                keyboard[3].start();
                break;
            case 4:
                keyboard[4] = MediaPlayer.create(context, NOTES[4]);
                keyboard[4].start();
                break;
            case 5:
                keyboard[5] = MediaPlayer.create(context, NOTES[5]);
                keyboard[5].start();
                break;
            case 6:
                keyboard[6] = MediaPlayer.create(context, NOTES[6]);
                keyboard[6].start();
                break;
        }
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
        else if (keyboard[1] != null) {
            keyboard[1].stop();
        }
        else if (keyboard[2] != null) {
            keyboard[2].stop();
        }
        else if (keyboard[3] != null) {
            keyboard[3].stop();
        }
        else if (keyboard[4] != null) {
            keyboard[4].stop();
        }
        else if (keyboard[5] != null) {
            keyboard[5].stop();
        }
        else if (keyboard[6] != null){
            keyboard[6].stop();
        }
    }
}
