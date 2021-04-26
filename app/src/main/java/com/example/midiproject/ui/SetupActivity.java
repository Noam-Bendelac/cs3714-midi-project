package com.example.midiproject.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;

import com.example.midiproject.R;

public class SetupActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setup);
  
    DrawerLayout drawer = findViewById(R.id.drawerLayout);
    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    
    // slightly darker color for left side
    drawer.setScrimColor(Color.argb(10, 0, 0, 0));
    
  }
  
  
  
  
}