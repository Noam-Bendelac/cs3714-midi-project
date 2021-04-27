package com.example.midiproject.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.midiproject.R;
import com.example.midiproject.model.Instrument;
import com.example.midiproject.model.MidiConnection;
import com.example.midiproject.model.Note;

/**
 * A fragment that holds the buttons for the drumpad activity
 */
public class DrumpadFragment extends Fragment {
    
    // names of the drum pads
    private static final String[] DRUM_NAMES = {
        "Crash",
        "Splash",
        "Ride",
        "Open\nHat",
        "Hi Tom",
        "Mid Tom",
        "Lo Tom",
        "Floor\nTom",
        "Snare\nRim",
        "Snare\nHit",
        "Half\nHat",
        "Pedal\nHat",
        "Snare\nSide",
        "Kick 1",
        "Closed\nHat",
        "Kick 2",
    };
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MidiConnection.getInstance(getContext()).setInstrument(Instrument.DRUMS);
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_drumpad, container, false);
    
    
        setupUI(root);
        
        
        // uncomment to attempt to connect to external, just for testing. TODO remove
        // new Handler(Looper.getMainLooper()).postDelayed(() -> {
        //     mMidiConnection.setDeviceSelection(MidiConnection.DeviceSelection.EXTERNAL);
        // }, 2000);
        
        return root;
    }
    
    
    @SuppressWarnings("SetTextI18n")
    private void setupUI(View root) {
        // apply note pitches to buttons
        
        int[] pitches = Instrument.DRUMS.getValidPitches();
    
        GridLayout gridLayout = root.findViewById(R.id.gridLayout);
        // remove all buttons first, in case there already are some from a previous update
        gridLayout.removeAllViews();
        // there should always be 16 notes, but just in case:
        gridLayout.setColumnCount((int) Math.ceil(Math.sqrt(pitches.length)));
    
        Context ctx = requireContext();
    
        for (int idx = 0; idx < pitches.length; idx++) {
            // keep track of row and column within the grid
            int row = idx / gridLayout.getColumnCount();
            int col = idx % gridLayout.getColumnCount();
        
            int pitch = pitches[idx];
            
            String name = DRUM_NAMES[idx];
        
            // create a button with the proper callbacks (onTouch)
            gridLayout.addView(new AppCompatButton(ctx) {{
                // double brace initialization means we are extending AppCompatButton
                
                // this is for making the button fill the grid cells
                // source: https://stackoverflow.com/a/57315186/
                setLayoutParams(new GridLayout.LayoutParams(
                  GridLayout.spec(row, 1F),
                  GridLayout.spec(col, 1F)
                ));
            
                // uncomment to set text to note name like "C#4"
                // setText(Note.pitchToString(pitch));
                setText(name);
                
                setAllCaps(false);
                setLineSpacing(0, 0.9f);
                setPadding(24, 24, 24, 20);
                setGravity(Gravity.END | Gravity.BOTTOM);
            
                // `this` is a reference to the button, to set the "pressed" style
                setOnTouchListener(new KeyTouchListener(pitch, this));
            }});
        }
    }
}