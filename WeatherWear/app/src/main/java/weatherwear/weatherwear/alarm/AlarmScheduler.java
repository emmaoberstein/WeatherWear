package weatherwear.weatherwear.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by emilylin27 on 2/28/16.
 */
public class AlarmScheduler {
    private static AlarmDatabaseHelper mDbHelper;
    private static PendingIntent mPi;
    private static AlarmManager mAlarmManager;
    private static Map<Integer, AlarmModel> mAlarmMap;
    private static Context mContext;
    public static final String REPEAT_KEY = "repeat";
    public static final String REQUEST_CODE_KEY = "requestcode";

    public static void setSchedule(Context context) {
        Log.d("AlarmSchedulerLogd","setSchedule");
        mContext = context;
        mDbHelper = new AlarmDatabaseHelper(mContext);
        mAlarmManager = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);
        mAlarmMap = new HashMap<Integer, AlarmModel>();
        for(AlarmModel a: mDbHelper.fetchEntries()){
            Log.d("AlarmSchedulerArrayLogd","setSchedule:"+AlarmFragment.parseTime(a.getTimeInMillis()));
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

    private static void toggleAlarm(AlarmModel a, int day){
        Log.d("AlarmSchedulerLogD","ToggleAlarm:"+AlarmFragment.parseTime(a.getTimeInMillis()));
        if (a.getIsOn()) {
            Calendar calendar = Calendar.getInstance();
            Intent i = new Intent(mContext, AlarmReceiver.class);
            i.putExtra(REPEAT_KEY,a.getRepeat());
            i.putExtra(REQUEST_CODE_KEY, a.getRequestCode());
            mPi = PendingIntent.getBroadcast(mContext, a.getRequestCode(), i, 0);
            calendar.set(Calendar.DAY_OF_WEEK, day);
            calendar.set(Calendar.HOUR_OF_DAY, a.getHour());
            calendar.set(Calendar.MINUTE, a.getMinutes());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.DATE, 1);
            }

            if(a.getRepeat()){
                Log.d("AlarmSchedulerLogD","startRepeatingAlarm:"+AlarmFragment.parseTime(a.getTimeInMillis()));
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        mAlarmManager.INTERVAL_DAY * 7, mPi);
            } else {
                Log.d("AlarmSchedulerLogD","startAlarm:"+AlarmFragment.parseTime(a.getTimeInMillis()));
                mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mPi);
            }
            mAlarmMap.put(a.getRequestCode(), a);
        } else {
            cancelAlarm(a);
            mAlarmMap.remove(a.getRequestCode());
        }
    }

    public static AlarmModel getAlarm(int key){
        return mAlarmMap.get(key);
    }

    public static void cancelAlarm(AlarmModel a){
        Log.d("AlarmSchedulerLogD", "CancelAlarm:" + AlarmFragment.parseTime(a.getTimeInMillis()));
        Intent i = new Intent(mContext, AlarmReceiver.class);
        mPi = PendingIntent.getBroadcast(mContext, a.getRequestCode(), i, 0);
        mAlarmManager.cancel(mPi);
    }

}