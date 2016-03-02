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

    public Calendar getEndDate(){
        return mEndDate;
    }

    public void setLocation(String location){
        mLocation = location;
    }

    public void setStartDate(){}

    public void setEndDate(){}
}
