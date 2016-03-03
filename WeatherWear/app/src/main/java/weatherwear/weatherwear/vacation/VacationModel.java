package weatherwear.weatherwear.vacation;

import java.util.Calendar;

/**
 * Created by emilylin27 on 3/1/16.
 */
public class VacationModel {
    private String mLocation;
    private Calendar mStartDate, mEndDate;

    public VacationModel(){
        mLocation="";
        mStartDate=Calendar.getInstance();
        mStartDate.setTimeInMillis(System.currentTimeMillis());
        mEndDate=Calendar.getInstance();
        mEndDate.setTimeInMillis(System.currentTimeMillis());
    }

    public String getLocation(){
        return mLocation;
    }

    public Calendar getStartDate(){
        return mStartDate;
    }

    public long getStartInMillis(){
        return mStartDate.getTimeInMillis();
    }

    public Calendar getEndDate(){
        return mEndDate;
    }

    public long getEndInMillis(){
        return mEndDate.getTimeInMillis();
    }

    public void setLocation(String location){
        mLocation = location;
    }

    public void setStartDate(long startInMilliseconds){
        mStartDate.setTimeInMillis(startInMilliseconds);
    }

    public void setStartDate(int month, int day, int year){
        mStartDate.set(Calendar.MONTH, month);
        mStartDate.set(Calendar.DAY_OF_MONTH, day);
        mStartDate.set(Calendar.YEAR, year);
    }

    public void setEndDate(int month, int day, int year){
        mEndDate.set(Calendar.MONTH, month);
        mEndDate.set(Calendar.DAY_OF_MONTH, day);
        mEndDate.set(Calendar.YEAR, year);
    }

    public void setEndDate(long endInMilliseconds){
        mEndDate.setTimeInMillis(endInMilliseconds);
    }
}
