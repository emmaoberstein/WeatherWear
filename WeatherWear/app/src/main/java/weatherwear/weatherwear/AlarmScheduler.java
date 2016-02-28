package weatherwear.weatherwear;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import weatherwear.weatherwear.database.AlarmDatabaseHelper;

/**
 * Created by emilylin27 on 2/28/16.
 */
public class AlarmScheduler {
    private static AlarmDatabaseHelper mDbHelper;
    private static Calendar mCalendar;

    public static void setSchedule(Context context) {
        mDbHelper = new AlarmDatabaseHelper(context);
        for(AlarmModel a: mDbHelper.fetchEntries()){
            setSchedule(context, a);
        }
    }

    private static void setSchedule(Context context, AlarmModel a) {
        mCalendar = a.getTime();
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int min = mCalendar.get(Calendar.MINUTE);

        // the request code distinguish different stress meter schedule instances
        int requestCode = hour * 10000 + min * 100;
        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        //set repeating alarm, and pass the pending intent,
        //so that the broadcast is sent everytime the alarm
        //is triggered
        if(a.getSun()){
            mCalendar.set(Calendar.DAY_OF_WEEK, 1);
            setAlarm(a.getRepeat(),1,mCalendar.getTimeInMillis(),context,pi);
        }
        if(a.getMon()){
            mCalendar.set(Calendar.DAY_OF_WEEK, 2);
            setAlarm(a.getRepeat(), 2, mCalendar.getTimeInMillis(), context, pi);
        }
        if(a.getTues()){
            mCalendar.set(Calendar.DAY_OF_WEEK, 3);
            setAlarm(a.getRepeat(), 3, mCalendar.getTimeInMillis(), context, pi);
        }
        if(a.getWed()){
            mCalendar.set(Calendar.DAY_OF_WEEK, 4);
            setAlarm(a.getRepeat(), 4, mCalendar.getTimeInMillis(), context, pi);
        }
        if(a.getThurs()){
            mCalendar.set(Calendar.DAY_OF_WEEK, 5);
            setAlarm(a.getRepeat(), 5, mCalendar.getTimeInMillis(), context, pi);
        }
        if(a.getFri()){
            mCalendar.set(Calendar.DAY_OF_WEEK, 6);
            setAlarm(a.getRepeat(),6,mCalendar.getTimeInMillis(),context,pi);
        }
        if(a.getSat()){
            mCalendar.set(Calendar.DAY_OF_WEEK, 7);
            setAlarm(a.getRepeat(), 7, mCalendar.getTimeInMillis(), context, pi);
        }
    }

    private static void setAlarm(boolean isRepeating, int dayOfWeek, long timeInMillis,
                          Context context, PendingIntent pi){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(isRepeating){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,
                    1 * 60 * 60 * 1000, pi);
        } else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
        }
    }

}