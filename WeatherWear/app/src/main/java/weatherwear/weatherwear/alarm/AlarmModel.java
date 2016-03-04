package weatherwear.weatherwear.alarm;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by emilylin27 on 2/27/16.
 */
public class AlarmModel {
    private boolean mSunday;
    private boolean mMonday;
    private boolean mTuesday;
    private boolean mWednesday;
    private boolean mThursday;
    private boolean mFriday;
    private boolean mSaturday;
    private long mId;
    private Calendar mTime;
    private boolean mIsOn;
    private int mDay;

    public AlarmModel(){
        mSunday = false;
        mMonday = false;
        mTuesday = false;
        mWednesday = false;
        mThursday = false;
        mFriday = false;
        mSaturday = false;
        mTime = Calendar.getInstance();
        mTime.setTimeInMillis(System.currentTimeMillis());
        mIsOn = true;
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

    public boolean getIsOn(){ return mIsOn; }

    public long getTimeInMillis() {
        return mTime.getTimeInMillis();
    }

    public int getHour(){
        return mTime.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutes(){
        return mTime.get(Calendar.MINUTE);
    }

    public int getRequestCode(){
        return (int)mId;
    }

    public Calendar getTime(){ return mTime; }

    public long getId(){ return mId; }

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
        mDay = 1;
    }

    public void setMon(boolean set){
        mMonday = set;
        mDay = 2;
    }

    public void setTues(boolean set){
        mTuesday = set;
        mDay = 3;
    }

    public void setWed(boolean set){
        mWednesday = set;
        mDay = 4;
    }

    public void setThurs(boolean set){
        mThursday = set;
        mDay = 5;
    }

    public void setFri(boolean set){
        mFriday = set;
        mDay = 6;
    }

    public void setSat(boolean set){
        mSaturday = set;
        mDay = 7;
    }

    public void setIsOn(boolean isOn){ mIsOn = isOn; }

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

    public String weeklyInfo(){
        String week = "Repeat every: ";
        if(mSunday) { week += "Sun "; }
        if(mMonday) { week += "Mon "; }
        if(mTuesday) { week += "Tues "; }
        if(mWednesday) { week += "Wed "; }
        if(mThursday) { week += "Thurs "; }
        if(mFriday) { week += "Fri "; }
        if(mSaturday) { week += "Sat"; }
        return week;
    }

    public boolean isDayChosen(){
        return mSunday || mMonday || mTuesday || mWednesday || mThursday || mFriday || mSaturday;
    }
}
