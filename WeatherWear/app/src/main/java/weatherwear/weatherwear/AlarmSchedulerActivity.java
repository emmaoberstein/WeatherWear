package weatherwear.weatherwear;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import java.util.Calendar;
import at.markushi.ui.CircleButton;

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
    private at.markushi.ui.CircleButton mButton;

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
        if(savedInstanceState != null){
            mSunday = savedInstanceState.getBoolean(SUN_KEY,false);
            if(mSunday){
                mButton = (CircleButton) findViewById(R.id.sundayButton);
                mButton.setColor(R.color.colorAccent);
            }
            mMonday = savedInstanceState.getBoolean(MON_KEY, false);
            if(mMonday){
                Log.d("Logd","mondayisTrue");
                mButton = (CircleButton) findViewById(R.id.mondayButton);
                mButton.setColor(R.color.colorAccent);
            }
            mTuesday = savedInstanceState.getBoolean(TUES_KEY, false);
            if(mTuesday){
                mButton = (CircleButton) findViewById(R.id.tuesdayButton);
                mButton.setColor(R.color.colorAccent);
            }
            mWednesday = savedInstanceState.getBoolean(WED_KEY, false);
            if(mWednesday){
                mButton = (CircleButton) findViewById(R.id.wednesdayButton);
                mButton.setColor(R.color.colorAccent);
            }
            mThursday = savedInstanceState.getBoolean(THURS_KEY, false);
            if(mThursday){
                mButton = (CircleButton) findViewById(R.id.thursdayButton);
                mButton.setColor(R.color.colorAccent);
            }
            mFriday = savedInstanceState.getBoolean(FRI_KEY, false);
            if(mFriday){
                mButton = (CircleButton) findViewById(R.id.fridayButton);
                mButton.setColor(R.color.colorAccent);
            }
            mSaturday = savedInstanceState.getBoolean(SAT_KEY, false);
            if(mSaturday){
                mButton = (CircleButton) findViewById(R.id.saturdayButton);
                mButton.setColor(R.color.colorAccent);
            }
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

        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
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
        finish();
    }

    public void onDayClick(View view) {
        int id = view.getId();
        mButton = (CircleButton) findViewById(id);
        switch(id) {
            case (R.id.sundayButton):
                if(mSunday){
                    mSunday = false;
                    mAlarmModel.setDay(R.string.Sunday, false);
                    mButton.setColor(R.color.colorPrimaryDark);
                } else{
                    mSunday = true;
                    mAlarmModel.setDay(R.string.Sunday,true);
                    mButton.setColor(R.color.colorAccent);
                }
                break;
            case (R.id.mondayButton):
                if(mMonday){
                    mMonday = false;
                    mAlarmModel.setDay(R.string.Monday, false);
                    mButton.setColor(R.color.colorPrimaryDark);
                } else{
                    mMonday = true;
                    mAlarmModel.setDay(R.string.Monday,true);
                    mButton.setColor(R.color.colorAccent);
                }
                break;
            case (R.id.tuesdayButton):
                if(mTuesday){
                    mTuesday = false;
                    mAlarmModel.setDay(R.string.Tuesday, false);
                    mButton.setColor(R.color.colorPrimaryDark);
                } else{
                    mTuesday = true;
                    mAlarmModel.setDay(R.string.Tuesday,true);
                    mButton.setColor(R.color.colorAccent);
                }
                break;
            case (R.id.wednesdayButton):
                if(mWednesday){
                    mWednesday = false;
                    mAlarmModel.setDay(R.string.Wednesday, false);
                    mButton.setColor(R.color.colorPrimaryDark);
                } else{
                    mWednesday = true;
                    mAlarmModel.setDay(R.string.Wednesday,true);
                    mButton.setColor(R.color.colorAccent);
                }
                break;
            case (R.id.thursdayButton):
                if(mThursday){
                    mThursday = false;
                    mAlarmModel.setDay(R.string.Thursday, false);
                    mButton.setColor(R.color.colorPrimaryDark);
                } else{
                    mThursday = true;
                    mAlarmModel.setDay(R.string.Thursday,true);
                    mButton.setColor(R.color.colorAccent);
                }
                break;
            case (R.id.fridayButton):
                if(mFriday){
                    mFriday = false;
                    mAlarmModel.setDay(R.string.Friday, false);
                    mButton.setColor(R.color.colorPrimaryDark);
                } else{
                    mFriday = true;
                    mAlarmModel.setDay(R.string.Friday,true);
                    mButton.setColor(R.color.colorAccent);
                }
                break;
            case (R.id.saturdayButton):
                if(mSaturday){
                    mSaturday = false;
                    mAlarmModel.setDay(R.string.Saturday, false);
                    mButton.setColor(R.color.colorPrimaryDark);
                } else{
                    mSaturday = true;
                    mAlarmModel.setDay(R.string.Saturday,true);
                    mButton.setColor(R.color.colorAccent);
                }
                break;
            default:
                break;
        }
    }

    public void onRepeatClick(View view) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.alarm_repeatCheck);
        mRepeat = checkBox.isChecked();
    }
}
