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


import java.util.ArrayList;
import java.util.Calendar;

public class AlarmSchedulerActivity extends AppCompatActivity {
    private AlarmModel mAlarmModel;
    private boolean mRepeat;
    private boolean mSunday;
    private boolean mMonday;
    private boolean mTuesday;
    private boolean mWednesday;
    private boolean mThursday;
    private boolean mFriday;
    private boolean mSaturday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_scheduler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.text_SetAlarm);
        setSupportActionBar(toolbar);
        mAlarmModel = new AlarmModel();
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
        at.markushi.ui.CircleButton button = (at.markushi.ui.CircleButton) findViewById(id);
        switch(id) {
            case (R.id.sundayButton):
                if(mAlarmModel.getDay(R.string.Sunday)){
                    Log.d("LogD","sunday is true");
                    mAlarmModel.setDay(R.string.Sunday, false);
                    button.setColor(R.color.colorPrimaryDark);
                } else{
                    Log.d("LogD","sunday is false");
                    mAlarmModel.setDay(R.string.Sunday,true);
                    button.setColor(R.color.colorAccent);
                }
                break;
            case (R.id.mondayButton):
                //mRepeatDays.add(getString(R.string.Monday));
                break;
            case (R.id.tuesdayButton):
                //mRepeatDays.add(getString(R.string.Tuesday));
                break;
            case (R.id.wednesdayButton):
                //mRepeatDays.add(getString(R.string.Wednesday));
                break;
            case (R.id.thursdayButton):
                //mRepeatDays.add(getString(R.string.Thursday));
                break;
            case (R.id.fridayButton):
               // mRepeatDays.add(getString(R.string.Friday));
                break;
            case (R.id.saturdayButton):
                //mRepeatDays.add(getString(R.string.Saturday));
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
