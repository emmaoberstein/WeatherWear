package weatherwear.weatherwear.vacation;

import java.util.Calendar;

/**
 * Created by emilylin27 on 3/6/16.
 */
public class OutfitModel {
    private int mTop, mBottom, mScarves, mHat, mGloves, mOuterwear, mShoes, mHigh, mLow;
    private String mLocation, mCondition, mDay;
    private Calendar mDate = Calendar.getInstance();
    private long mId;

    public OutfitModel(){
        mTop = mBottom = mScarves = mHat = mGloves = mOuterwear = mShoes = mHigh = mLow = -1;
        mLocation = mCondition = mDay = "";
        mDate.setTimeInMillis(System.currentTimeMillis());
    }

    //*********getters************

    public long getmId(){
        return mId;
    }

    public int getmTop(){
        return mTop;
    }

    public int getmBottom(){
        return mBottom;
    }

    public int getmScarves(){
        return mScarves;
    }

    public int getmHat(){
        return mHat;
    }

    public int getmGloves(){
        return mGloves;
    }

    public int getmOuterwear(){
        return mOuterwear;
    }

    public int getmShoes(){
        return mShoes;
    }

    public int getmHigh(){
        return mHigh;
    }

    public int getmLow(){
        return mLow;
    }

    public String getmLocation(){
        return mLocation;
    }

    public String getmCondition(){
        return mCondition;
    }

    public String getmDay(){
        return mDay;
    }

    public long getmDate(){
        return mDate.getTimeInMillis();
    }

    //**************setters****************
    public void setmTop(int top){
        mTop = top;
    }

    public void setmBottom(int bottom){
        mBottom = bottom;
    }

    public void setmHat(int hat){
        mHat = hat;
    }

    public void setmScarves(int scarf){
        mScarves = scarf;
    }

    public void setmOuterwear(int outerwear){
        mOuterwear = outerwear;
    }

    public void setmShoes(int shoes){
        mShoes = shoes;
    }

    public void setmGloves(int gloves){
        mGloves = gloves;
    }

    public void setmLocation(String location){
        mLocation = location;
    }

    public void setmCondition(String condition){
        mCondition = condition;
    }

    public void setmHigh(int high){
        mHigh = high;
    }

    public void setmLow(int low){
        mLow = low;
    }

    public void setmDate(long date){
        mDate.setTimeInMillis(date);
    }

    public void setmDay(String day){
        mDay = day;
    }

    public void setmId(long id){
        mId = id;
    }
}
