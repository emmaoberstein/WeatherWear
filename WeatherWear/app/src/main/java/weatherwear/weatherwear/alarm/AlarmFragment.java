package weatherwear.weatherwear.alarm;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import weatherwear.weatherwear.R;
import weatherwear.weatherwear.Utils;

/**
 * Created by Emily on 2/27/16.
 * Displays alarms
 */
public class AlarmFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<AlarmModel>> {
    private static final int ADD_ID = 0;
    private static final int LOADER_ID = 1;
    public static final String ID_KEY = "id";

    private static AlarmDatabaseHelper mDbHelper;

    private static ArrayAdapter<AlarmModel> mAlarmAdapter;
    private static ArrayList<AlarmModel> mAlarmList = new ArrayList<AlarmModel>();
    private static Context mContext;

    public static LoaderManager loaderManager;
    public static int onCreateCheck = 0;

    @Override
    public void onResume() {
        super.onResume();
        // Re-query in case the data base has changed
        requeryAlarms(mContext);
        // Update all alarms correspondingly
        AlarmScheduler.setSchedule(getActivity());
    }

    // Re-queries all alarms, optionally forces LoaderManager to reload
    private void requeryAlarms(Context mContext) {
        if (mDbHelper == null) {
            mDbHelper = new AlarmDatabaseHelper(mContext);
            mAlarmAdapter = new AlarmEntriesAdapter(mContext);
            loaderManager = getActivity().getLoaderManager();
            setListAdapter(mAlarmAdapter);
        }
        if (onCreateCheck == 1) {
            onCreateCheck = 0;
        } else {
            loaderManager.initLoader(LOADER_ID, null, this).forceLoad();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the view, and add the title/menu
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.my_alarms);
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        // Open data base for operations.
        mDbHelper = new AlarmDatabaseHelper(mContext);
        loaderManager = getActivity().getLoaderManager();
        // Instantiate our customized array adapter
        mAlarmAdapter = new AlarmEntriesAdapter(mContext);
        // Set the adapter to the listview
        setListAdapter(mAlarmAdapter);
        loaderManager.initLoader(LOADER_ID, null, this).forceLoad();
        onCreateCheck = 1;
        setHasOptionsMenu(true);
        return view;
    }

    // Opens AlarmSettingsActivity to modify alarm
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Cancels alarms if it's playing
        AlarmAlertManager aAManager = new AlarmAlertManager();
        if (aAManager.isPlaying()) {
            aAManager.stopAlerts();
        }
        // On click of alarm, open AlarmSettingsActivity to modify it
        // Transfer the id of the alarm
        Intent intent = new Intent(mContext, AlarmSettingsActivity.class);
        Bundle extras = new Bundle();
        AlarmModel alarmModel = mAlarmAdapter.getItem(position);
        extras.putLong(ID_KEY, alarmModel.getId());
        intent.putExtras(extras);

        startActivity(intent);
    }

    // Create the options menu with the add button
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuitem;
        menuitem = menu.add(Menu.NONE, ADD_ID, ADD_ID, getString(R.string.plus_btn));
        menuitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    // Handle pressing the 'New' button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ADD_ID:
                startActivity(new Intent(getActivity(), AlarmSettingsActivity.class));
                return true;
            default:
                return false;
        }
    }

    // Loader handling
    @Override
    public Loader<ArrayList<AlarmModel>> onCreateLoader(int id, Bundle args) {
        return new AlarmLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<AlarmModel>> loader, ArrayList<AlarmModel> data) {
        mAlarmList = data;
        mAlarmAdapter.clear();
        mAlarmAdapter.addAll(mAlarmList);
        mAlarmAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<AlarmModel>> loader) {
        mAlarmAdapter.clear();
        mAlarmAdapter.addAll(mAlarmList);
        mAlarmAdapter.notifyDataSetChanged();
    }

    // Subclass of ArrayAdapter to display interpreted database row values in customized list view
    private class AlarmEntriesAdapter extends ArrayAdapter<AlarmModel> {

        public AlarmEntriesAdapter(Context context) {
            super(context, R.layout.alarm_list_layout);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Start the inflater
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View listItemView = convertView;
            // Optionally inflate if null
            if (listItemView == null) {
                listItemView = inflater.inflate(R.layout.alarm_list_layout, parent, false);
            }
            // Get all textviews
            TextView titleView = (TextView) listItemView.findViewById(R.id.titleText);
            TextView subtitleView = (TextView) listItemView.findViewById(R.id.subtitle);
            Switch switchState = (Switch) listItemView.findViewById(R.id.switchState);
            TextView switchView = (TextView) listItemView.findViewById(R.id.switchText);

            final AlarmModel alarm = getItem(position);

            // Parse data to readable format
            String time = Utils.parseTime(alarm.getTimeInMillis());

            // Set text on the view.
            titleView.setText(time);
            subtitleView.setText(alarm.weeklyInfo());

            // Handle alarm changes (remove OnCheckedChangeListener to prevent toggling all alarms)
            if (alarm.getIsOn()) {
                switchView.setText(R.string.alarm_fragment_on_text);
                switchState.setOnCheckedChangeListener(null);
                switchState.setChecked(true);
            } else {
                switchView.setText(R.string.alarm_fragment_off_text);
                switchState.setOnCheckedChangeListener(null);
                switchState.setChecked(false);
            }

            // Handle switch toggling
            switchState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Turn off current alarms
                    AlarmAlertManager aAManager = new AlarmAlertManager();
                    if (aAManager.isPlaying()) {
                        aAManager.stopAlerts();
                    }
                    // Update alarm status
                    if (isChecked) {
                        alarm.setIsOn(true);

                    } else {
                        alarm.setIsOn(false);
                    }
                    // Update the alarm, schedule accordingly, and refresh the list
                    mDbHelper.updateAlarm(alarm);
                    AlarmScheduler.setSchedule(getActivity());
                    mAlarmAdapter.notifyDataSetChanged();
                }
            });

            return listItemView;
        }
    }

    // AsyncTaskLoader to load in all alarms from DB
    private static class AlarmLoader extends AsyncTaskLoader<ArrayList<AlarmModel>> {
        public Context mContext;
        public AlarmLoader(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public ArrayList<AlarmModel> loadInBackground() {
            mDbHelper = new AlarmDatabaseHelper(mContext);
            mAlarmList = mDbHelper.fetchAlarms();
            return mAlarmList;
        }
    }

}
