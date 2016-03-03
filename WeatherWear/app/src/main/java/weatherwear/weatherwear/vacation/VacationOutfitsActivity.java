package weatherwear.weatherwear.vacation;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.jar.Attributes;

import weatherwear.weatherwear.MainActivity;
import weatherwear.weatherwear.R;

public class VacationOutfitsActivity extends AppCompatActivity {
    public static final String LOCATION_KEY = "loc";
    public static final String START_KEY = "start";
    public static final String END_KEY = "end";
    public static final String DAYS_KEY = "days";
    public static final String NAME_KEY = "name";

    private ArrayList<String> mDays = new ArrayList<String>();
    ArrayAdapter<String> mAdapter;
    private String mLocation, mStart, mEnd, mName;
    private int mNumDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_outfits_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.vacation_VacationOutfit);

        Intent i = getIntent();
        mName = i.getStringExtra(NAME_KEY);
        mLocation = i.getStringExtra(LOCATION_KEY);
        mStart = i.getStringExtra(START_KEY);
        mEnd = i.getStringExtra(END_KEY);
        mNumDays = i.getIntExtra(DAYS_KEY, 1);
        mDays.clear();
        for(int j = 1; j <= mNumDays; j++){
            mDays.add("DAY " + Integer.toString(j));
        }

        TextView vacationinfo = (TextView) findViewById(R.id.vacation_info);
        vacationinfo.setText(mName + "\n" + mStart + " ~ " + mEnd);

        mAdapter = new ArrayAdapter<String>(this,
                R.layout.vacation_outfits_list_layout, mDays);
        ListView listView = (ListView) findViewById(R.id.embedded_ListView);
        listView.setAdapter(mAdapter);
    }

    public void onCancel(View view) {
        finish();
    }

    public void onSave(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
