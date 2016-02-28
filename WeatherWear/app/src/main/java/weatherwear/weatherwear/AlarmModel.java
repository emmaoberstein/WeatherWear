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
    private long mId;
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
        mTime = Calendar.getInstance();
    }

    public boolean getSun(){
        return mSunday;
    }

    public boolean getMon(){
        return mMonday;
    }

    public boolean getTues(){
        return mTuesday;
    }

    public boolean getWed(){
        return mWednesday;
    }

    public boolean getThurs(){
        return mThursday;
    }

    public boolean getFri(){
        return mFriday;
    }

    public boolean getSat(){
        return mSaturday;
    }

    public boolean getRepeat(){
        return mRepeat;
    }

    public long getTimeInMillis() {
        return mTime.getTimeInMillis();
    }

    public Calendar getTime(){ return mTime; }

    public long getId(){
        return mId;
    }

    public void changeSun(){
        mSunday = !mSunday;
    }

    public void changeMon(){
        mMonday = !mMonday;
    }

    public void changeTues(){
        mTuesday = !mTuesday;
    }

    public void changeWed(){
        mWednesday = !mWednesday;
    }

    public void changeThurs(){
        mThursday = !mThursday;
    }

    public void changeFri(){
        mFriday = !mFriday;
    }

    public void changeSat(){
        mSaturday = !mSaturday;
    }

    public void setSun(boolean set){
        mSunday = set;
    }

    public void setMon(boolean set){
        mMonday = set;
    }

    public void setTues(boolean set){
        mTuesday = set;
    }

    public void setWed(boolean set){
        mWednesday = set;
    }

    public void setThurs(boolean set){
        mThursday = set;
    }

    public void setFri(boolean set){
        mFriday = set;
    }

    public void setSat(boolean set){
        mSaturday = set;
    }

    public void setRepeat(boolean repeat){
        mRepeat = repeat;
    }

    public void setId(long id){
        mId = id;
    }

    public void setTime(int hour, int minute){
        mTime.set(Calendar.HOUR_OF_DAY, hour);
        mTime.set(Calendar.MINUTE, minute);
    }

    public void setTime(long time){
        mTime.setTimeInMillis(time);
    }


}
