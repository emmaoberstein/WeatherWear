package weatherwear.weatherwear.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import weatherwear.weatherwear.MainActivity;
import weatherwear.weatherwear.NewOutfitActivity;

/**
 * Created by emilylin27 on 2/27/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    //Receives alarm broadcast
    @Override
    public void onReceive(final Context context, Intent intent) {
        startAlarm(context);
    }

    // Starts the alarm/vibration to play, and triggers the MainActivity to start
    private void startAlarm(Context context) {
        AlarmAlertManager aAManager = new AlarmAlertManager(context);
        if(!aAManager.isPlaying()){
            aAManager.startAlerts();
        }
        // Starts MainActivity
        Intent i = new Intent(context, MainActivity.class);
        // Ensures that it clears the current activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }
}
