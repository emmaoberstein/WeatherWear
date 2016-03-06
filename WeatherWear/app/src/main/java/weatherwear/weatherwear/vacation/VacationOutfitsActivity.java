package weatherwear.weatherwear.vacation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class VacationOutfitsActivity extends AppCompatActivity {
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

    private ArrayList<String> mDays = new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;
    private String mZipCode, mName;
    private long mStart, mEnd, mId;
    private int mNumDays;
    private VacationDatabaseHelper mDbHelper;
    private boolean mFromHistory;
    private static VacationModel mVacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_outfits_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.vacation_VacationOutfit);

        mDbHelper = new VacationDatabaseHelper(this);
        mVacation = new VacationModel();

        Intent i = getIntent();
        mFromHistory = i.getBooleanExtra(HISTORY_KEY, false);
        if(mFromHistory){
            mId=i.getLongExtra(ID_KEY, 0);
            mVacation.setId(mId);
        }
        mName = i.getStringExtra(NAME_KEY);
        mZipCode = i.getStringExtra(ZIPCODE_KEY);
        mStart = i.getLongExtra(START_KEY, System.currentTimeMillis());
        mEnd = i.getLongExtra(END_KEY, System.currentTimeMillis());
        mNumDays = i.getIntExtra(DAYS_KEY, 1);
        mDays.clear();
        mVacation.setName(mName);
        mVacation.setZipCode(mZipCode);
        mVacation.setStartDate(mStart);
        mVacation.setEndDate(mEnd);
        mVacation.setDayOne(i.getLongExtra(DAY_ONE_KEY, -1));
        mVacation.setDayTwo(i.getLongExtra(DAY_TWO_KEY, -1));
        mVacation.setDayThree(i.getLongExtra(DAY_THREE_KEY, -1));
        mVacation.setDayFour(i.getLongExtra(DAY_FOUR_KEY, -1));
        mVacation.setDayFive(i.getLongExtra(DAY_FIVE_KEY, -1));
        if(mNumDays == 0){
            mDays.add("DAY 1");
        }
        for(int j = 1; j <= mNumDays; j++){
            mDays.add("DAY " + Integer.toString(j));
        }

        TextView vacationinfo = (TextView) findViewById(R.id.vacation_info);
        vacationinfo.setText(mName + "\n" + Utils.parseDate(mStart) + " ~ " + Utils.parseDate(mEnd));

        mAdapter = new ArrayAdapter<String>(this,
                R.layout.vacation_outfits_list_layout, mDays);
        ListView listView = (ListView) findViewById(R.id.embedded_ListView);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        if(mVacation.getDayOne()==-1){
                            Intent i = new Intent(view.getContext(), NewOutfitActivity.class);
                            i.putExtra(START_DAY, mVacation.getStartInMillis());
                            i.putExtra(ZIPCODE_KEY, mZipCode);
                            i.putExtra(VACATION_KEY, true);
                            i.putExtra(ID_KEY, mVacation.getId());
                            int day = Utils.getWhichDay(mVacation.getStartInMillis(), position);
                            Log.d("VacationOutfitsLogd", "" + day);
                            if(day == -1){
                                Toast.makeText(getApplicationContext(), "Day passed!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                i.putExtra(DAYS_KEY, day);
                            }
                            startActivity(i);
                        } else {
                            Intent i = new Intent(view.getContext(), DisplayOutfitActivity.class);
                            i.putExtra(ID_KEY, mVacation.getDayOne());
                            startActivity(i);
                        }
                        break;
                    case 1:
                        if(mVacation.getDayTwo() == -1) {
                            Intent i = new Intent(view.getContext(), NewOutfitActivity.class);
                            i.putExtra(START_DAY, mVacation.getStartInMillis());
                            i.putExtra(ZIPCODE_KEY, mZipCode);
                            i.putExtra(VACATION_KEY, true);
                            int day = Utils.getWhichDay(mVacation.getStartInMillis(), position);
                            Log.d("VacationOutfitsLogd", "" + day);
                            if(day == -1){
                                Toast.makeText(getApplicationContext(), "Day passed!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                i.putExtra(DAYS_KEY, day);
                            }
                            startActivity(i);
                        } else {
                            Intent i = new Intent(view.getContext(), DisplayOutfitActivity.class);
                            i.putExtra(ID_KEY, mVacation.getDayTwo());
                            startActivity(i);
                        }
                        break;
                    case 2:
                        if(mVacation.getDayThree() == -1){
                            Intent i = new Intent(view.getContext(), NewOutfitActivity.class);
                            i.putExtra(START_DAY, mVacation.getStartInMillis());
                            i.putExtra(ZIPCODE_KEY, mZipCode);
                            i.putExtra(VACATION_KEY, true);
                            int day = Utils.getWhichDay(mVacation.getStartInMillis(), position);
                            Log.d("VacationOutfitsLogd", "" + day);
                            if(day == -1){
                                Toast.makeText(getApplicationContext(), "Day passed!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                i.putExtra(DAYS_KEY, day);
                            }
                            startActivity(i);
                        } else {
                            Intent i = new Intent(view.getContext(), DisplayOutfitActivity.class);
                            i.putExtra(ID_KEY, mVacation.getDayThree());
                            startActivity(i);
                        }
                        break;
                    case 3:
                        if(mVacation.getDayFour() == -1){
                            Intent i = new Intent(view.getContext(), NewOutfitActivity.class);
                            i.putExtra(START_DAY, mVacation.getStartInMillis());
                            i.putExtra(ZIPCODE_KEY, mZipCode);
                            i.putExtra(VACATION_KEY, true);
                            int day = Utils.getWhichDay(mVacation.getStartInMillis(), position);
                            Log.d("VacationOutfitsLogd", "" + day);
                            if(day == -1){
                                Toast.makeText(getApplicationContext(), "Day passed!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                i.putExtra(DAYS_KEY, day);
                            }
                            startActivity(i);
                        } else {
                            Intent i = new Intent(view.getContext(), DisplayOutfitActivity.class);
                            i.putExtra(ID_KEY, mVacation.getDayFour());
                            startActivity(i);
                        }
                        break;
                    case 4:
                        if(mVacation.getDayFive() == -1){
                            Intent i = new Intent(view.getContext(), NewOutfitActivity.class);
                            i.putExtra(START_DAY, mVacation.getStartInMillis());
                            i.putExtra(ZIPCODE_KEY, mZipCode);
                            i.putExtra(VACATION_KEY, true);
                            int day = Utils.getWhichDay(mVacation.getStartInMillis(), position);
                            Log.d("VacationOutfitsLogd", "" + day);
                            if(day == -1){
                                Toast.makeText(getApplicationContext(), "Day passed!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                i.putExtra(DAYS_KEY, day);
                            }
                            startActivity(i);
                        }
                        else {
                            Intent i = new Intent(view.getContext(), DisplayOutfitActivity.class);
                            i.putExtra(ID_KEY, mVacation.getDayFive());
                            startActivity(i);
                        }
                        break;
                    default:
                        break;
                }

            }
        });
    }

    public static VacationModel getVacation(){
        return mVacation;
    }



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
