package weatherwear.weatherwear.vacation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import weatherwear.weatherwear.DisplayOutfitActivity;
import weatherwear.weatherwear.MainActivity;
import weatherwear.weatherwear.NewOutfitActivity;
import weatherwear.weatherwear.R;
import weatherwear.weatherwear.Utils;

/**
 * Displays the different vacation days you can choose to create an outfit
 */
public class VacationOutfitsActivity extends AppCompatActivity {
    // keys
    public static final String ZIPCODE_KEY = "zip";
    public static final String START_DAY = "start_day";
    public static final String START_KEY = "start";
    public static final String END_KEY = "end";
    public static final String DAYS_KEY = "days";
    public static final String NAME_KEY = "name";
    public static final String HISTORY_KEY = "history";
    public static final String ID_KEY ="id";
    public static final String VACATION_KEY = "vacation";
    public static final String DAY_ONE_KEY = "one";
    public static final String DAY_TWO_KEY = "two";
    public static final String DAY_THREE_KEY = "three";
    public static final String DAY_FOUR_KEY = "four";
    public static final String DAY_FIVE_KEY = "five";
    // array of days of vacation - day 1, day 2...day 5
    private ArrayList<String> mDays = new ArrayList<String>();
    // member variables
    private ArrayAdapter<String> mAdapter;
    private String mZipCode, mName;
    private long mStart, mEnd, mId;
    private int mNumDays;
    private VacationDatabaseHelper mDbHelper;
    private boolean mFromHistory;
    private static VacationModel mVacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Instantiate the view and set the title bar text
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_outfits_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.vacation_VacationOutfit);
        setSupportActionBar(toolbar);

        // Set up all of the local activities
        mDbHelper = new VacationDatabaseHelper(this);
        mVacation = new VacationModel();

        // Set variables according to the vacation
        Intent intent = getIntent();
        mFromHistory = intent.getBooleanExtra(HISTORY_KEY, false);
        if(mFromHistory){
            mId=intent.getLongExtra(ID_KEY, 0);
            mVacation.setId(mId);
        }
        mName = intent.getStringExtra(NAME_KEY);
        mZipCode = intent.getStringExtra(ZIPCODE_KEY);
        mStart = intent.getLongExtra(START_KEY, System.currentTimeMillis());
        mEnd = intent.getLongExtra(END_KEY, System.currentTimeMillis());
        mNumDays = intent.getIntExtra(DAYS_KEY, 1);
        mDays.clear();
        mVacation.setName(mName);
        mVacation.setZipCode(mZipCode);
        mVacation.setStartDate(mStart);
        mVacation.setEndDate(mEnd);
        mVacation.setDayOne(intent.getLongExtra(DAY_ONE_KEY, -1));
        mVacation.setDayTwo(intent.getLongExtra(DAY_TWO_KEY, -1));
        mVacation.setDayThree(intent.getLongExtra(DAY_THREE_KEY, -1));
        mVacation.setDayFour(intent.getLongExtra(DAY_FOUR_KEY, -1));
        mVacation.setDayFive(intent.getLongExtra(DAY_FIVE_KEY, -1));

        // Fills mDays with appropriate number of vacation days
        if(mNumDays == 0){
            mDays.add("DAY 1");
        }
        for(int j = 1; j <= mNumDays; j++){
            mDays.add("DAY " + Integer.toString(j));
        }

        // Sets vacation name and dates
        TextView vacationinfo = (TextView) findViewById(R.id.vacation_info);
        vacationinfo.setText(mName + "\n" + Utils.parseDate(mStart) + " ~ " + Utils.parseDate(mEnd));

        // embedded listview to select outfit for each vacation day
        mAdapter = new ArrayAdapter<String>(this,
                R.layout.vacation_outfits_list_layout, mDays);
        ListView listView = (ListView) findViewById(R.id.embedded_ListView);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // intent to start new outfit - can edit
                Intent newOutfitIntent = new Intent(view.getContext(), NewOutfitActivity.class);
                newOutfitIntent.putExtra(START_DAY, mVacation.getStartInMillis());
                newOutfitIntent.putExtra(ZIPCODE_KEY, mZipCode);
                newOutfitIntent.putExtra(VACATION_KEY, true);
                newOutfitIntent.putExtra(ID_KEY, mVacation.getId());

                // intent to start display outfit - outfit already chosen
                Intent displayOutfitIntent = new Intent(view.getContext(), DisplayOutfitActivity.class);
                displayOutfitIntent.putExtra(START_DAY, mVacation.getStartInMillis());
                displayOutfitIntent.putExtra(ZIPCODE_KEY, mZipCode);
                displayOutfitIntent.putExtra(ID_KEY, mVacation.getId());
                // position indicates what day of vacation
                switch(position){
                    case 0:
                        // -1 indicates there is no saved outfit - launch new outfit activity
                        // otherwise display the saved outfit
                        if(mVacation.getDayOne()==-1){
                            // gets what day of vacation in relation to vacation start date and today's date
                            int day = Utils.getWhichDay(mVacation.getStartInMillis(), position);
                            // can't create outfit for a vacation day that is in the past
                            if(day == -1){
                                Toast.makeText(getApplicationContext(), "Day passed!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                newOutfitIntent.putExtra(DAYS_KEY, day);
                            }
                            startActivity(newOutfitIntent);
                        } else {
                            displayOutfitIntent.putExtra(ID_KEY, mVacation.getDayOne());
                            startActivity(displayOutfitIntent);
                        }
                        break;
                    case 1:
                        // -1 indicates there is no saved outfit - launch new outfit activity
                        // otherwise display the saved outfit
                        if(mVacation.getDayTwo() == -1) {
                            // gets what day of vacation in relation to vacation start date and today's date
                            int day = Utils.getWhichDay(mVacation.getStartInMillis(), position);
                            // can't create outfit for a vacation day that is in the past
                            if(day == -1){
                                Toast.makeText(getApplicationContext(), "Day passed!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                newOutfitIntent.putExtra(DAYS_KEY, day);
                            }
                            startActivity(newOutfitIntent);
                        } else {
                            displayOutfitIntent.putExtra(ID_KEY, mVacation.getDayTwo());
                            startActivity(displayOutfitIntent);
                        }
                        break;
                    case 2:
                        // -1 indicates there is no saved outfit - launch new outfit activity
                        // otherwise display the saved outfit
                        if(mVacation.getDayThree() == -1){
                            // gets what day of vacation in relation to vacation start date and today's date
                            int day = Utils.getWhichDay(mVacation.getStartInMillis(), position);
                            // can't create outfit for a vacation day that is in the past
                            if(day == -1){
                                Toast.makeText(getApplicationContext(), "Day passed!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                newOutfitIntent.putExtra(DAYS_KEY, day);
                            }
                            startActivity(newOutfitIntent);
                        } else {
                            displayOutfitIntent.putExtra(ID_KEY, mVacation.getDayThree());
                            startActivity(displayOutfitIntent);
                        }
                        break;
                    case 3:
                        // -1 indicates there is no saved outfit - launch new outfit activity
                        // otherwise display the saved outfit
                        if(mVacation.getDayFour() == -1){
                            // gets what day of vacation in relation to vacation start date and today's date
                            int day = Utils.getWhichDay(mVacation.getStartInMillis(), position);
                            // can't create outfit for a vacation day that is in the past
                            if(day == -1){
                                Toast.makeText(getApplicationContext(), "Day passed!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                newOutfitIntent.putExtra(DAYS_KEY, day);
                            }
                            startActivity(newOutfitIntent);
                        } else {
                            displayOutfitIntent.putExtra(ID_KEY, mVacation.getDayFour());
                            startActivity(displayOutfitIntent);
                        }
                        break;
                    case 4:
                        // -1 indicates there is no saved outfit - launch new outfit activity
                        // otherwise display the saved outfit
                        if(mVacation.getDayFive() == -1){
                            // gets what day of vacation in relation to vacation start date and today's date
                            int day = Utils.getWhichDay(mVacation.getStartInMillis(), position);
                            // can't create outfit for a vacation day that is in the past
                            if(day == -1){
                                Toast.makeText(getApplicationContext(), "Day passed!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                newOutfitIntent.putExtra(DAYS_KEY, day);
                            }
                            startActivity(newOutfitIntent);
                        }
                        else {
                            displayOutfitIntent.putExtra(ID_KEY, mVacation.getDayFive());
                            startActivity(displayOutfitIntent);
                        }
                        break;
                    default:
                        break;
                }

            }
        });
    }

    // returns vacation
    public static VacationModel getVacation(){ return mVacation; }

    public void onCancel(View view) {
        finish();
    }

    public void onSave(View view) {
        new InsertData().execute(mVacation);
    }

    // Inserts into database
    private class InsertData extends AsyncTask<VacationModel, Void, Void> {
        @Override
        protected Void doInBackground(VacationModel... args) {
            if(mFromHistory){
                mDbHelper.onUpdate(args[0]);
            } else {
                mDbHelper.insertVacation(args[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Vacation saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
