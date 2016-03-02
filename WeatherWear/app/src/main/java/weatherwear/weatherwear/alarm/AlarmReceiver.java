package weatherwear.weatherwear.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import weatherwear.weatherwear.MainActivity;

/**
 * Created by emilylin27 on 2/27/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private boolean mRepeat;
    private int mRequestCode;
    private AlarmDatabaseHelper mDbHelper;
    //Receive broadcast
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("ALarmReceiverLogD", "onRecieve");
        mRepeat = intent.getBooleanExtra(AlarmScheduler.REPEAT_KEY,false);
        mRequestCode = intent.getIntExtra(AlarmScheduler.REQUEST_CODE_KEY, 0);
        mDbHelper = new AlarmDatabaseHelper(context);
        startAlarm(context);
    }

    private void startAlarm(Context context) {
        Log.d("AlarmReceiverLogD", "startAlarm");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean displayNotification = sharedPref.getBoolean("switchPref_DisplayNotification", true);
        AlarmAlertManager aAManager = new AlarmAlertManager(context);
        if(!aAManager.isPlaying()){
            aAManager.startAlerts();
        }
        Intent i = new Intent(context, MainActivity.class); //The activity you  want to start.
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(mRepeat){
            context.startService(i);
        } else {
            AlarmModel alarmModel = AlarmScheduler.getAlarm(mRequestCode);
            Log.d("AlarmReceiverLogD","not repeat:"+AlarmFragment.parseTime(alarmModel.getTimeInMillis()));
            alarmModel.setIsOn(false);
            mDbHelper.onUpdate(alarmModel);
            AlarmScheduler.setSchedule(context);
            context.startService(i);
        }
    }
}
