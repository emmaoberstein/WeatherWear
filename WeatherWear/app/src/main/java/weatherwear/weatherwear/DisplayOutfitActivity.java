package weatherwear.weatherwear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import weatherwear.weatherwear.clothing.ClothingDatabaseHelper;
import weatherwear.weatherwear.clothing.ClothingItem;
import weatherwear.weatherwear.vacation.OutfitDatabaseHelper;
import weatherwear.weatherwear.vacation.OutfitModel;
import weatherwear.weatherwear.vacation.VacationOutfitsActivity;

/*
 * Created by Emma on 3/6/16.
 * Displays previously created outfits (no changing items)
 */
public class DisplayOutfitActivity extends AppCompatActivity {
    public static final String KEY_DISPLAY = "display";
    public static final String ID_KEY = "id";
    OutfitDatabaseHelper mOutfitDbHelper = new OutfitDatabaseHelper(this);
    OutfitModel mOutfit;
    private String mZipcode;
    private long mStart, mId;
    private boolean mFromVacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Instantiates the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_outfit_fragment);
        // Get the data from the passed outfit, and generate it
        Intent i = getIntent();
        mOutfit = mOutfitDbHelper.fetchOutfitByIndex(i.getLongExtra(VacationOutfitsActivity.ID_KEY, 1));
        mZipcode = i.getStringExtra(VacationOutfitsActivity.ZIPCODE_KEY);
        mStart = i.getLongExtra(VacationOutfitsActivity.START_DAY, System.currentTimeMillis());
        mId = i.getLongExtra(VacationOutfitsActivity.ID_KEY, -1);
        mFromVacation = i.getBooleanExtra(VacationOutfitsActivity.VACATION_KEY, false);
        // Set the title text
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Day " + mOutfit.getmDay() + " Outfit");
    }

    // Set up menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_menu, menu);
        return true;
    }

    // Handle the 'new' button being clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_button:
                // Create an intent NewOutfitActivity
                Intent i = new Intent(this, NewOutfitActivity.class);
                i.putExtra(KEY_DISPLAY, true);
                i.putExtra(VacationOutfitsActivity.ZIPCODE_KEY,mZipcode);
                i.putExtra(VacationOutfitsActivity.START_DAY, mStart);
                i.putExtra(VacationOutfitsActivity.VACATION_KEY, mFromVacation);
                i.putExtra(ID_KEY, mOutfit.getmId());
                i.putExtra(VacationOutfitsActivity.DAYS_KEY, Integer.parseInt(mOutfit.getmDay()) -1);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        // set the user information text from the given outfit
        String day = mOutfit.getmDay();
        ((TextView) findViewById(R.id.welcome)).setText("Outfit for Day " + day);

        String date = Utils.parseVacationDate(mOutfit.getmDate());
        if (date != null) ((TextView) findViewById(R.id.outfit_date)).setText("Outfit date: " + date);

        String location = mOutfit.getmLocation();
        if (location != null) ((TextView) findViewById(R.id.location)).setText("Location: " + location);

        String mKey = getString(R.string.preference_name);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        // display the high in Celsius or Fahrenheit
        Long high = mOutfit.getmHigh();
        if (high != -500) {
            if (!sp.getString(PreferenceFragment.PREFERENCE_VALUE_TEMP,"-1").equals("Celsius")) {
                ((TextView) (findViewById(R.id.high))).setText("High: " + high + "°F");
            } else {
                ((TextView) (findViewById(R.id.high))).setText("Low: " +  String.valueOf(Math.round((((Double.valueOf(high)-32)*5/9)) * 10) / 10) + "°C");
            }
        }

        // display the low in Celsius or Fahrenheit
        Long low = mOutfit.getmLow();
        if (low != -500) {
            if (!sp.getString(PreferenceFragment.PREFERENCE_VALUE_TEMP,"-1").equals("Celsius")) {
                ((TextView) (findViewById(R.id.low))).setText("Low: " + low + "°F");
            } else {
                ((TextView) (findViewById(R.id.low))).setText("Low: " +  String.valueOf(Math.round((((Double.valueOf(low)-32)*5/9)) * 10) / 10) + "°C");
            }
        }


        String condition = mOutfit.getmCondition();
        if (condition != null) ((TextView) findViewById(R.id.condition)).setText("Condition: " + condition);

        // load the pictures for the outfit
        new LoadOutfitAsyncTask().execute(mOutfit.getmTop(), mOutfit.getmBottom(), mOutfit.getmShoes(),
                mOutfit.getmOuterwear(), mOutfit.getmGloves(), mOutfit.getmHat(), mOutfit.getmScarves());
    }

    // Async task for loading the outfit pictures
    private class LoadOutfitAsyncTask extends AsyncTask<Long, Void, ArrayList<ClothingItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<ClothingItem> clothes) {
            super.onPostExecute(clothes);

            findViewById(R.id.outfit_description).setVisibility(View.VISIBLE);

            // set the top picture or set view as invisible
            if (clothes.get(0) != null)  {
                ((findViewById(R.id.top))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.top_image))).setImageBitmap(clothes.get(0).getImage());
                ((findViewById(R.id.top_group))).setVisibility(View.VISIBLE);
            } else {
                ((findViewById(R.id.top))).setVisibility(View.GONE);
                ((findViewById(R.id.top_group))).setVisibility(View.GONE);
            }

            // set the bottom picture or set view as invisible
            if (clothes.get(1) != null)  {
                ((findViewById(R.id.bottom))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.bottom_image))).setImageBitmap(clothes.get(1).getImage());
                ((findViewById(R.id.bottom_group))).setVisibility(View.VISIBLE);
            }else {
                ((findViewById(R.id.bottom))).setVisibility(View.GONE);
                ((findViewById(R.id.bottom_group))).setVisibility(View.GONE);
            }

            // set the shoes picture or set view as invisible
            if (clothes.get(2) != null)  {
                ((findViewById(R.id.shoes))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.shoes_image))).setImageBitmap(clothes.get(2).getImage());
                ((findViewById(R.id.shoes_group))).setVisibility(View.VISIBLE);
            } else {
                ((findViewById(R.id.shoes))).setVisibility(View.GONE);
                ((findViewById(R.id.shoes_group))).setVisibility(View.GONE);
            }

            // set the outerwear picture or set view as invisible
            if (clothes.get(3) != null)  {
                ((findViewById(R.id.outerwear))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.outerwear_image))).setImageBitmap(clothes.get(3).getImage());
                ((findViewById(R.id.outerwear_group))).setVisibility(View.VISIBLE);
            } else {
                ((findViewById(R.id.outerwear))).setVisibility(View.GONE);
                ((findViewById(R.id.outerwear_group))).setVisibility(View.GONE);
            }

            // set the gloves picture or set view as invisible
            if (clothes.get(4) != null)  {
                ((findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.gloves_image))).setImageBitmap(clothes.get(4).getImage());
                ((findViewById(R.id.gloves_group))).setVisibility(View.VISIBLE);
            } else {
                ((findViewById(R.id.gloves_group))).setVisibility(View.GONE);
            }

            // set the hats picture or set view as invisible
            if (clothes.get(5) != null)  {
                ((findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.hats_image))).setImageBitmap(clothes.get(5).getImage());
                ((findViewById(R.id.hats_group))).setVisibility(View.VISIBLE);
            } else {
                ((findViewById(R.id.hats_group))).setVisibility(View.GONE);
            }

            // set the scarves picture or set view as invisible
            if (clothes.get(6) != null)  {
                ((findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.scarves_image))).setImageBitmap(clothes.get(6).getImage());
                ((findViewById(R.id.scarves_group))).setVisibility(View.VISIBLE);
            } else {
                ((findViewById(R.id.scarves_group))).setVisibility(View.GONE);
            }

            // if there are no accessories, set accessories view as invisible
            if (clothes.get(4) == null && clothes.get(5) == null && clothes.get(6) == null) {
                ((findViewById(R.id.accessories))).setVisibility(View.GONE);
            }
        }

        @Override
        protected ArrayList<ClothingItem> doInBackground(Long... params) {
            ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getApplicationContext());
            ArrayList<ClothingItem> clothes = new ArrayList<ClothingItem>();

            // for each parameter check if index is -1, if not add clothing item to clothes
            if (params[0] != -1) {
                clothes.add(dbHelper.fetchItemByIndex(params[0]));
            } else clothes.add(null);

            if (params[1] != -1) {
                clothes.add(dbHelper.fetchItemByIndex(params[1]));
            } else clothes.add(null);
            
            if (params[2] != -1) {
                clothes.add(dbHelper.fetchItemByIndex(params[2]));
            } else clothes.add(null);

            if (params[3] != -1) {
                clothes.add(dbHelper.fetchItemByIndex(params[3]));
            } else clothes.add(null);

            if (params[4] != -1) {
                clothes.add(dbHelper.fetchItemByIndex(params[4]));
            } else clothes.add(null);

            if (params[5] != -1) {
                clothes.add(dbHelper.fetchItemByIndex(params[5]));
            } else clothes.add(null);

            if (params[6] != -1) {
                clothes.add(dbHelper.fetchItemByIndex(params[6]));
            } else clothes.add(null);

            // return the ArrayList of clothes to onPostExecute
            return clothes;
        }
    }
}
