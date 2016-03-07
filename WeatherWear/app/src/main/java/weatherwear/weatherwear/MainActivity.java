package weatherwear.weatherwear;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import weatherwear.weatherwear.alarm.AlarmAlertManager;
import weatherwear.weatherwear.alarm.AlarmFragment;
import weatherwear.weatherwear.alarm.AlarmScheduler;
import weatherwear.weatherwear.vacation.VacationFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment mFragment;
    private AlarmAlertManager mAAManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, new OutfitFragment())
                .commit();

        drawer.closeDrawer(GravityCompat.START);

        AlarmScheduler.setSchedule(this);
        mAAManager = new AlarmAlertManager();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Check if launching from alarm, and start NewOutfitActivity
        if (mAAManager.isPlaying()) {
            Intent intent = new Intent(this, NewOutfitActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if(mAAManager.isPlaying()){
            mAAManager.stopAlerts();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mAAManager.isPlaying()){
            mAAManager.stopAlerts();
        }
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(mAAManager.isPlaying()){
            mAAManager.stopAlerts();
        }
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.todays_outfit) {
            mFragment = new OutfitFragment();
        } else if (id == R.id.my_closet) {
            mFragment = new ClosetFragment();
        } else if (id == R.id.set_alarm) {
            mFragment = new AlarmFragment();
        } else if (id == R.id.user_preferences) {
            mFragment = new PreferenceFragment();
        } else if(id == R.id.my_vacations) {
            mFragment = new VacationFragment();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, mFragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
