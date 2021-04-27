package com.example.midiproject.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.midiproject.R;
import com.example.midiproject.model.Instrument;
import com.example.midiproject.model.MidiConnection;
import com.example.midiproject.model.Note;

public class KeyboardFragment extends Fragment {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MidiConnection.getInstance(getContext()).setInstrument(Instrument.PIANO);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_keyboard, container, false);
        
        
        setupUI(root);
        
        return root;
    }
    
    @SuppressLint("ClickableViewAccessibility")
    private void setupUI(View root) {
        LinearLayout layout = (LinearLayout) root.findViewById(R.id.linearLayout);
        int[] pitches = Instrument.PIANO.getValidPitches();
        layout.setOrientation(LinearLayout.HORIZONTAL);
        
        
        for (int pitch : pitches) {
            Button btnTag = new Button(requireContext());
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            btnTag.setLayoutParams(p);
            
            btnTag.setText(Note.pitchToString(pitch));
            
            btnTag.setOnTouchListener(new KeyTouchListener(pitch, btnTag));
            
            layout.addView(btnTag);
        }
        
        
    }
}
