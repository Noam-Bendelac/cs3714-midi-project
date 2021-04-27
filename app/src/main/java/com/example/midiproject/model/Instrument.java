package com.example.midiproject.model;

import com.example.midiproject.R;

/**
 * Represents one of our 2 instruments, piano and drums. Handles getting audio file from midi pitch
 */
public abstract class Instrument {
  
  
  /**
   * @return number of notes on the keyboard/drumpad.
   * implement this getter in a subclass to return NUM_NOTES
   */
  // private static final int NUM_NOTES = 8;
  public abstract int getNumNotes();
  
  
  /**
   * implement this getter in a subclass to return an array AUDIO_FILES,
   * with indices matching the indices of getValidPitches() and length getNumNotes()
   */
  // protected final int[] AUDIO_FILES;
  protected abstract int[] getAudioFiles();
  
  
  /**
   *
   * @return array of valid midi pitches that SHOULD NOT BE MODIFIED (if only java had a feature to
   * enforce that...) with length getNumNotes()
   * implement this getter in a subclass to return an array MIDI_PITCHES
   */
  // protected final int[] MIDI_PITCHES;
  public abstract int[] getValidPitches();
  
  
  /**
   *
   * @param pitch midi pitch 0-127
   * @return audio file resource id, or AUDIO_RES_NONE
   */
  public int pitchToAudioFile(int pitch) {
    int numNotes = getNumNotes();
    int[] midiPitches = getValidPitches();
    int[] audioFiles = getAudioFiles();
    for (int i = 0; i < numNotes; i++) {
      if (midiPitches[i] == pitch) {
        return audioFiles[i];
      }
    }
    return AUDIO_RES_NONE;
  }
  
  
  
  
  // represents audio file that's not available
  public static final int AUDIO_RES_NONE = -1;
  
  
  // static instruments
  
  public static Instrument PIANO = new Instrument() {
  
    // Number of notes on the keyboard.
    private static final int NUM_NOTES = 8;
    @Override
    public int getNumNotes() {
      return NUM_NOTES;
    }
    
    // 16 Different note sounds
    private final int[] AUDIO_FILES = {
      R.raw.piano_c,
      R.raw.piano_d,
      R.raw.piano_e,
      R.raw.piano_f,
      R.raw.piano_g,
      R.raw.piano_a,
      R.raw.piano_b,
      // TODO change octave
      R.raw.piano_c,
    };
    @Override
    protected int[] getAudioFiles() {
      return AUDIO_FILES;
    }
    
    
    // the midi pitch of each of the above files
    // TODO this is the major scale for now. if we add black keys, this would just be 60, 61, 62, 63...
    private final int[] MIDI_PITCHES = {
      60,
      62,
      64,
      65,
      67,
      69,
      71,
      72,
    };
    @Override
    public int[] getValidPitches() {
      return MIDI_PITCHES;
    }
    
    @Override
    public int pitchToAudioFile(int pitch) {
      for (int i = 0; i < NUM_NOTES; i++) {
        if (MIDI_PITCHES[i] == pitch) {
          return AUDIO_FILES[i];
        }
      }
      return AUDIO_RES_NONE;
    }
    
  };
  
  public static Instrument DRUMS = new Instrument() {
    
    // Number of notes on the drumpad
    private static final int NUM_NOTES = 16;
    @Override
    public int getNumNotes() {
      return NUM_NOTES;
    }
    
    // 16 Different note sounds
    private final int[] AUDIO_FILES = {
      R.raw.drum_crash,
      R.raw.drum_splash,
      R.raw.drum_ride,
      R.raw.drum_hat_open,
      R.raw.drum_tom_hi,
      R.raw.drum_tom_mid,
      R.raw.drum_tom_lo,
      R.raw.drum_tom_floor,
      R.raw.drum_snare_rim,
      R.raw.drum_snare_hit,
      R.raw.drum_hat_half,
      R.raw.drum_hat_pedal,
      R.raw.drum_snare_side,
      R.raw.drum_kick_1,
      R.raw.drum_hat_closed,
      R.raw.drum_kick_2,
    };
    @Override
    protected int[] getAudioFiles() {
      return AUDIO_FILES;
    }
    
    // the midi pitch of each of the above files
    // this is a standard called General Midi:
    // https://usermanuals.finalemusic.com/SongWriter2012Win/Content/PercussionMaps.htm
    // though this is optimized for FL studio, which might deviate from the standard a little
    private final int[] MIDI_PITCHES = {
      // row
      49,
      55,
      51,
      48, // 46 in general midi
      // row
      60, // 50 in general midi
      45,
      43,
      41,
      // row
      40, // not sure if snare rim is defined in general midi
      38,
      46, // i don't think half hi-hat is defined in general midi
      44,
      //row
      37,
      36,
      42,
      35,
    };
    @Override
    public int[] getValidPitches() {
      return MIDI_PITCHES;
    }
    
    @Override
    public int pitchToAudioFile(int pitch) {
      for (int i = 0; i < NUM_NOTES; i++) {
        if (MIDI_PITCHES[i] == pitch) {
          return AUDIO_FILES[i];
        }
      }
      return AUDIO_RES_NONE;
    }
    
  };
  
  
}
