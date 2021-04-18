package com.example.midiproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiInputPort;
import android.media.midi.MidiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  
  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  
  
    MidiManager m = (MidiManager)getSystemService(Context.MIDI_SERVICE);
    
    // both of these list conversions are necessary. sigh...
    // mutable list. never reassigned. never null
    final List<MidiDeviceInfo> infos = new ArrayList<>(Arrays.asList(m.getDevices()));
  
    // livedata to hold current device's info. for now, this gets set to newest plugged in device
    // info.value may be null
    final MutableLiveData<MidiDeviceInfo> info = new MutableLiveData<>(infos.size() > 0 ? infos.get(0) : null);
    
    // livedata to hold current opened device. derived from info. value may be null
    final MutableLiveData<MidiDevice> device = new MutableLiveData<>(null);
    info.observeForever(midiDeviceInfo -> {
      // cleanup first? move into openDevice callback?
      if (device.getValue() != null) {
        try {
          device.getValue().close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (midiDeviceInfo == null) {
        device.setValue(null);
        return;
      }
      m.openDevice(midiDeviceInfo, newDevice -> {
        if (newDevice == null) {
          Log.e("MIDI", "could not open device " + midiDeviceInfo);
        } else {
          device.setValue(newDevice);
        }
      }, new Handler(Looper.getMainLooper()));
    });
    
    // livedata to hold opened port. 
    MutableLiveData<MidiInputPort> deviceInputPort = new MutableLiveData<>(null);
    device.observeForever(midiDevice -> {
      // cleanup first
      if (deviceInputPort.getValue() != null) {
        try {
          deviceInputPort.getValue().close();
        } catch (IOException e) {
          Log.e("open port", Arrays.toString(e.getStackTrace()));
        }
      }
      
      if (midiDevice == null) {
        deviceInputPort.setValue(null);
        return;
      }
      deviceInputPort.setValue(midiDevice.openInputPort(0));
    });
    
    // plug-n-play when new device connected, and cleanup when device disconnected
    m.registerDeviceCallback(new MidiManager.DeviceCallback() {
      public void onDeviceAdded( MidiDeviceInfo newInfo ) {
        infos.add(newInfo);
        info.setValue(newInfo);
        // TextView tv = findViewById(R.id.myText);
        // tv.setText(newInfo.toString());
      }
      public void onDeviceRemoved( MidiDeviceInfo removedInfo ) {
        infos.remove(removedInfo);
        if (info.getValue() == removedInfo) {
          if (infos.size() != 0) {
            info.setValue(infos.get(0));
          } else {
            info.setValue(null); // will set device.value to null
          }
        }
      }
    }, new Handler(Looper.getMainLooper()));
    
    
    // spaghetti code that sends a note of duration 500ms every 1000ms
    Handler noteHandler = new Handler(Looper.getMainLooper());
    noteHandler.postDelayed(new Runnable() {
      @Override
      public void run() {
        // note on
        if (deviceInputPort.getValue() != null) {
          byte[] buffer = new byte[32];
          int numBytes = 0;
          int channel = 1; // MIDI channels 1-16 are encoded as 0-15.
          buffer[numBytes++] = (byte)(0x90 + (channel - 1)); // note on
          buffer[numBytes++] = (byte)60; // pitch is middle C
          buffer[numBytes++] = (byte)127; // max velocity
          int offset = 0;
          // post is non-blocking
          try {
            deviceInputPort.getValue().send(buffer, offset, numBytes);
          } catch (IOException e) {
            Log.e("send midi", Arrays.toString(e.getStackTrace()));
          }
          
        }
        
        // note off 500 ms later
        noteHandler.postDelayed(() -> {
          if (deviceInputPort.getValue() != null) {
            byte[] buffer = new byte[32];
            int numBytes = 0;
            int channel = 1; // MIDI channels 1-16 are encoded as 0-15.
            buffer[numBytes++] = (byte)(0x90 + (channel - 1)); // note on
            buffer[numBytes++] = (byte)60; // pitch is middle C
            buffer[numBytes++] = (byte)0; // velocity of 0 == note off
            int offset = 0;
            // post is non-blocking
            try {
              deviceInputPort.getValue().send(buffer, offset, numBytes);
            } catch (IOException e) {
              Log.e("send midi", Arrays.toString(e.getStackTrace()));
            }
    
          }
        }, 500);
        
        
        // whole loop 1000ms later
        noteHandler.postDelayed(this, 1000);
      }
    }, 1000);
  
  }
}