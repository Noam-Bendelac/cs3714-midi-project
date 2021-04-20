package com.example.midiproject.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.midiproject.MainActivity;
import com.example.midiproject.R;
import com.example.midiproject.model.MidiConnection;

/**
 * A fragment for the controls drawer of the Setup Activity
 */
public class SetupFragment extends Fragment {
  
  
  MidiConnection midiConnection;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    midiConnection = MidiConnection.getInstance(getContext());
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View root = inflater.inflate(R.layout.fragment_setup, container, false);
    
    // TODO we may or may not need a ViewModel for persistence
    
    Button buttonTestNote = root.findViewById(R.id.buttonTestNote);
    buttonTestNote.setOnClickListener(v -> {
      midiConnection.sendNoteOn(60, 127);
      new Handler(Looper.getMainLooper()).postDelayed(() -> {
        midiConnection.sendNoteOff(60);
      }, 500);
    });
    
    
    return root;
  }
}