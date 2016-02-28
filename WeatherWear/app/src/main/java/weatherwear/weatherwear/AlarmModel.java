package weatherwear.weatherwear;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by emilylin27 on 2/27/16.
 */
public class AlarmModel {
    private boolean mRepeat;
    private boolean mSunday;
    private boolean mMonday;
    private boolean mTuesday;
    private boolean mWednesday;
    private boolean mThursday;
    private boolean mFriday;
    private boolean mSaturday;
    private Calendar mTime;


    public AlarmModel(){
        mRepeat = false;
        mSunday = false;
        mMonday = false;
        mTuesday = false;
        mWednesday = false;
        mThursday = false;
        mFriday = false;
        mSaturday = false;
    }

    public void setDay(int day, boolean setAlarm){
        switch(day){
            case(R.string.Sunday):
                Log.d("LogD", Boolean.toString(setAlarm));
                mSunday = setAlarm;
                break;
            case(R.string.Monday):
                mMonday = setAlarm;
                break;
            case(R.string.Tuesday):
                mTuesday = setAlarm;
                break;
            case(R.string.Wednesday):
                mWednesday = setAlarm;
                break;
            case(R.string.Thursday):
                mThursday = setAlarm;
                break;
            case(R.string.Friday):
                mFriday = setAlarm;
                break;
            case(R.string.Saturday):
                mSaturday = setAlarm;
                break;
            default:
                break;
        }
    }

    public boolean getDay(int day){
        switch(day){
            case(R.string.Sunday):
                return mSunday;
            case(R.string.Monday):
                return mMonday;
            case(R.string.Tuesday):
                return mTuesday;
            case(R.string.Wednesday):
                return mWednesday;
            case(R.string.Thursday):
                return mThursday;
            case(R.string.Friday):
                return mFriday;
            case(R.string.Saturday):
                return mSaturday;
            default:
                return false;
        }
    }

    public void setRepeat(boolean repeat){
        mRepeat = repeat;
    }


}
