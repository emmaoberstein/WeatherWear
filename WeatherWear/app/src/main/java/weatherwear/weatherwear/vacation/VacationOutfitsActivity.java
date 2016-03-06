package weatherwear.weatherwear.vacation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import weatherwear.weatherwear.MainActivity;
import weatherwear.weatherwear.R;
import weatherwear.weatherwear.Utils;

public class VacationOutfitsActivity extends AppCompatActivity {
    public static final String ZIPCODE_KEY = "zip";
    public static final String START_KEY = "start";
    public static final String END_KEY = "end";
    public static final String DAYS_KEY = "days";
    public static final String NAME_KEY = "name";
    public static final String HISTORY_KEY = "history";
    public static final String ID_KEY ="id";
    private ArrayList<String> mDays = new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;
    private String mZipCode, mName;
    private long mStart, mEnd, mId;
    private int mNumDays;
    private VacationDatabaseHelper mDbHelper;
    private boolean mFromHistory;
    private VacationModel mVacation;

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

            }
        });
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
