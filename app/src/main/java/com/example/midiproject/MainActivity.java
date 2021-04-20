package com.example.midiproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.media.midi.MidiDeviceInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.example.midiproject.model.MidiConnection;

import java.util.List;

public class MainActivity extends AppCompatActivity {
  
  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  
  
    // connect to whatever device is available
    MidiConnection connection = MidiConnection.getInstance(this);
    LiveData<List<MidiDeviceInfo>> infos = connection.getInfos();
    infos.observe(this, midiDeviceInfos -> {
      connection.setCurrInfo(midiDeviceInfos.size() > 0 ? midiDeviceInfos.get(0) : null);
    });
    LiveData<MidiDeviceInfo> currInfo = connection.getCurrInfo();
    currInfo.observe(this, midiDeviceInfo -> {
      if (midiDeviceInfo != null) {
        // ((TextView)findViewById(R.id.myText)).setText(Integer.toString(midiDeviceInfo.getId()));
        ((TextView)findViewById(R.id.myText)).setText(midiDeviceInfo.toString());
      } else {
        ((TextView)findViewById(R.id.myText)).setText("");
      }
    });
  
  
    // send a note of duration 500ms every 1000ms
    Handler noteHandler = new Handler(Looper.getMainLooper());
    noteHandler.postDelayed(new Runnable() {
      @Override
      public void run() {
        // note on
        MidiConnection.getInstance(MainActivity.this).sendNoteOn(60, 127);
        
        // note off 500 ms later
        noteHandler.postDelayed(() -> {
          MidiConnection.getInstance(MainActivity.this).sendNoteOff(60);
        }, 500);
        
        
        // whole loop 1000ms later
        noteHandler.postDelayed(this, 1000);
      }
    }, 1000);
  
  }
}