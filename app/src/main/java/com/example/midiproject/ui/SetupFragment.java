package com.example.midiproject.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

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
  
  @SuppressLint("ClickableViewAccessibility")
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View root = inflater.inflate(R.layout.fragment_setup, container, false);
    
    // TODO we may or may not need a ViewModel for persistence
  
    DrawerLayout drawer = root.findViewById(R.id.drawerLayout);
    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
  
    // slightly darker color for left side
    drawer.setScrimColor(Color.argb(10, 0, 0, 0));
    
    
    // RadioGroup radioGroup = root.findViewById(R.id.radioGroup);
    RadioButton radioButtonExternal = root.findViewById(R.id.radioButtonExternal);
    RadioButton radioButtonInternal = root.findViewById(R.id.radioButtonInternal);
  
    
    MidiConnection connection = MidiConnection.getInstance(getContext());
    
    connection.getExternalAvailable().observe(getViewLifecycleOwner(), externalAvailable -> {
      radioButtonExternal.setEnabled(externalAvailable);
    });
    
    // radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
    //   switch (checkedId) {
    //     case R.id.radioButtonInternal:
    //       connection.setDeviceSelection(MidiConnection.DeviceSelection.INTERNAL);
    //       break;
    //     case R.id.radioButtonExternal:
    //       connection.setDeviceSelection(MidiConnection.DeviceSelection.EXTERNAL);
    //       break;
    //   }
    // });
    radioButtonInternal.setOnClickListener(v -> connection.setDeviceSelection(MidiConnection.DeviceSelection.INTERNAL));
    radioButtonExternal.setOnClickListener(v -> connection.setDeviceSelection(MidiConnection.DeviceSelection.EXTERNAL));
    connection.getDeviceSelection().observe(getViewLifecycleOwner(), deviceSelection -> {
      RadioButton selected = deviceSelection == MidiConnection.DeviceSelection.INTERNAL
        ? radioButtonInternal
        : radioButtonExternal;
      selected.setChecked(true);
      // Toast.makeText(getContext(), "selection changed: "+deviceSelection.toString(), Toast.LENGTH_SHORT).show();
    });
    
    
    Button buttonTestNote = root.findViewById(R.id.buttonTestNote);
    buttonTestNote.setOnTouchListener((v, event) -> {
      int actionMasked = event.getActionMasked();
      switch (actionMasked) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_POINTER_DOWN:
          midiConnection.sendNoteOn(60, 127);
          buttonTestNote.setPressed(true);
          break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
        case MotionEvent.ACTION_CANCEL:
          midiConnection.sendNoteOff(60);
          buttonTestNote.setPressed(false);
          break;
      }
      return true;
    });
    
    
    return root;
  }
}