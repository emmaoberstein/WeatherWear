package weatherwear.weatherwear.vacation;

import java.util.Calendar;

/**
 * Created by emilylin27 on 3/1/16.
 */
public class VacationModel {
    private String mZipCode, mName;
    private Calendar mStartDate, mEndDate;
    private long mId;

    public VacationModel(){
        mName="";
        mZipCode ="";
        mStartDate=Calendar.getInstance();
        mStartDate.setTimeInMillis(System.currentTimeMillis());
        mEndDate=Calendar.getInstance();
        mEndDate.setTimeInMillis(System.currentTimeMillis());
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

    public void setEndDate(int month, int day, int year){
        mEndDate.set(Calendar.MONTH, month);
        mEndDate.set(Calendar.DAY_OF_MONTH, day);
        mEndDate.set(Calendar.YEAR, year);
    }

    public void setEndDate(long endInMilliseconds){
        mEndDate.setTimeInMillis(endInMilliseconds);
    }
}
