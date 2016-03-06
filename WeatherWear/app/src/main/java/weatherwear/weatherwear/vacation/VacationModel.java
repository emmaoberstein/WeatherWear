package weatherwear.weatherwear.vacation;

import java.util.Calendar;

/**
 * Created by emilylin27 on 3/1/16.
 */
public class VacationModel {
    private String mZipCode, mName;
    private Calendar mStartDate, mEndDate;
    private long mId;
    private long mDayOne, mDayTwo, mDayThree, mDayFour, mDayFive;

    public VacationModel(){
        mName="";
        mZipCode ="";
        mStartDate=Calendar.getInstance();
        mStartDate.setTimeInMillis(System.currentTimeMillis());
        mEndDate=Calendar.getInstance();
        mEndDate.setTimeInMillis(System.currentTimeMillis());
        mDayFive = mDayFour = mDayOne = mDayTwo = mDayThree = -1;
    }

    //*********Getters*******

    public long getDayOne(){
        return mDayOne;
    }

    public long getDayTwo(){
        return mDayTwo;
    }

    public long getDayThree(){
        return mDayThree;
    }

    public long getDayFour(){
        return mDayFour;
    }

    public long getDayFive(){
        return mDayFive;
    }

    public long getId(){
        return mId;
    }

    public String getName(){ return mName;}

    public String getZipCode(){
        return mZipCode;
    }

    public long getStartInMillis(){
        return mStartDate.getTimeInMillis();
    }

    public long getEndInMillis(){
        return mEndDate.getTimeInMillis();
    }

    //********setters******

    public void setZipCode(String zipCode){
        mZipCode = zipCode;
    }

    public void setStartDate(long startInMilliseconds){
        mStartDate.setTimeInMillis(startInMilliseconds);
    }

    public void setId(long id){
        mId = id;
    }

    public void setName(String name){
        mName = name;
    }

    public void setEndDate(long endInMilliseconds){
        mEndDate.setTimeInMillis(endInMilliseconds);
    }

    public void setDayOne(long id){
        mDayOne = id;
    }

    public void setDayTwo(long id){
        mDayTwo = id;
    }

    public void setDayThree(long id){
        mDayThree = id;
    }

    public void setDayFour(long id){
        mDayFour = id;
    }

    public void setDayFive(long id){
        mDayFive = id;
    }
}
