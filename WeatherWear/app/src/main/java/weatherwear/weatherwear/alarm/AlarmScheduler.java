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
    private static PendingIntent mFuturePi;
    private static PendingIntent mImmediatePi;
    private static AlarmManager mAlarmManager;
    private static Context mContext;
    public static final String REQUEST_CODE_KEY = "requestcode";

    public static void setSchedule(Context context) {
        mContext = context;
        mDbHelper = new AlarmDatabaseHelper(mContext);
        mAlarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        for(AlarmModel a: mDbHelper.fetchEntries()){
            if (a.getSun()) {
                updateAlarm(a, 1);
            }
            if (a.getMon()) {
                updateAlarm(a, 2);
            }
            if (a.getTues()) {
                updateAlarm(a, 3);
            }
            if (a.getWed()) {
                updateAlarm(a, 4);
            }
            if (a.getThurs()) {
                updateAlarm(a, 5);
            }
            if (a.getFri()) {
                updateAlarm(a, 6);
            }
            if (a.getSat()) {
                updateAlarm(a, 7);
            }
        }
    }

    private static void updateAlarm(AlarmModel a, int day) {
        if (a.getIsOn()) {
            Calendar calendar = Calendar.getInstance();

            // Set up immediate intent (for accuracy)
            Intent immediateI = new Intent(mContext, AlarmReceiver.class);
            immediateI.putExtra(REQUEST_CODE_KEY, a.getRequestCode());
            mImmediatePi = PendingIntent.getBroadcast(mContext, a.getRequestCode(), immediateI, PendingIntent.FLAG_UPDATE_CURRENT);

            // Set up future intent (for overall)
            Intent futureI = new Intent(mContext, AlarmReceiver.class);
            futureI.putExtra(REQUEST_CODE_KEY, a.getRequestCode() * 1000);
            mFuturePi = PendingIntent.getBroadcast(mContext, a.getRequestCode() * 1000, futureI, PendingIntent.FLAG_UPDATE_CURRENT);

            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.DAY_OF_WEEK, day);
            calendar.set(Calendar.HOUR_OF_DAY, a.getHour());
            calendar.set(Calendar.MINUTE, a.getMinutes());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.DATE, 7);
            }
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mImmediatePi);
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 7 * 24 * 60 * 60 * 1000, AlarmManager.INTERVAL_DAY * 7, mFuturePi);
        } else {
            cancelAlarm(a);
        }
    }

    public static void cancelAlarm(AlarmModel a){
        Intent immedateI = new Intent(mContext, AlarmReceiver.class);
        mImmediatePi = PendingIntent.getBroadcast(mContext, a.getRequestCode(), immedateI, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.cancel(mImmediatePi);

        Intent futureI = new Intent(mContext, AlarmReceiver.class);
        mFuturePi = PendingIntent.getBroadcast(mContext, a.getRequestCode() * 1000, futureI, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.cancel(mFuturePi);
    }
}