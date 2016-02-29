package weatherwear.weatherwear;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import weatherwear.weatherwear.database.AlarmDatabaseHelper;

/**
 * Created by emilylin27 on 2/28/16.
 */
public class AlarmScheduler {
    private static AlarmDatabaseHelper mDbHelper;

    public static void setSchedule(Context context) {
        mDbHelper = new AlarmDatabaseHelper(context);
        for(AlarmModel a: mDbHelper.fetchEntries()){
            if(a.getIsOn()) {
                if (a.getSun()) {
                    setSchedule(context, a, 1);
                }
                if (a.getMon()) {
                    setSchedule(context, a, 2);
                }
                if (a.getTues()) {
                    setSchedule(context, a, 3);
                }
                if (a.getWed()) {
                    setSchedule(context, a, 4);
                }
                if (a.getThurs()) {
                    setSchedule(context, a, 5);
                }
                if (a.getFri()) {
                    setSchedule(context, a, 6);
                }
                if (a.getSat()) {
                    setSchedule(context, a, 7);
                }
            }
        }
    }
    // From 1970 epoch time in seconds to something like "10/24/2012"
    private static String parseTime(long msTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(msTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private static void setSchedule(Context context, AlarmModel a, int dayOfTheWeek) {
        // the request code distinguish different stress meter schedule instances
        int requestCode = (int) a.getTimeInMillis();
        Log.d("requestCode", requestCode + "");
        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = a.getTime();
        //calendar.set(Calendar.DAY_OF_WEEK, dayOfTheWeek);
        Log.d("AlarmLogD", parseTime(calendar.getTimeInMillis()));

        if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(a.getRepeat()){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    24 * 60 * 60 * 1000, pi);
        } else{
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        }

    }

}