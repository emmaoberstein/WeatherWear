package weatherwear.weatherwear;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by emilylin27 on 2/27/16.
 */
public class AlarmModel {
    private boolean mRepeat;
    private ArrayList<String> mDaysOfTheWeek;
    private Calendar mTime;

    public AlarmModel(){
        mDaysOfTheWeek = new ArrayList<String>();
    }

    public void addDayOfTheWeek(String day){
        mDaysOfTheWeek.add(day);
    }


}
