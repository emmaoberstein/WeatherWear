package weatherwear.weatherwear;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by emilylin27 on 3/3/16.
 */
public class Utils {
    private static final String DATE_FORMAT_TIME = "h:mm a";
    private static final String DATE_FORMAT_DATE = "MM.dd.yyyy";
    private static final String DATE_FORMAT_VACATION = "MMMM d";

    // parse to something like 5:00PM
    public static String parseTime(long msTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(msTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_TIME, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    // parse to something like "01.02.1995"
    public static String parseDate(long msDate) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(msDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DATE, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }


    public static String parseVacationDate(long msDate) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(msDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_VACATION, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static int getNumDays(long start, long end){
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTimeInMillis(start);
        endCal.setTimeInMillis(end);

        // Set the copies to be at midnight, but keep the day information.
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);

        endCal.set(Calendar.HOUR_OF_DAY, 0);
        endCal.set(Calendar.MINUTE, 0);
        endCal.set(Calendar.SECOND, 0);
        endCal.set(Calendar.MILLISECOND, 0);

        // At this point, each calendar is set to midnight on
        // their respective days. Now use TimeUnit.MILLISECONDS to
        // compute the number of full days between the two of them.
        int difference = (int) ((endCal.getTimeInMillis() - startCal.getTimeInMillis()) / (1000 * 60 * 60 * 24));
        if (difference == -1) {
            return -1;
        } else if (difference == 0) {
            return 0;
        } else {
            return difference + 1;
        }
    }

    // Get which day (whether valid/today/what day in the future)
    public static int getWhichDay(long start, int dayClicked) {
        Calendar startCal = Calendar.getInstance();
        Calendar todayCal = Calendar.getInstance();
        startCal.setTimeInMillis(start);
        todayCal.setTimeInMillis(System.currentTimeMillis());

        // Set the copies to be at midnight, but keep the day information.
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);

        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        // At this point, each calendar is set to midnight on
        // their respective days. Now use TimeUnit.MILLISECONDS to
        // compute the number of full days between the two of them.
        int difference = (int) ((todayCal.getTimeInMillis() - startCal.getTimeInMillis()) / (1000 * 60 * 60 * 24));
        if ((dayClicked == 0) && difference == 0) {
            return 0;
        } else if (difference > dayClicked) {
            return -1;
        } else {
            return dayClicked - difference;
        }
    }

}
