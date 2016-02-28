package weatherwear.weatherwear;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import at.markushi.ui.CircleButton;
import weatherwear.weatherwear.database.AlarmDatabaseHelper;

public class AlarmSchedulerActivity extends AppCompatActivity {
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
        mDbHelper = new AlarmDatabaseHelper(this);
        if (savedInstanceState != null) {
            mSunday = savedInstanceState.getBoolean(SUN_KEY, false);
            setColor(mSunday, R.id.sundayButton);
            mMonday = savedInstanceState.getBoolean(MON_KEY, false);
            setColor(mMonday, R.id.mondayButton);
            mTuesday = savedInstanceState.getBoolean(TUES_KEY, false);
            setColor(mTuesday, R.id.tuesdayButton);
            mWednesday = savedInstanceState.getBoolean(WED_KEY, false);
            setColor(mWednesday, R.id.wednesdayButton);
            mThursday = savedInstanceState.getBoolean(THURS_KEY, false);
            setColor(mThursday, R.id.thursdayButton);
            mFriday = savedInstanceState.getBoolean(FRI_KEY, false);
            setColor(mFriday, R.id.fridayButton);
            mSaturday = savedInstanceState.getBoolean(SAT_KEY, false);
            setColor(mSaturday, R.id.saturdayButton);
        }
    }

    private void setColor(boolean sound, int id) {
        at.markushi.ui.CircleButton button = (CircleButton) findViewById(id);
        if (sound) {
            button.setColor(R.color.colorAccent);
        } else {
            button.setColor(R.color.colorPrimaryDark);
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

    public static void setSchedule(Context context) {
        // 12:30 PM
        setSchedule(context, 12, 30, 0);
        // 6:30 PM
        setSchedule(context, 18, 30, 0);
    }

    private static void setSchedule(Context context, int hour, int min, int sec) {

        // the request code distinguish different stress meter schedule instances
        int requestCode = hour * 10000 + min * 100 + sec;
        Intent intent = new Intent(context, AlarmReceiver.class);

        //set pending intent to call AlarmReceiver.
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
        }

        //set repeating alarm, and pass the pending intent,
        //so that the broadcast is sent everytime the alarm
        //is triggered
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);
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
                setColor(mSunday, R.id.sundayButton);
                break;
            case (R.id.mondayButton):
                setColor(mMonday, R.id.mondayButton);
                break;
            case (R.id.tuesdayButton):
                mAlarmModel.changeTues();
                mTuesday = mAlarmModel.getTues();
                setColor(mTuesday, R.id.tuesdayButton);
                break;
            case (R.id.wednesdayButton):
                mAlarmModel.changeWed();
                mWednesday = mAlarmModel.getWed();
                setColor(mWednesday, R.id.wednesdayButton);
                break;
            case (R.id.thursdayButton):
                mAlarmModel.changeThurs();
                mThursday = mAlarmModel.getThurs();
                setColor(mThursday, R.id.thursdayButton);
                break;
            case (R.id.fridayButton):
                mAlarmModel.changeFri();
                mFriday = mAlarmModel.getFri();
                setColor(mFriday, R.id.fridayButton);
                break;
            case (R.id.saturdayButton):
                mAlarmModel.changeSat();
                mSaturday = mAlarmModel.getSat();
                setColor(mSaturday, R.id.saturdayButton);
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
            mDbHelper.insertAlarm(args[0]);
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
