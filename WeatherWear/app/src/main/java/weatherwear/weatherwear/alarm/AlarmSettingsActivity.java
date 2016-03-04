package weatherwear.weatherwear.alarm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import at.markushi.ui.CircleButton;
import weatherwear.weatherwear.R;

public class AlarmSettingsActivity extends AppCompatActivity {
    private AlarmModel mAlarmModel;
    private AlarmDatabaseHelper mDbHelper;
    private boolean mFromHistory;
    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Instantiate view, and update the toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_settings_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.text_SetAlarm);
        setSupportActionBar(toolbar);
        // Instantiate the database helper
        mDbHelper = new AlarmDatabaseHelper(this);
        // Check to see if loading an existing alarm, or creating a new one
        Bundle extras = getIntent().getExtras();
        mFromHistory = (extras != null);

        if (mFromHistory) { // Loading existing alarm
            // Get the id (for handling deletions/loading alarm from DB))
            mId = extras.getLong(AlarmFragment.ID_KEY);
            // Load the alarm model from the ID, and update all UI elements
            mAlarmModel = mDbHelper.fetchEntryByIndex(mId);
            // Update all toggle buttons
            setPressed(mAlarmModel.getSun(), R.id.sundayButton);
            setPressed(mAlarmModel.getMon(), R.id.mondayButton);
            setPressed(mAlarmModel.getTues(), R.id.tuesdayButton);
            setPressed(mAlarmModel.getWed(), R.id.wednesdayButton);
            setPressed(mAlarmModel.getThurs(), R.id.thursdayButton);
            setPressed(mAlarmModel.getFri(), R.id.fridayButton);
            setPressed(mAlarmModel.getSat(), R.id.saturdayButton);
            // Update the TimePicker
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(mAlarmModel.getTimeInMillis());
            TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_timePicker);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY)); // deprecated calls to work with API 22
            timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        } else { // Creating new alarm
            mAlarmModel = new AlarmModel();
        }
    }

    // Adds the delete option to the top menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);
        return true;
    }

    // Menu item handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_button: // If you click delete, either remove it (if existing), or alert cancellation
                if(mFromHistory) {
                    mDbHelper.removeEntry(mId);
                    Toast.makeText(getApplicationContext(), R.string.alarm_settings_deleted_alarm_message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.alarm_settings_discarded_alarm_message, Toast.LENGTH_SHORT).show();
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Toggles "pressed" status on 'Day of the Week' buttons
    private void setPressed(boolean pressed, int id) {
        at.markushi.ui.CircleButton button = (CircleButton) findViewById(id);
        if (pressed) {
            button.setTranslationY(-20);
        } else {
            button.setTranslationY(0);
        }
    }

    // Handles cancel button click
    public void onCancel(View view) {
        // Customized cancellation text depending on if loaded or new alarm
        if (mFromHistory)
            Toast.makeText(getApplicationContext(), R.string.alarm_settings_discarded_changes_message, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), R.string.alarm_settings_discarded_alarm_message, Toast.LENGTH_SHORT).show();
        finish();
    }

    // Handles save button click
    public void onSave(View view) {
        // Checks to ensure that the alarm is set for at least one day of the week
        if(mAlarmModel.isDayChosen()) {
            // Automatically turn alarm on
            mAlarmModel.setIsOn(true);
            // Updates the alarm model with the current time, and updates it
            TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_timePicker);
            mAlarmModel.setTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
            new InsertData().execute(mAlarmModel);
        } else {
            Toast.makeText(getApplicationContext(), R.string.alarm_settings_no_day_message, Toast.LENGTH_SHORT).show();
        }
    }

    // Handles 'Day of the Week' button clicks
    //  - Updates model and calls 'setPressed' to update UI
    public void onDayClick(View view) {
        int id = view.getId();
        switch (id) {
            case (R.id.sundayButton):
                mAlarmModel.changeSun();
                setPressed(mAlarmModel.getSun(), R.id.sundayButton);
                break;
            case (R.id.mondayButton):
                mAlarmModel.changeMon();
                setPressed(mAlarmModel.getMon(), R.id.mondayButton);
                break;
            case (R.id.tuesdayButton):
                mAlarmModel.changeTues();
                setPressed(mAlarmModel.getTues(), R.id.tuesdayButton);
                break;
            case (R.id.wednesdayButton):
                mAlarmModel.changeWed();
                setPressed(mAlarmModel.getWed(), R.id.wednesdayButton);
                break;
            case (R.id.thursdayButton):
                mAlarmModel.changeThurs();
                setPressed(mAlarmModel.getThurs(), R.id.thursdayButton);
                break;
            case (R.id.fridayButton):
                mAlarmModel.changeFri();
                setPressed(mAlarmModel.getFri(), R.id.fridayButton);
                break;
            case (R.id.saturdayButton):
                mAlarmModel.changeSat();
                setPressed(mAlarmModel.getSat(), R.id.saturdayButton);
                break;
            default:
                break;
        }

    }

    // Inserts into database
    private class InsertData extends AsyncTask<AlarmModel, Void, Void> {
        @Override
        protected Void doInBackground(AlarmModel... args) {
            // Calls update or insertion depending if loaded or new alarm
            if(mFromHistory){
                mDbHelper.onUpdate(args[0]);
            } else {
                mDbHelper.insertAlarm(args[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Feedback to user upon saving
            Toast.makeText(getApplicationContext(), R.string.alarm_settings_alarm_saved_message, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
