package weatherwear.weatherwear;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Calendar;

public class AlarmScheduler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_scheduler);

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

}
