package com.example.midiproject.model;


/**
 * handles logic related to midi notes and musical pitch
 */
public class Note {
    
    private static String[] toneToString = {
        "C",
        "C#",
        "D",
        "D#",
        "E",
        "F",
        "F#",
        "G",
        "G#",
        "A",
        "A#",
        "B",
    };
    
    /**
     * source: https://www.inspiredacoustics.com/en/MIDI_note_numbers_and_center_frequencies
     * @param pitch midi pitch 0-127
     * @return string in the format "C4" or "C#4"
     */
    public static String pitchToString(int pitch) {
        int tone = pitch % 12;
        int octave = (pitch / 12) - 1;
        
        return toneToString[tone] + Integer.toString(octave);
    }
    
}
