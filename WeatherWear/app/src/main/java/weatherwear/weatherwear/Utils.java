package weatherwear.weatherwear;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by emilylin27 on 3/3/16.
 */
public class Utils {
    private static final String DATE_FORMAT_TIME = "h:mm a";
    private static final String DATE_FORMAT_DATE = "MM.dd.yyyy";

    // parse to something like 5:00PM
    public static String parseTime(long msTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(msTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_TIME, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    // From 1970 epoch time in seconds to something like "01.02.1995"
    public static String parseDate(long msDate) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(msDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DATE, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

}
