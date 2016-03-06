package weatherwear.weatherwear.vacation;

import java.util.Calendar;

/**
 * Created by emilylin27 on 3/6/16.
 */
public class OutfitModel {
    private long mTop, mBottom, mScarves, mHat, mGloves, mOuterwear, mShoes, mHigh, mLow;
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

    public long getmTop(){
        return mTop;
    }

    public long getmBottom(){
        return mBottom;
    }

    public long getmScarves(){
        return mScarves;
    }

    public long getmHat(){
        return mHat;
    }

    public long getmGloves(){
        return mGloves;
    }

    public long getmOuterwear(){
        return mOuterwear;
    }

    public long getmShoes(){
        return mShoes;
    }

    public long getmHigh(){
        return mHigh;
    }

    public long getmLow(){
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
    public void setmTop(long top){
        mTop = top;
    }

    public void setmBottom(long bottom){
        mBottom = bottom;
    }

    public void setmHat(long hat){
        mHat = hat;
    }

    public void setmScarves(long scarf){
        mScarves = scarf;
    }

    public void setmOuterwear(long outerwear){
        mOuterwear = outerwear;
    }

    public void setmShoes(long shoes){
        mShoes = shoes;
    }

    public void setmGloves(long gloves){
        mGloves = gloves;
    }

    public void setmLocation(String location){
        mLocation = location;
    }

    public void setmCondition(String condition){
        mCondition = condition;
    }

    public void setmHigh(long high){
        mHigh = high;
    }

    public void setmLow(long low){
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
