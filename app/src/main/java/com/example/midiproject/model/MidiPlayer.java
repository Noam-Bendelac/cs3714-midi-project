package com.example.midiproject.model;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.midiproject.R;

import org.jetbrains.annotations.NotNull;


/**
 * Represents internal midi synth "device"
 */
public class MidiPlayer {
    
    public static final int MAX_MIDI_PITCH = 127;
    
    private final Context mCtx;

    // Array of mediaPlayers currently playing
    // null element means not playing
    // not all these elements will be used, as not all 127 midi pitches can be played
    private final MediaPlayer[] mMediaPlayers = new MediaPlayer[MAX_MIDI_PITCH];
    
    
    // which instrument the synth will use. TODO must set this at the start of the keyboard and
    // drumpad pages
    private @NotNull Instrument mInstrument = Instrument.PIANO;
    public void setInstrument(@NotNull Instrument instrument) {
        mInstrument = instrument;
    }
    
    
    public MidiPlayer(Context ctx) {
        
        mCtx = ctx.getApplicationContext();
    }
    

    /**
     *
     *
     * @param pitch midi pitch number 0-127
     * @param velocity 0-127, ignored
     */
    public void noteOn(int pitch, int velocity) {
        assertPitchValid(pitch);
        if (mMediaPlayers[pitch] != null) {
            // already playing, restart note instead of creating player again
            mMediaPlayers[pitch].seekTo(0);
            // do we need to call start again?
        } else {
            mMediaPlayers[pitch] = MediaPlayer.create(mCtx, mInstrument.pitchToAudioFile(pitch));
            mMediaPlayers[pitch].start();
        }
    }

    /**
     *
     * @param pitch midi pitch number 0-127
     */
    public void noteOff(int pitch) {
        assertPitchValid(pitch);
        if (mMediaPlayers[pitch] != null) {
            mMediaPlayers[pitch].stop();
            // release is needed to free resources, otherwise there's a limit on how many media
            // players we can instantiate
            mMediaPlayers[pitch].release();
            mMediaPlayers[pitch] = null;
        }
    }
    
    
    private void assertPitchValid(int pitch) {
        if (pitch < 0 || pitch > MAX_MIDI_PITCH) {
            throw new IllegalArgumentException("pitch invalid: " + pitch);
        }
    }
    
}
