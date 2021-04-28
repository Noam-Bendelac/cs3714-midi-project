package com.example.midiproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.midiproject.ui.DrumpadFragment;
import com.example.midiproject.ui.KeyboardFragment;
import com.example.midiproject.ui.SetupFragment;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {
  private DrawerLayout mDrawer;
  private Toolbar mToolbar;
  private NavigationView nvDrawer;
  
  // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
  private ActionBarDrawerToggle drawerToggle;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    // Set a Toolbar to replace the ActionBar.
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);
    
    // This will display an Up icon (<-), we will replace it with hamburger later
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    
    // Find our drawer view
    mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawerToggle = setupDrawerToggle();
    drawerToggle.setDrawerIndicatorEnabled(true);
    drawerToggle.syncState();
    mDrawer.addDrawerListener(drawerToggle);


    // Find our drawer view
    nvDrawer = (NavigationView) findViewById(R.id.nvView);
    // Setup drawer view
    setupDrawerContent(nvDrawer);
  }

  private ActionBarDrawerToggle setupDrawerToggle() {
    // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
    // and will not render the hamburger icon without it.
    return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open,  R.string.drawer_close);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    drawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    // Pass any configuration change to the drawer toggles
    drawerToggle.onConfigurationChanged(newConfig);
  }
  
  private void setupDrawerContent(NavigationView navigationView) {
    navigationView.setNavigationItemSelectedListener(
      menuItem -> {
        selectDrawerItem(menuItem);
        return true;
      });
  }
  
  
  public void selectDrawerItem(MenuItem menuItem) {
    // Create a new fragment and specify the fragment to show based on nav item clicked
    Fragment fragment = null;
    Class fragmentClass;
    switch(menuItem.getItemId()) {
      case R.id.nav_keyboard:
        fragmentClass = KeyboardFragment.class;
        break;
      case R.id.nav_drumpad:
        fragmentClass = DrumpadFragment.class;
        break;
      case R.id.nav_setup:
        fragmentClass = SetupFragment.class;
        break;
      default:
        fragmentClass = KeyboardFragment.class;
    }
    
    try {
      fragment = (Fragment) fragmentClass.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    // Insert the fragment by replacing any existing fragment
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    
    // Highlight the selected item has been done by NavigationView
    menuItem.setChecked(true);
    // Set action bar title
    setTitle(menuItem.getTitle());
    // Close the navigation drawer
    mDrawer.closeDrawers();
  }
  
  
  
  /**
   * this overrides the "back"/"up" button to instead open the nav drawer
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // The action bar home/up action should open or close the drawer.
    switch (item.getItemId()) {
      case android.R.id.home:
        mDrawer.openDrawer(GravityCompat.START);
        return true;
    }
    
    return super.onOptionsItemSelected(item);
  }


}