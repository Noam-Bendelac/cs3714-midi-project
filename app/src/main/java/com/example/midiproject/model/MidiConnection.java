package com.example.midiproject.model;

import android.content.Context;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiInputPort;
import android.media.midi.MidiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.midiproject.BuildConfig;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Singleton repository representing available MIDI devices and connecting to them
 * based on https://developer.android.com/reference/android/media/midi/package-summary
 */
public class MidiConnection {
  
  
  private static MidiConnection instance;
  
  public static MidiConnection getInstance(Context ctx) {
    if (instance == null) {
      instance = new MidiConnection(ctx);
    }
    return instance;
  }
  
  
  // livedata to hold list of connected device infos
  // mutable list. never null?
  // TODO initialize with empty list?
  private final MutableLiveData<List<MidiDeviceInfo>> mInfos = new MutableLiveData<>();
  
  
  // livedata to hold current device's info
  // currInfo.value may be null
  private final MutableLiveData<MidiDeviceInfo> mCurrInfo = new MutableLiveData<>(null);
  
  // livedata to hold current opened device. derived from currInfo. value may be null
  private final MutableLiveData<MidiDevice> mCurrDevice = new MutableLiveData<>(null);
  
  // livedata to hold opened port. derived from currDevice. value may be null
  private final MutableLiveData<MidiInputPort> mCurrInputPort = new MutableLiveData<>(null);
  
  
  
  /**
   * Livedata getters only needed for infos list and current device info
   */
  public LiveData<List<MidiDeviceInfo>> getInfos() {
    return mInfos;
  }
  
  public LiveData<MidiDeviceInfo> getCurrInfo() {
    return mCurrInfo;
  }
  
  
  /**
   * set currently chosen device
   */
  public void setCurrInfo(@Nullable MidiDeviceInfo info) {
    if (BuildConfig.DEBUG && info != null && !mInfos.getValue().contains(info)) {
      throw new AssertionError("Assertion failed");
    }
    mCurrInfo.setValue(info);
  }
  
  
  /**
   * sets up event listeners for plug-and-play and connecting to a device when currInfo is set
   * @param ctx only used to get midi service
   */
  private MidiConnection(Context ctx) {
    
    // getting application context from ctx might be a better practice
    MidiManager m = (MidiManager)ctx.getApplicationContext().getSystemService(Context.MIDI_SERVICE);
    
    // both of these list conversions are necessary to make a mutable list. sigh...
    mInfos.setValue(new ArrayList<>(Arrays.asList(m.getDevices())));
    
    
    // TODO what should it be initially?
    // mCurrInfo.setValue(mInfos.getValue().size() > 0 ? mInfos.getValue().get(0) : null);
    mCurrInfo.setValue(null);
    
    
    // derive device (open the device) from curr selected deviceInfo
    mCurrInfo.observeForever(midiDeviceInfo -> {
      // cleanup first
      if (mCurrDevice.getValue() != null) {
        try {
          mCurrDevice.getValue().close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        // must synchronously currDevice.setValue so no one writes to a closed device
        mCurrDevice.setValue(null);
      }
      if (midiDeviceInfo != null) {
        m.openDevice(midiDeviceInfo, newDevice -> {
          if (newDevice == null) {
            Log.e("MIDI", "could not open device " + midiDeviceInfo);
          }
          mCurrDevice.setValue(newDevice);
        }, new Handler(Looper.getMainLooper()));
      }
    });
    
    
    mCurrDevice.observeForever(midiDevice -> {
      // cleanup first
      if (mCurrInputPort.getValue() != null) {
        try {
          // is this ok if currDevice is already closed?
          mCurrInputPort.getValue().close();
        } catch (IOException e) {
          Log.e("open port", Arrays.toString(e.getStackTrace()));
        }
        // must synchronously currInputPort.setValue
        mCurrInputPort.setValue(null);
      }
      
      if (midiDevice != null) {
        mCurrInputPort.setValue(midiDevice.openInputPort(0));
      }
    });
    
    // plug-n-play when new device connected, and cleanup when device disconnected
    m.registerDeviceCallback(new MidiManager.DeviceCallback() {
      public void onDeviceAdded( MidiDeviceInfo newInfo ) {
        // new device available, notify observers
        mInfos.getValue().add(newInfo);
        mInfos.setValue(mInfos.getValue());
        // mCurrInfo.setValue(newInfo);
      }
      public void onDeviceRemoved( MidiDeviceInfo removedInfo ) {
        // device removed, notify observers
        mInfos.getValue().remove(removedInfo);
        mInfos.setValue(mInfos.getValue());
        
        if (mCurrInfo.getValue() == removedInfo) {
          // current device was removed!
          // TODO what do we replace it with?
          mCurrInfo.setValue(null);
          // TODO send event?
        }
      }
    }, new Handler(Looper.getMainLooper()));
    
  }
  
  
  /**
   * There are two ways to turn a note off: sending a NOTE_OFF message, or sending a NOTE_ON with
   * velocity 0. For some reason, the second method is preferred.
   */

  private static final byte NOTE_ON_STATUS = (byte) 0x90;
  // private static final byte NOTE_OFF_STATUS = (byte) 0x80;
  private static final byte NOTE_OFF_VELOCITY = (byte) 0;
  
  
  /**
   * based on https://developer.android.com/reference/android/media/midi/package-summary#send_a_noteon
   * 
   * @param pitch 0-127
   * @param velocity 0-127
   */
  public void sendNoteOn(int pitch, int velocity) {
    if (mCurrInputPort.getValue() != null) {
      byte[] buffer = new byte[4];
      int numBytes = 0;
      
      // hardcoded channel for now
      // MIDI channels 1-16 are encoded as 0-15.
      int channel = 1;
      buffer[numBytes++] = (byte)(NOTE_ON_STATUS + (channel - 1)); // note on
      buffer[numBytes++] = (byte)pitch;
      buffer[numBytes++] = (byte)velocity;
      
      int offset = 0;
      // post is non-blocking
      try {
        mCurrInputPort.getValue().send(buffer, offset, numBytes);
      } catch (IOException e) {
        Log.e("send midi", Arrays.toString(e.getStackTrace()));
      }
    
    }
  }
  
  public void sendNoteOff(int pitch) {
    sendNoteOn(pitch, NOTE_OFF_VELOCITY);
  }
  
  
}
