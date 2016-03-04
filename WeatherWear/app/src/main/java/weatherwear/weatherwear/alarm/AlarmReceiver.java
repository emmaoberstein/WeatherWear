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

    //Receive broadcast
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("ALarmReceiverLogD", "onRecieve");
        startAlarm(context);
    }

    private void startAlarm(Context context) {
        Log.d("AlarmReceiverLogD", "startAlarm");
        AlarmAlertManager aAManager = new AlarmAlertManager(context);
        if(!aAManager.isPlaying()){
            aAManager.startAlerts();
        }
        Intent i = new Intent(context, MainActivity.class); //The activity you  want to start.
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(i);
    }
}
