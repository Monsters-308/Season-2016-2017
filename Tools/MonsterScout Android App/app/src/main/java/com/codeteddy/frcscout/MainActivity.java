package com.codeteddy.frcscout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codeteddy.frcscout.fragments.CommentFragment;
import com.codeteddy.frcscout.fragments.HistoryFragment;
import com.codeteddy.frcscout.fragments.MainFragment;
import com.codeteddy.frcscout.fragments.PitFragment;
import com.codeteddy.frcscout.fragments.ScoutFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private View headerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Read sharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean enabled = sharedPreferences.getBoolean("enabled", false);
        if(!enabled){
            startActivity(new Intent(getApplicationContext(), IntroActivity.class));
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Use it
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);


        TextView name = (TextView) headerView.findViewById(R.id.nav_name);

        name.setText(sharedPreferences.getString("user_name", "Alexander Kaschta"));

        int index = sharedPreferences.getInt("last_fragment_state", 0);
        setFragment(index);
        //TODO Fix bug #1, that index in navigation drawer doesn't get set during on create
        setNavigationDrawerIndex(index);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            setFragment(0);

        } else if (id == R.id.nav_scout) {
            setFragment(1);

        } else if (id == R.id.nav_pit) {
            setFragment(2);
        } else if (id == R.id.nav_comment) {
            setFragment(3);

        } else if(id == R.id.nav_history){
            setFragment(4);
        }
        else if(id == R.id.nav_about){
            new MaterialDialog.Builder(this)
                    .customView(R.layout.dialog_about, false)
                    .positiveText("Okay")
                    .show();
        }
        else if (id == R.id.nav_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setTitle(String message) {
        getSupportActionBar().setTitle(message);
    }

    public void setNavigationDrawerIndex(int number){
        navigationView.setCheckedItem(number);
    }


    public void setFragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case 0:
                fragment = new MainFragment();
                setTitle("Home");
                setNavigationDrawerIndex(R.id.nav_home);
                break;
            case 1:
                fragment = new ScoutFragment();
                setTitle("Match Scouting");
                setNavigationDrawerIndex(R.id.nav_scout);
                break;
            case 2:
                fragment = new PitFragment();
                setTitle("Pit Scouting");
                setNavigationDrawerIndex(R.id.nav_pit);
                break;
            case 3:
                fragment = new CommentFragment();
                setTitle("Comment");
                setNavigationDrawerIndex(R.id.nav_comment);
                break;
            case 4:
                fragment = new HistoryFragment();
                setTitle("History");
                setNavigationDrawerIndex(R.id.nav_history);
                break;
            default:
                System.out.println("I'm doing nothing");
        }
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.frame, fragment).setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).commitAllowingStateLoss();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            System.out.println("Saved");
            editor.putInt("last_fragment_state", id);
            editor.apply();
        }
    }
}
