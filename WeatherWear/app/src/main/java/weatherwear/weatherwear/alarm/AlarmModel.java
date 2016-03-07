package weatherwear.weatherwear.alarm;

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

    // get whether alarm should be set for that day
    public boolean getSun(){ return mSunday; }

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

    // get state of alarm (on/off)
    public boolean getIsOn(){ return mIsOn; }

    // time getters
    public long getTimeInMillis() { return mTime.getTimeInMillis(); }

    public int getHour(){
        return mTime.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinutes(){
        return mTime.get(Calendar.MINUTE);
    }

    public Calendar getTime(){ return mTime; }

    // returns unique request code for each alarm
    public int getRequestCode(){ return (int)mId; }

    public long getId(){ return mId; }

    // change state of alarm on that day
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

    // should an alarm be set for this day?
    public void setSun(boolean set) {
        mSunday = set;
    }

    public void setMon(boolean set) {
        mMonday = set;
    }

    public void setTues(boolean set) {
        mTuesday = set;
    }

    public void setWed(boolean set) {
        mWednesday = set;
    }

    public void setThurs(boolean set) {
        mThursday = set;
    }

    public void setFri(boolean set) {
        mFriday = set;
    }

    public void setSat(boolean set) {
        mSaturday = set;
    }

    // turn this alarm on or off
    public void setIsOn(boolean isOn) {
        mIsOn = isOn;
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

    // String generator for AlarmFragment list below alarm time
    public String weeklyInfo() {
        String week = "Repeat every: ";
        if (mSunday) { week += "Sun "; }
        if (mMonday) { week += "Mon "; }
        if (mTuesday) { week += "Tues "; }
        if (mWednesday) { week += "Wed "; }
        if (mThursday) { week += "Thurs "; }
        if (mFriday) { week += "Fri "; }
        if (mSaturday) { week += "Sat"; }
        return week;
    }

    // Check to ensure that at least one day is on
    public boolean isDayChosen(){
        return mSunday || mMonday || mTuesday || mWednesday || mThursday || mFriday || mSaturday;
    }
}
