package weatherwear.weatherwear;

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
import weatherwear.weatherwear.database.AlarmDatabaseHelper;

public class AlarmSettingsActivity extends AppCompatActivity {
    private static final String SUN_KEY = "sun";
    private static final String MON_KEY = "mon";
    private static final String TUES_KEY = "tues";
    private static final String WED_KEY = "wed";
    private static final String THURS_KEY = "thurs";
    private static final String FRI_KEY = "fri";
    private static final String SAT_KEY = "sat";
    private static final String REPEAT_KEY = "repeat";

    private AlarmModel mAlarmModel;
    private boolean mRepeat;
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
        setContentView(R.layout.activity_alarm_scheduler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.text_SetAlarm);
        setSupportActionBar(toolbar);
        mAlarmModel = new AlarmModel();
        mRepeat = false;
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
            mId = extras.getLong(AlarmFragment.ID_KEY);
            mRepeat = extras.getBoolean(AlarmFragment.REPEAT_KEY);
            CheckBox checkBox = (CheckBox) findViewById(R.id.alarm_repeatCheck);
            checkBox.setChecked(mRepeat);
            mSunday = extras.getBoolean(AlarmFragment.SUN_KEY);
            setPressed(mSunday, R.id.sundayButton);
            mMonday = extras.getBoolean(AlarmFragment.MON_KEY);
            setPressed(mMonday, R.id.mondayButton);
            mTuesday = extras.getBoolean(AlarmFragment.TUES_KEY);
            setPressed(mTuesday, R.id.tuesdayButton);
            mWednesday = extras.getBoolean(AlarmFragment.WED_KEY);
            setPressed(mWednesday, R.id.wednesdayButton);
            mThursday = extras.getBoolean(AlarmFragment.THURS_KEY);
            setPressed(mThursday, R.id.thursdayButton);
            mFriday = extras.getBoolean(AlarmFragment.FRI_KEY);
            setPressed(mFriday, R.id.fridayButton);
            mSaturday = extras.getBoolean(AlarmFragment.SAT_KEY);
            setPressed(mSaturday, R.id.saturdayButton);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(extras.getLong(AlarmFragment.TIME_KEY));
            TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_timePicker);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
        }
        mDbHelper = new AlarmDatabaseHelper(this);
        if (savedInstanceState != null) {
            mSunday = savedInstanceState.getBoolean(SUN_KEY, false);
            setPressed(mSunday, R.id.sundayButton);
            mMonday = savedInstanceState.getBoolean(MON_KEY, false);
            setPressed(mMonday, R.id.mondayButton);
            mTuesday = savedInstanceState.getBoolean(TUES_KEY, false);
            setPressed(mTuesday, R.id.tuesdayButton);
            mWednesday = savedInstanceState.getBoolean(WED_KEY, false);
            setPressed(mWednesday, R.id.wednesdayButton);
            mThursday = savedInstanceState.getBoolean(THURS_KEY, false);
            setPressed(mThursday, R.id.thursdayButton);
            mFriday = savedInstanceState.getBoolean(FRI_KEY, false);
            setPressed(mFriday, R.id.fridayButton);
            mSaturday = savedInstanceState.getBoolean(SAT_KEY, false);
            setPressed(mSaturday, R.id.saturdayButton);
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SUN_KEY, mSunday);
        outState.putBoolean(MON_KEY, mMonday);
        outState.putBoolean(TUES_KEY, mTuesday);
        outState.putBoolean(WED_KEY, mWednesday);
        outState.putBoolean(THURS_KEY, mThursday);
        outState.putBoolean(FRI_KEY, mFriday);
        outState.putBoolean(SAT_KEY, mSaturday);
        outState.putBoolean(REPEAT_KEY, mRepeat);
    }

    public void onCancel(View view) {
        finish();
    }

    public void onSave(View view) {
        mAlarmModel.setRepeat(mRepeat);
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
        finish();
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

    public void onRepeatClick(View view) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.alarm_repeatCheck);
        mRepeat = checkBox.isChecked();
    }


    // Inserts into database
    private class InsertData extends AsyncTask<AlarmModel, Void, Void> {
        @Override
        protected Void doInBackground(AlarmModel... args) {
            if(mFromHistory){

            } else {
                mDbHelper.insertAlarm(args[0]);
            }
            AlarmScheduler.setSchedule(getApplicationContext());
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
