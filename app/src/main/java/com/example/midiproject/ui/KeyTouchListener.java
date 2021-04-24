package com.example.midiproject.ui;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.midiproject.model.MidiConnection;

import org.jetbrains.annotations.NotNull;

public class KeyTouchListener implements View.OnTouchListener {
  
  private final int mPitch;
  private final @NotNull Button mButton;
  
  public KeyTouchListener(int pitch, @NotNull Button button) {
    mPitch = pitch;
    mButton = button;
  }
  
  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouch(View v, MotionEvent event) {
    MidiConnection connection = MidiConnection.getInstance(v.getContext());
    
    switch (event.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_POINTER_DOWN:
        connection.sendNoteOn(mPitch, 127);
        mButton.setPressed(true);
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_POINTER_UP:
      case MotionEvent.ACTION_CANCEL:
        connection.sendNoteOff(mPitch);
        mButton.setPressed(false);
        break;
    }
    return true;
  }
}
