package com.example.midiproject.model;

import com.example.midiproject.R;

/**
 * Represents one of our 2 instruments, piano and drums. Handles getting audio file from midi pitch
 */
public abstract class Instrument {
  
  /**
   *
   * @param pitch midi pitch 0-127
   * @return audio file resource id, or AUDIO_RES_NONE
   */
  public abstract int pitchToAudioFile(int pitch);
  
  
  /**
   * 
   * @return array of valid midi pitches that SHOULD NOT BE MODIFIED (if only java had a feature to
   * enforce that...)
   */
  public abstract int[] getValidPitches();
  
  
  
  
  
  // represents audio file that's not available
  public static final int AUDIO_RES_NONE = -1;
  
  
  // static instruments
  
  public static Instrument PIANO = new Instrument() {
  
    // Number of notes on the keyboard. TODO this might change
    private static final int NUM_NOTES = 8;
    
    // pitch of first note
    private static final int START_PITCH = 60;
  
    // 16 Different note sounds
    private final int[] AUDIO_FILES = {
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c
    };
    
    @Override
    public int pitchToAudioFile(int pitch) {
      int noteNum = pitch - START_PITCH;
      if (noteNum < 0 || noteNum >= NUM_NOTES) {
        return AUDIO_RES_NONE;
      }
      return AUDIO_FILES[noteNum];
    }
  
    @Override
    public int[] getValidPitches() {
      int[] ret = new int[NUM_NOTES];
      for (int i = 0; i < NUM_NOTES; i++) {
        ret[i] = START_PITCH + i;
      }
      return ret;
    }
  };
  
  public static Instrument DRUMS = new Instrument() {
  
    // Number of notes on the drumpad
    private static final int NUM_NOTES = 16;
    
    // 16 Different note sounds
    private final int[] AUDIO_FILES = {
      // TODO replace these with actual drum sounds
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
      R.raw.piano_c,
    };
    
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
    public int pitchToAudioFile(int pitch) {
      for (int i = 0; i < NUM_NOTES; i++) {
        if (MIDI_PITCHES[i] == pitch) {
          return AUDIO_FILES[i];
        }
      }
      return AUDIO_RES_NONE;
    }
  
    @Override
    public int[] getValidPitches() {
      return MIDI_PITCHES;
    }
  };
  
  
}
