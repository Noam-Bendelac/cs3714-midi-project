package com.example.midiproject.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import com.example.midiproject.R;
import com.example.midiproject.model.Instrument;
import com.example.midiproject.model.MidiConnection;
import com.example.midiproject.model.Note;

import java.util.Arrays;
import java.util.List;

/**
 * A fragment that holds the buttons for the drumpad activity
 */
public class DrumpadFragment extends Fragment {
    
    private MidiConnection mMidiConnection;
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMidiConnection = MidiConnection.getInstance(getContext());
        mMidiConnection.setInstrument(Instrument.DRUMS);
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
    
    
    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("SetTextI18n")
    private void setupUI(View root) {
        // apply note pitches to buttons
    
        GridLayout gridLayout = root.findViewById(R.id.gridLayout);
    
        int[] pitches = Instrument.DRUMS.getValidPitches();
    
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
        
            // create a button with the proper callbacks (onTouch)
            gridLayout.addView(new AppCompatButton(ctx) {{
                // this is for making the button fill the grid cells
                // source: https://stackoverflow.com/a/57315186/
                setLayoutParams(new GridLayout.LayoutParams(
                  GridLayout.spec(row, 1F),
                  GridLayout.spec(col, 1F)
                ));
            
                setText(Note.pitchToString(pitch));
                setGravity(Gravity.END | Gravity.BOTTOM);
            
                // TODO move the listener to some other class for reuse
                setOnTouchListener((v, event) -> {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN:
                            mMidiConnection.sendNoteOn(pitch, 127);
                            setPressed(true);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:
                        case MotionEvent.ACTION_CANCEL:
                            mMidiConnection.sendNoteOff(pitch);
                            setPressed(false);
                            break;
                    }
                    return true;
                });
            }});
        }
    }
}