package weatherwear.weatherwear.vacation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import weatherwear.weatherwear.R;
import weatherwear.weatherwear.Utils;

public class VacationCreatorActivity extends AppCompatActivity {

    private VacationModel mVacation;
    private static Button mStartButton, mEndButton;
    private EditText mNameText, mZipCodeText;
    private static boolean mHasStartDate, mHasEndDate, mHasLocation, mHasName;
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
        mHasEndDate = false;
        mHasLocation = false;
        mHasName = false;
        mStartButton = (Button) findViewById(R.id.vacation_startDatePicker);
        mEndButton = (Button) findViewById(R.id.vacation_endDatePicker);
        mNameText = (EditText) findViewById(R.id.vacation_createName);
        mNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mHasName = true;
            }
        });
        mZipCodeText = (EditText) findViewById(R.id.vacation_createId);
        mZipCodeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mHasLocation = true;
            }
        });

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
        if(!(mHasEndDate && mHasLocation && mHasStartDate && mHasName)){
            Toast.makeText(getApplicationContext(), "Incomplete input!", Toast.LENGTH_SHORT).show();
        } else {
            mVacation.setZipCode(mZipCodeText.getText().toString());
            mVacation.setName(mNameText.getText().toString());
            Intent intent = new Intent(this, VacationOutfitsActivity.class);
            intent.putExtra(VacationOutfitsActivity.NAME_KEY, mVacation.getName());
            intent.putExtra(VacationOutfitsActivity.LOCATION_KEY, mVacation.getZipCode());
            intent.putExtra(VacationOutfitsActivity.START_KEY, parseDate(mVacation.getStartInMillis()));
            intent.putExtra(VacationOutfitsActivity.END_KEY, parseDate(mVacation.getEndInMillis()));
            intent.putExtra(VacationOutfitsActivity.DAYS_KEY,getNumDays(mVacation.getEndInMillis()));
            startActivity(intent);
        }
    }

    public static void setStartButtonText(long time){
        mHasStartDate = true;
        mStartInMillis = time;
        mStartButton.setText("START DATE: " + Utils.parseDate(time));
    }

    public static void setEndButtonText(long time, Context context){
        if(!moreThanFive(time)) {
            mEndButton.setText("END DATE: " + Utils.parseDate(time));
            mHasEndDate = true;
        } else {
            Toast.makeText(context, "Currently only supports up to 5 days", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean moreThanFive(long endTime) {
         return (int) ((endTime - mStartInMillis) / (1000*60*60*24)) > 5;
    }

    private int getNumDays(long endTime){
        return (int) ((endTime - mStartInMillis) / (1000*60*60*24));
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
