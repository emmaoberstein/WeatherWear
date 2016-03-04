package weatherwear.weatherwear.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by emilylin27 on 2/28/16.
 */
public class AlarmScheduler {
    private static AlarmDatabaseHelper mDbHelper;
    private static PendingIntent mPi;
    private static AlarmManager mAlarmManager;
    private static Context mContext;
    public static final String REQUEST_CODE_KEY = "requestcode";

    public static void setSchedule(Context context) {
        mContext = context;
        mDbHelper = new AlarmDatabaseHelper(mContext);
        mAlarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        for(AlarmModel a: mDbHelper.fetchEntries()){
            if (a.getSun()) {
                toggleAlarm(a, 1);
            }
            if (a.getMon()) {
                toggleAlarm(a, 2);
            }
            if (a.getTues()) {
                toggleAlarm(a, 3);
            }
            if (a.getWed()) {
                toggleAlarm(a, 4);
            }
            if (a.getThurs()) {
                toggleAlarm(a, 5);
            }
            if (a.getFri()) {
                toggleAlarm(a, 6);
            }
            if (a.getSat()) {
                toggleAlarm(a, 7);
            }
        }
    }

    private static void toggleAlarm(AlarmModel a, int day) {
        if (a.getIsOn()) {
            Log.d("logd","startalarm");
            Calendar calendar = Calendar.getInstance();
            Intent i = new Intent(mContext, AlarmReceiver.class);
            i.putExtra(REQUEST_CODE_KEY, a.getRequestCode());
            mPi = PendingIntent.getBroadcast(mContext, a.getRequestCode(), i,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.DAY_OF_WEEK, day);
            calendar.set(Calendar.HOUR_OF_DAY, a.getHour());
            calendar.set(Calendar.MINUTE, a.getMinutes());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.DATE, 1);
            }
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        1 * 60 * 60 * 1000, mPi);
        } else {
            cancelAlarm(a);
        }
    }

    public static void cancelAlarm(AlarmModel a){
        Log.d("Logd","cancelAlarm");
        Intent i = new Intent(mContext, AlarmReceiver.class);
        mPi = PendingIntent.getBroadcast(mContext, a.getRequestCode(), i, PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmManager.cancel(mPi);
    }
}