package weatherwear.weatherwear.vacation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import weatherwear.weatherwear.R;
import weatherwear.weatherwear.Utils;

public class VacationCreatorActivity extends AppCompatActivity {
    public final static String START_KEY = "start";
    public final static String END_KEY = "end";
    public final static String ZIP_CODE_KEY = "zipcode";
    public final static String NAME_KEY = "name";
    public final static String HISTORY_KEY = "history";
    public final static String ID_KEY = "id";

    private static VacationModel mVacation;
    private static Button mStartButton, mEndButton;
    private boolean mFromHistory;
    private EditText mNameText, mZipCodeText;
    private static boolean mHasEndDate, mHasZipCode;
    private static long mStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_creator_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.vacation_VacationCreator);
        setSupportActionBar(toolbar);
        mVacation = new VacationModel();
        mStartButton = (Button) findViewById(R.id.vacation_startDatePicker);
        mEndButton = (Button) findViewById(R.id.vacation_endDatePicker);
        mNameText = (EditText) findViewById(R.id.vacation_createName);
        mZipCodeText = (EditText) findViewById(R.id.vacation_createId);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        mFromHistory = i.getBooleanExtra(HISTORY_KEY, false);
        if(mFromHistory){
            mVacation.setStartDate(extras.getLong(START_KEY));
            mVacation.setEndDate(extras.getLong(END_KEY));
            mVacation.setZipCode(extras.getString(ZIP_CODE_KEY));
            mVacation.setName(extras.getString(NAME_KEY));
            mVacation.setId(extras.getLong(ID_KEY));
            mNameText.setText(extras.getString(NAME_KEY));
            mZipCodeText.setText(extras.getString(ZIP_CODE_KEY));
            mZipCodeText.setEnabled(false);
            mHasEndDate = mHasZipCode = true;
            mEndButton.setText("END DATE: " + Utils.parseDate(mVacation.getEndInMillis()));
            mEndButton.setEnabled(false);
        } else {
            mVacation.setStartDate(System.currentTimeMillis());
            mHasEndDate = false;
            mHasZipCode = false;
        }
        mStartButton.setText("START DATE: " + Utils.parseDate(mVacation.getStartInMillis()));
        mStartTime = mVacation.getStartInMillis();
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
                if(mFromHistory) {
                    new VacationDatabaseHelper(this).removeEntry(mVacation.getId());
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onEndDateClick(View view) {
        VacationDialogFragment frag = new VacationDialogFragment();
        frag.setDialogId(VacationDialogFragment.END_DATE_KEY);
        frag.show(getFragmentManager(), VacationDialogFragment.ID_KEY);
    }

    public void onGenerateClick(View view) {
        String zipCodeInput = mZipCodeText.getText().toString();
        if(zipCodeInput.length()!=5){
            Toast.makeText(getApplicationContext(), "Zipcode must be 5 digits!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mHasZipCode = true;
        }
        String nameInput = mNameText.getText().toString();
        if(!(mHasEndDate && mHasZipCode && nameInput.length()!=0)){
            Toast.makeText(getApplicationContext(), "Incomplete input!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mVacation.setZipCode(mZipCodeText.getText().toString());
            mVacation.setName(mNameText.getText().toString());
            Intent intent = new Intent(this, VacationOutfitsActivity.class);
            intent.putExtra(VacationOutfitsActivity.HISTORY_KEY,mFromHistory);
            intent.putExtra(VacationOutfitsActivity.ID_KEY, mVacation.getId());
            intent.putExtra(VacationOutfitsActivity.NAME_KEY, mVacation.getName());
            intent.putExtra(VacationOutfitsActivity.ZIPCODE_KEY, mVacation.getZipCode());
            intent.putExtra(VacationOutfitsActivity.START_KEY, mVacation.getStartInMillis());
            intent.putExtra(VacationOutfitsActivity.END_KEY, mVacation.getEndInMillis());
            intent.putExtra(VacationOutfitsActivity.DAYS_KEY,
                    Utils.getNumDays(mVacation.getStartInMillis(), mVacation.getEndInMillis()));
            startActivity(intent);
            finish();
        }
    }

    public static void setEndButtonText(long time, Context context){
        if((Utils.getNumDays(mStartTime,time)>5)) {
            Toast.makeText(context, "Currently only supports up to 5 days", Toast.LENGTH_SHORT).show();
        } else if ((Utils.getNumDays(mStartTime,time)<0)){
            Toast.makeText(context, "End date before start date!", Toast.LENGTH_SHORT).show();
        } else {
            mEndButton.setText("END DATE: " + Utils.parseDate(time));
            mVacation.setEndDate(time);
            mHasEndDate = true;
        }
    }


    public void onCancelClick(View view) {
        Toast.makeText(getApplicationContext(), getString(R.string.cancel_vacation), Toast.LENGTH_SHORT).show();
        finish();
    }

}
