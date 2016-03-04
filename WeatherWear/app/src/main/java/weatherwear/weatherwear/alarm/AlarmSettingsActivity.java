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
    private boolean mSunday;
    private boolean mMonday;
    private boolean mTuesday;
    private boolean mWednesday;
    private boolean mThursday;
    private boolean mFriday;
    private boolean mSaturday;
    private AlarmDatabaseHelper mDbHelper;
    private boolean mFromHistory;
    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_settings_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.text_SetAlarm);
        setSupportActionBar(toolbar);
        mAlarmModel = new AlarmModel();
        mSunday = false;
        mMonday = false;
        mTuesday = false;
        mWednesday = false;
        mThursday = false;
        mFriday = false;
        mSaturday = false;
        Bundle extras = getIntent().getExtras();
        mFromHistory = (extras != null);
        if(mFromHistory){
            //getId
            mId = extras.getLong(AlarmFragment.ID_KEY);
            mAlarmModel.setId(mId);
            //get alarm days on
            mSunday = extras.getBoolean(AlarmFragment.SUN_KEY);
            mAlarmModel.setSun(mSunday);
            setPressed(mSunday, R.id.sundayButton);
            mMonday = extras.getBoolean(AlarmFragment.MON_KEY);
            mAlarmModel.setMon(mMonday);
            setPressed(mMonday, R.id.mondayButton);
            mTuesday = extras.getBoolean(AlarmFragment.TUES_KEY);
            mAlarmModel.setTues(mTuesday);
            setPressed(mTuesday, R.id.tuesdayButton);
            mWednesday = extras.getBoolean(AlarmFragment.WED_KEY);
            mAlarmModel.setWed(mWednesday);
            setPressed(mWednesday, R.id.wednesdayButton);
            mThursday = extras.getBoolean(AlarmFragment.THURS_KEY);
            mAlarmModel.setThurs(mThursday);
            setPressed(mThursday, R.id.thursdayButton);
            mFriday = extras.getBoolean(AlarmFragment.FRI_KEY);
            mAlarmModel.setFri(mFriday);
            setPressed(mFriday, R.id.fridayButton);
            mSaturday = extras.getBoolean(AlarmFragment.SAT_KEY);
            mAlarmModel.setSat(mSaturday);
            setPressed(mSaturday, R.id.saturdayButton);
            //
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(extras.getLong(AlarmFragment.TIME_KEY));
            mAlarmModel.setTime(cal.getTimeInMillis());
            TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_timePicker);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        }
        mDbHelper = new AlarmDatabaseHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_button:
                if(mFromHistory) {
                    mDbHelper.removeEntry(mId);
                }
                Toast.makeText(getApplicationContext(), "Alarm deleted", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setPressed(boolean sound, int id) {
        at.markushi.ui.CircleButton button = (CircleButton) findViewById(id);
        if (sound) {
            button.setPressed(true);
            button.setTranslationY(-20);
        } else {
            button.setPressed(false);
            button.setTranslationY(0);
        }
    }

    public void onCancel(View view) {
        Toast.makeText(getApplicationContext(), "Alarm cancelled", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onSave(View view) {
        if(mAlarmModel.isDayChosen()) {
            mAlarmModel.setSun(mSunday);
            mAlarmModel.setMon(mMonday);
            mAlarmModel.setTues(mTuesday);
            mAlarmModel.setWed(mWednesday);
            mAlarmModel.setThurs(mThursday);
            mAlarmModel.setFri(mFriday);
            mAlarmModel.setSat(mSaturday);
            TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_timePicker);
            mAlarmModel.setTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
            new InsertData().execute(mAlarmModel);
        } else {
            Toast.makeText(getApplicationContext(), "Choose a day!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDayClick(View view) {
        int id = view.getId();
        switch (id) {
            case (R.id.sundayButton):
                mAlarmModel.changeSun();
                mSunday = mAlarmModel.getSun();
                setPressed(mSunday, R.id.sundayButton);
                break;
            case (R.id.mondayButton):
                mAlarmModel.changeMon();
                mMonday = mAlarmModel.getMon();
                setPressed(mMonday, R.id.mondayButton);
                break;
            case (R.id.tuesdayButton):
                mAlarmModel.changeTues();
                mTuesday = mAlarmModel.getTues();
                setPressed(mTuesday, R.id.tuesdayButton);
                break;
            case (R.id.wednesdayButton):
                mAlarmModel.changeWed();
                mWednesday = mAlarmModel.getWed();
                setPressed(mWednesday, R.id.wednesdayButton);
                break;
            case (R.id.thursdayButton):
                mAlarmModel.changeThurs();
                mThursday = mAlarmModel.getThurs();
                setPressed(mThursday, R.id.thursdayButton);
                break;
            case (R.id.fridayButton):
                mAlarmModel.changeFri();
                mFriday = mAlarmModel.getFri();
                setPressed(mFriday, R.id.fridayButton);
                break;
            case (R.id.saturdayButton):
                mAlarmModel.changeSat();
                mSaturday = mAlarmModel.getSat();
                setPressed(mSaturday, R.id.saturdayButton);
                break;
            default:
                break;
        }

    }

    // Inserts into database
    private class InsertData extends AsyncTask<AlarmModel, Void, Void> {
        @Override
        protected Void doInBackground(AlarmModel... args) {
            if(mFromHistory){
                mDbHelper.onUpdate(args[0]);
            } else {
                Log.d("InsertDataLogD", "" + args[0].getIsOn());
                mDbHelper.insertAlarm(args[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Alarm saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
