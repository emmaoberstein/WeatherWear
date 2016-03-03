package weatherwear.weatherwear.vacation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import weatherwear.weatherwear.R;

public class VacationCreatorActivity extends AppCompatActivity {

    private Calendar cal;
    private VacationModel mVacation;
    private static Button mStartButton, mEndButton;
    private boolean mHasStartDate;
    private static long mStartInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_creator_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.vacation_VacationCreator);
        setSupportActionBar(toolbar);
        mVacation = new VacationModel();
        mHasStartDate = false;
        mStartButton = (Button) findViewById(R.id.vacation_startDatePicker);
        mEndButton = (Button) findViewById(R.id.vacation_endDatePicker);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_button:
                Toast.makeText(getApplicationContext(), "Vacation deleted", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onStartDateClick(View view) {
        mHasStartDate = true;
        VacationDialogFragment frag = new VacationDialogFragment();
        frag.setDialogId(VacationDialogFragment.START_DATE_KEY);
        frag.show(getFragmentManager(), VacationDialogFragment.ID_KEY);
    }

    public void onEndDateClick(View view) {
        if(mHasStartDate) {
            VacationDialogFragment frag = new VacationDialogFragment();
            frag.setDialogId(VacationDialogFragment.END_DATE_KEY);
            frag.show(getFragmentManager(), VacationDialogFragment.ID_KEY);
        } else{
            Toast.makeText(getApplicationContext(), "Pick start date!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onGenerateClick(View view) {
        startActivity(new Intent(this, VacationOutfitsActivity.class));
    }

    public static void setStartButtonText(long time){
        mStartInMillis = time;
        mStartButton.setText("START DATE: " + parseDate(time));
    }

    public static void setEndButtonText(long time, Context context){
        if(!moreThanFive(time)) {
            mEndButton.setText("END DATE: " + parseDate(time));
        } else {
            Toast.makeText(context, "Currently only supports up to 5 days", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean moreThanFive(long endTime) {
         return (int) ((endTime - mStartInMillis) / (1000*60*60*24)) > 5;
    }

    public void onCancelClick(View view) {
        Toast.makeText(getApplicationContext(), getString(R.string.cancel_vacation), Toast.LENGTH_SHORT).show();
        finish(); }

    public VacationModel getVacation(){
        return mVacation;
    }

    // format milliseconds to something like 01.13.1995
    private static String parseDate(long msDate) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(msDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.d.yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
