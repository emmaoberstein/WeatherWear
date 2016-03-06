package weatherwear.weatherwear;

import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import weatherwear.weatherwear.alarm.AlarmAlertManager;
import weatherwear.weatherwear.database.ClothingDatabaseHelper;
import weatherwear.weatherwear.database.ClothingItem;
import weatherwear.weatherwear.vacation.OutfitDatabaseHelper;
import weatherwear.weatherwear.vacation.OutfitModel;
import weatherwear.weatherwear.vacation.VacationDatabaseHelper;
import weatherwear.weatherwear.vacation.VacationModel;
import weatherwear.weatherwear.vacation.VacationOutfitsActivity;

/**
 * Created by Emma on 2/16/16.
 */
public class NewOutfitActivity extends AppCompatActivity {

    private ArrayList<String> mWeatherArray;
    private ArrayList<ClothingItem> mTops, mBottoms, mShoes, mOuterwear, mScarves, mGloves, mHats;
    private int mTopIndex = -1, mBottomIndex = -1, mShoesIndex = -1, mOuterwearIndex = -1,
            mGlovesIndex = -1, mScarvesIndex = -1, mHatsIndex = -1;
    ProgressDialog progDailog;
    private String mVacationZip;
    private boolean mFromVacation;
    private int mDay;
    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outfit_fragment);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.new_outfit);

        Intent i = getIntent();
        mVacationZip = i.getStringExtra(VacationOutfitsActivity.ZIPCODE_KEY);
        mFromVacation = i.getBooleanExtra(VacationOutfitsActivity.VACATION_KEY, false);
        mDay = i.getIntExtra(VacationOutfitsActivity.DAYS_KEY, 0);
        mId = i.getLongExtra(VacationOutfitsActivity.ID_KEY, -1);

        executeTestWeatherCode();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                executeTestWeatherCode();
                return true;
            default:
                return false;
        }
    }

    public void setOutfit(View v) {
        if(mFromVacation){
            setOutfitForVacation(v);
            return;
        }
        String mKey = getString(R.string.preference_name);
        SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.clear();

        // store weather
        mEditor.putString("DATE_INDEX", ((TextView)findViewById(R.id.outfit_date)).getText().toString());
        mEditor.putString("LOCATION_INDEX", ((TextView) findViewById(R.id.location)).getText().toString());
        mEditor.putString("HIGH_INDEX", ((TextView)findViewById(R.id.high)).getText().toString());
        mEditor.putString("LOW_INDEX", ((TextView)findViewById(R.id.low)).getText().toString());
        mEditor.putString("CONDITION_INDEX", ((TextView)findViewById(R.id.condition)).getText().toString());

        // store outfit indices
        if (mTopIndex != -1) mEditor.putLong("TOP_INDEX", (mTops.get(mTopIndex)).getId());
        else mEditor.putLong("TOP_INDEX", -1);

        if (mBottomIndex != -1) mEditor.putLong("BOTTOM_INDEX", (mBottoms.get(mBottomIndex)).getId());
        else mEditor.putLong("BOTTOMS_INDEX", -1);

        if (mShoesIndex != -1) mEditor.putLong("SHOES_INDEX", (mShoes.get(mShoesIndex)).getId());
        else mEditor.putLong("SHOES_INDEX", -1);

        if (mOuterwearIndex != -1) mEditor.putLong("OUTERWEAR_INDEX", (mOuterwear.get(mOuterwearIndex)).getId());
        else mEditor.putLong("OUTERWEAR_INDEX", -1);

        if (mGlovesIndex != -1) mEditor.putLong("GLOVES_INDEX", (mGloves.get(mGlovesIndex)).getId());
        else mEditor.putLong("GLOVES_INDEX", -1);

        if (mScarvesIndex != -1) mEditor.putLong("SCARVES_INDEX", (mScarves.get(mScarvesIndex)).getId());
        else mEditor.putLong("SCARVES_INDEX", -1);

        if (mHatsIndex != -1) mEditor.putLong("HATS_INDEX", (mHats.get(mHatsIndex)).getId());
        else mEditor.putLong("HATS_INDEX", -1);

        mEditor.commit();
        Toast.makeText(getApplicationContext(), "Outfit set!", Toast.LENGTH_SHORT).show();
        new SetWornAsyncTask().execute(mTopIndex, mBottomIndex, mShoesIndex, mOuterwearIndex, mGlovesIndex, mScarvesIndex, mHatsIndex);
    }

    private void setOutfitForVacation(View v){
        OutfitModel outfit = new OutfitModel();
        // store outfit indices
        if (mTopIndex != -1) outfit.setmTop((mTops.get(mTopIndex)).getId());

        if (mBottomIndex != -1) outfit.setmBottom((mBottoms.get(mBottomIndex)).getId());

        if (mShoesIndex != -1) outfit.setmBottom((mShoes.get(mShoesIndex)).getId());

        if (mOuterwearIndex != -1) outfit.setmOuterwear((mOuterwear.get(mOuterwearIndex)).getId());

        if (mGlovesIndex != -1) outfit.setmGloves((mGloves.get(mGlovesIndex)).getId());

        if (mScarvesIndex != -1) outfit.setmScarves((mScarves.get(mScarvesIndex)).getId());

        if (mHatsIndex != -1) outfit.setmBottom((mHats.get(mHatsIndex)).getId());

        outfit.setmLocation(mVacationZip);
        outfit.setmDay(Integer.toString(mDay));

        new InsertVacationOutfit().execute(outfit);
    }

    public void cancelOutfit(View v) {
        Toast.makeText(getApplicationContext(), "Outfit cancelled!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setWelcomeMessage(TextView welcomeText) {
        String welcomeMessage = "";

        if (mFromVacation) {
            welcomeText.setText("Outfit for Day " + String.valueOf(mDay + 1));
            return;
        }

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            welcomeMessage += "Good Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            welcomeMessage += "Good Afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            welcomeMessage += "Good Evening";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            welcomeMessage += "Good Night";
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sp.getString("editTextPref_DisplayName", "-1").equals("-1")) {
            welcomeMessage += " " + sp.getString("editTextPref_DisplayName", "-1");
        }

        welcomeText.setText(welcomeMessage + "!");
    }

    private String getSeason() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);

        if (month >= 10 || month < 4) return "winter";
        if (month >= 8) return "fall";
        if (month >= 6) return "summer";
        return "spring";
    }

    private void getTop() {
        if (mTops == null) return;

        ArrayList<ClothingItem> toRemove = new ArrayList<ClothingItem>();
        for (ClothingItem top: mTops){
            if (System.currentTimeMillis()/86400000 - top.getLastUsed() < top.getCycleLength()) {
                toRemove.add(top);
            }
        }
        mTops.removeAll(toRemove);

        ((findViewById(R.id.top))).setVisibility(View.VISIBLE);
        if (mTops.size() == 0) {
            ((ImageView) (findViewById(R.id.top_image))).setImageDrawable(getDrawable(R.drawable.noneavailable));
            ((findViewById(R.id.top_back))).setVisibility(View.GONE);
            ((findViewById(R.id.top_forward))).setVisibility(View.GONE);
        } else {
            mTopIndex = (int) (Math.random() * mTops.size());
            ((ImageView) (findViewById(R.id.top_image))).setImageBitmap(mTops.get(mTopIndex).getImage());
        }
        ((findViewById(R.id.top_group))).setVisibility(View.VISIBLE);
    }

    private void getBottoms() {
        if (mBottoms == null) return;

        ArrayList<ClothingItem> toRemove = new ArrayList<ClothingItem>();
        for (ClothingItem bottom: mBottoms){
            if (System.currentTimeMillis()/86400000 - bottom.getLastUsed() < bottom.getCycleLength()) {
                toRemove.add(bottom);
            }
        }
        mBottoms.removeAll(toRemove);

        ((findViewById(R.id.bottom))).setVisibility(View.VISIBLE);
        if (mBottoms.size() == 0) {
            ((ImageView) (findViewById(R.id.bottom_image))).setImageDrawable(getDrawable(R.drawable.noneavailable));
            ((findViewById(R.id.bottom_back))).setVisibility(View.GONE);
            ((findViewById(R.id.bottom_forward))).setVisibility(View.GONE);
        } else {
            mBottomIndex = (int) (Math.random() * mBottoms.size());
            ((ImageView) (findViewById(R.id.bottom_image))).setImageBitmap(mBottoms.get(mBottomIndex).getImage());
        }
        ((findViewById(R.id.bottom_group))).setVisibility(View.VISIBLE);
    }

    private void getShoes() {
        if (mShoes == null) return;

        ArrayList<ClothingItem> toRemove = new ArrayList<ClothingItem>();
        for (ClothingItem shoes: mShoes){
            if (System.currentTimeMillis()/86400000 - shoes.getLastUsed() < shoes.getCycleLength()) {
                toRemove.add(shoes);
            }
        }
        mShoes.removeAll(toRemove);
        
        ((findViewById(R.id.shoes))).setVisibility(View.VISIBLE);
        if (mShoes.size() == 0) {
            ((ImageView) (findViewById(R.id.shoes_image))).setImageDrawable(getDrawable(R.drawable.noneavailable));
            ((findViewById(R.id.shoes_back))).setVisibility(View.GONE);
            ((findViewById(R.id.shoes_forward))).setVisibility(View.GONE);
        } else {
            mShoesIndex = (int) (Math.random() * mShoes.size());
            ((ImageView) (findViewById(R.id.shoes_image))).setImageBitmap(mShoes.get(mShoesIndex).getImage());
        }
        ((findViewById(R.id.shoes_group))).setVisibility(View.VISIBLE);
    }

    private void getOuterwear() {
        if (mOuterwear == null) return;

        ArrayList<ClothingItem> toRemove = new ArrayList<ClothingItem>();
        for (ClothingItem outerwear: mOuterwear){
            if (System.currentTimeMillis()/86400000 - outerwear.getLastUsed() < outerwear.getCycleLength()) {
                toRemove.add(outerwear);
            }
        }
        mOuterwear.removeAll(toRemove);
        
        ((findViewById(R.id.outerwear))).setVisibility(View.VISIBLE);
        if (mOuterwear.size() == 0) {
            ((ImageView) (findViewById(R.id.outerwear_image))).setImageDrawable(getDrawable(R.drawable.noneavailable));
            ((findViewById(R.id.outerwear_back))).setVisibility(View.GONE);
            ((findViewById(R.id.outerwear_forward))).setVisibility(View.GONE);
        } else {
            mOuterwearIndex = (int) (Math.random() * mOuterwear.size());
            ((ImageView) (findViewById(R.id.outerwear_image))).setImageBitmap(mOuterwear.get(mOuterwearIndex).getImage());
        }
        ((findViewById(R.id.outerwear_group))).setVisibility(View.VISIBLE);
    }

    private void getScarves() {
        if (mScarves == null) return;

        ArrayList<ClothingItem> toRemove = new ArrayList<ClothingItem>();
        for (ClothingItem scarf: mScarves){
            if (System.currentTimeMillis()/86400000 - scarf.getLastUsed() < scarf.getCycleLength()) {
                toRemove.add(scarf);
            }
        }
        mScarves.removeAll(toRemove);
        
        ((findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
        if (mScarves.size() == 0) {
            ((ImageView) (findViewById(R.id.scarves_image))).setImageDrawable(getDrawable(R.drawable.noscarvesavailable));
            ((findViewById(R.id.scarves_back))).setVisibility(View.GONE);
            ((findViewById(R.id.scarves_forward))).setVisibility(View.GONE);
        } else {
            mScarvesIndex = (int) (Math.random() * mScarves.size());
            ((ImageView) (findViewById(R.id.scarves_image))).setImageBitmap(mScarves.get(mScarvesIndex).getImage());
        }
        ((findViewById(R.id.scarves_group))).setVisibility(View.VISIBLE);
    }

    private void getGloves() {
        if (mGloves == null) return;

        ArrayList<ClothingItem> toRemove = new ArrayList<ClothingItem>();
        for (ClothingItem glove: mGloves){
            if (System.currentTimeMillis()/86400000 - glove.getLastUsed() < glove.getCycleLength()) {
                toRemove.add(glove);
            }
        }
        mGloves.removeAll(toRemove);
        
        ((findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
        if (mGloves.size() == 0) {
            ((ImageView) (findViewById(R.id.gloves_image))).setImageDrawable(getDrawable(R.drawable.noglovesavailable));
            ((findViewById(R.id.gloves_back))).setVisibility(View.GONE);
            ((findViewById(R.id.gloves_forward))).setVisibility(View.GONE);
        } else {
            mGlovesIndex = (int) (Math.random() * mGloves.size());
            ((ImageView) (findViewById(R.id.gloves_image))).setImageBitmap(mGloves.get(mGlovesIndex).getImage());
        }
        ((findViewById(R.id.gloves_group))).setVisibility(View.VISIBLE);
    }

    private void getHats() {
        if (mHats == null) return;

        ArrayList<ClothingItem> toRemove = new ArrayList<ClothingItem>();
        for (ClothingItem hat: mHats){
            if (System.currentTimeMillis()/86400000 - hat.getLastUsed() < hat.getCycleLength()) {
                toRemove.add(hat);
            }
        }
        mHats.removeAll(toRemove);
        
        ((findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
        if (mHats.size() == 0) {
            ((ImageView) (findViewById(R.id.hats_image))).setImageDrawable(getDrawable(R.drawable.nohatsavailable));
            ((findViewById(R.id.hats_back))).setVisibility(View.GONE);
            ((findViewById(R.id.hats_forward))).setVisibility(View.GONE);

        } else {
            mHatsIndex = (int) (Math.random() * mHats.size());
            ((ImageView) (findViewById(R.id.hats_image))).setImageBitmap(mHats.get(mHatsIndex).getImage());
        }
        ((findViewById(R.id.hats_group))).setVisibility(View.VISIBLE);
    }

    private void executeTestWeatherCode() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String zipCode = sp.getString("editTextPref_SetLocation", "");
        boolean current = sp.getBoolean("checkboxPref_CurrentLocation", false);
        if(mFromVacation) {
            Log.d("OutfitLogd", ""+mVacationZip);
            new WeatherAsyncTask().execute(mVacationZip);
        }
        else if (current || zipCode.equals("")) {
            callWithCurrentZipCode();
        } else {
            new WeatherAsyncTask().execute(zipCode);
        }
    }

    private void callWithCurrentZipCode() {
        final GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .build();

        final Geocoder mGeocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                try {
                    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    List<Address> addresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    mGoogleApiClient.disconnect();
                    if (addresses.size() == 0) {
                        // Failed to obtain zip code
                        new AlertDialog.Builder(getApplicationContext()).setMessage("Error obtaining zip code").show();
                    } else {
                        String zipCode = addresses.get(0).getPostalCode();
                        new WeatherAsyncTask().execute(zipCode);
                        // Do something with zip code
                    }
                } catch (SecurityException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                // Whelp
            }
        });

        mGoogleApiClient.connect();
    }

    private ArrayList<String> getWeatherArray(JSONObject data){
        try {
            Log.d("getWeatherArraylogd",""+mDay);
            String condition, low, high;
            // Extract useful information from raw JSON
            if(mDay == 0) {
                condition = data.getJSONObject("item").getJSONObject("condition").getString("text");
                low = ((JSONObject) data.getJSONObject("item").getJSONArray("forecast").get(0)).getString("low");
                high = ((JSONObject) data.getJSONObject("item").getJSONArray("forecast").get(0)).getString("high");
            } else {
                // Can get data for today, tomorrow, day after, next, next.  In total, 5 days including today with indices 1-4 for the four future days. (change in get parameter)
                low = ((JSONObject) data.getJSONObject("item").getJSONArray("forecast").get(mDay)).getString("low");
                high = ((JSONObject) data.getJSONObject("item").getJSONArray("forecast").get(mDay)).getString("high");
                condition = ((JSONObject) data.getJSONObject("item").getJSONArray("forecast").get(mDay)).getString("text");
            }

            ArrayList<String> weatherData = new ArrayList<String>();

            String location = data.getJSONObject("item").getString("title").split("for ")[1];
            location = location.split(",")[0];
            weatherData.add(location);
            weatherData.add(high);
            weatherData.add(low);
            weatherData.add(condition);

            return weatherData;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private class WeatherAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(NewOutfitActivity.this);
            progDailog.setMessage("Loading Your Outfit...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> weather) {
            if (weather == null) {
                progDailog.dismiss();

                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                mWeatherArray = weather;
                new ClothingAsyncTask().execute(mWeatherArray);
            }

        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            try {
                String zipcode = params[0];
                String start = "https://query.yahooapis.com/v1/public/yql?q=";
                String query = "SELECT * FROM weather.forecast WHERE woeid IN (SELECT woeid FROM geo.places(1) WHERE text=\"" + zipcode + ", USA\")";

                // Build URL weather request
                URL request = new URL(start + URLEncoder.encode(query, "UTF-8") + "&format=json");
                // Request content and convert to JSONObject
                JSONObject json = new JSONObject(IOUtils.toString(request, Charset.forName("UTF-8")));

                JSONObject data = json.getJSONObject("query").getJSONObject("results").getJSONObject("channel");

                // Extract useful information from raw JSON
                String windChillTemperature = data.getJSONObject("wind").getString("chill");
                String currentTemperature = data.getJSONObject("item").getJSONObject("condition").getString("temp");
                /*String currentCondition = data.getJSONObject("item").getJSONObject("condition").getString("text");
                String todayLow = ((JSONObject) data.getJSONObject("item").getJSONArray("forecast").get(0)).getString("low");
                String todayHigh = ((JSONObject) data.getJSONObject("item").getJSONArray("forecast").get(0)).getString("high");

                // Can get data for today, tomorrow, day after, next, next.  In total, 5 days including today with indices 1-4 for the four future days. (change in get parameter)
                String tomorrowLow = ((JSONObject) data.getJSONObject("item").getJSONArray("forecast").get(1)).getString("low");
                String tomorrowHigh = ((JSONObject) data.getJSONObject("item").getJSONArray("forecast").get(1)).getString("high");
                String tomorrowCondition = ((JSONObject) data.getJSONObject("item").getJSONArray("forecast").get(1)).getString("text");

                ArrayList<String> weatherData = new ArrayList<String>();

                String location = data.getJSONObject("item").getString("title").split("for ")[1];
                location = location.split(",")[0];
                weatherData.add(location);
                weatherData.add(todayHigh);
                weatherData.add(todayLow);
                weatherData.add(currentCondition);

                Log.d("MYZIP", data.getJSONObject("item").getString("title"));
                Log.d("Current Wind Chill", windChillTemperature);
                Log.d("Current Temperature", currentTemperature);
                Log.d("Current Condition", currentCondition);
                Log.d("Current Low", todayLow);
                Log.d("Current High", todayHigh);


                Log.d("Tomorrow Condition", tomorrowCondition);
                Log.d("Tomorrow Low", tomorrowLow);
                Log.d("Tomorrow High", tomorrowHigh);*/
                return getWeatherArray(data);
                //return weatherData;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class ClothingAsyncTask extends AsyncTask<ArrayList<String>, Void, ArrayList<ArrayList<ClothingItem>>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<ClothingItem>> clothes) {
            progDailog.dismiss();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d");
            setWelcomeMessage(((TextView) (findViewById(R.id.welcome))));
            if(mDay == 0) {
                ((TextView) (findViewById(R.id.outfit_date))).setText("Outfit Date: " + sdf.format(new Date()));
            } else {
                ((TextView) (findViewById(R.id.outfit_date))).setText("Outfit Date: " + sdf.format(new Date()));
            }
            ((TextView) (findViewById(R.id.location))).setText("Location: " + mWeatherArray.get(0));

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (!sp.getString("listPref_Temp","-1").equals("Celsius")) {
                ((TextView) (findViewById(R.id.high))).setText("High: " + mWeatherArray.get(1) + "°F");
                ((TextView) (findViewById(R.id.low))).setText("Low: " + mWeatherArray.get(2) + "°F");
            } else {
                ((TextView) (findViewById(R.id.high))).setText("High: " + String.valueOf(Math.round((((Double.valueOf(mWeatherArray.get(1))-32)*5/9)) * 10) / 10) + "°C");
                ((TextView) (findViewById(R.id.low))).setText("Low: " +  String.valueOf(Math.round((((Double.valueOf(mWeatherArray.get(2))-32)*5/9)) * 10) / 10) + "°C");
            }
            ((TextView) (findViewById(R.id.condition))).setText("Condition: " + mWeatherArray.get(3));
            if (clothes.size() == 0) {
                new AlertDialog.Builder(getApplicationContext()).setMessage("Error Generating Outfit!").show();
            } else {
                mTops = clothes.get(0);
                mBottoms = clothes.get(1);
                mShoes = clothes.get(2);
                mOuterwear = clothes.get(3);
                mScarves = clothes.get(4);
                mGloves = clothes.get(5);
                mHats = clothes.get(6);
                findViewById(R.id.outfit_description).setVisibility(View.VISIBLE);
                getTop();
                getBottoms();
                getShoes();
                getOuterwear();
                getScarves();
                getGloves();
                getHats();
                findViewById(R.id.saveOutfit).setEnabled(true);
            }

        }

        @Override
        protected ArrayList<ArrayList<ClothingItem>> doInBackground(ArrayList<String>... params) {

            ArrayList<ArrayList<ClothingItem>> clothes = new ArrayList<ArrayList<ClothingItem>>();
            ArrayList<String> weather = params[0];
            String season = getSeason();
            ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getApplicationContext());

            int avgTemp = ((Integer.valueOf(weather.get(1)) + Integer.valueOf(weather.get(2))) / 2);

            // top
            if (avgTemp >= 85) {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Sleeveless Shirts", season));
            } else if (avgTemp >= 50) {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Short Sleeve Shirts", season));
            } else {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Long Sleeve Shirts", season));
            }

            // bottom
            if (avgTemp >= 70) {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Shorts", season));
            } else {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Pants", season));
            }

            // shoes
            if (weather.get(3).toLowerCase().contains("snow")) {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Snow Boots", season));
            } else if (weather.get(3).toLowerCase().contains("rain") ||
                    weather.get(3).toLowerCase().contains("shower")) {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Rain Boots", season));
            } else if (avgTemp <= 50) {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Boots", season));
            } else if (avgTemp <= 75) {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Sneakers", season));
            } else {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Sandals", season));
            }

            // outerwear
            if (avgTemp <= 50) {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Coats", season));
            } else if (weather.get(3).toLowerCase().contains("rain") ||
                    weather.get(3).toLowerCase().contains("shower")) {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Raincoats", season));
            } else clothes.add(null);

            if (avgTemp <= 31) {
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Scarves", season));
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Gloves", season));
                clothes.add(dbHelper.fetchEntriesByCategoryAndSeason("Hats", season));
            } else {
                clothes.add(null);
                clothes.add(null);
                clothes.add(null);
            }

            return clothes;
        }
    }

    public void topBack(View v) {
        if (mTopIndex == 0) mTopIndex = mTops.size();
        ((ImageView) (findViewById(R.id.top_image))).setImageBitmap(mTops.get(--mTopIndex).getImage());
    }

    public void topForward(View v) {
        if (mTopIndex +  1 >= mTops.size()) mTopIndex = -1;
        ((ImageView) (findViewById(R.id.top_image))).setImageBitmap(mTops.get(++mTopIndex).getImage());
    }

    public void bottomBack(View v) {
        if (mBottomIndex == 0) mBottomIndex = mBottoms.size();
        ((ImageView) (findViewById(R.id.bottom_image))).setImageBitmap(mBottoms.get(--mBottomIndex).getImage());
    }

    public void bottomForward(View v) {
        if (mBottomIndex +  1 >= mBottoms.size()) mBottomIndex = -1;
        ((ImageView) (findViewById(R.id.bottom_image))).setImageBitmap(mBottoms.get(++mBottomIndex).getImage());
    }

    public void shoesBack(View v) {
        if (mShoesIndex == 0) mShoesIndex = mShoes.size();
        ((ImageView) (findViewById(R.id.shoes_image))).setImageBitmap(mShoes.get(--mShoesIndex).getImage());
    }

    public void shoesForward(View v) {
        if (mShoesIndex +  1 >= mShoes.size()) mShoesIndex = -1;
        ((ImageView) (findViewById(R.id.shoes_image))).setImageBitmap(mShoes.get(++mShoesIndex).getImage());
    }

    public void outerwearBack(View v) {
        if (mOuterwearIndex == 0) mOuterwearIndex = mOuterwear.size();
        ((ImageView) (findViewById(R.id.outerwear_image))).setImageBitmap(mOuterwear.get(--mOuterwearIndex).getImage());
    }

    public void outerwearForward(View v) {
        if (mOuterwearIndex +  1 >= mOuterwear.size()) mOuterwearIndex = -1;
        ((ImageView) (findViewById(R.id.outerwear_image))).setImageBitmap(mOuterwear.get(++mOuterwearIndex).getImage());
    }

    public void scarvesBack(View v) {
        if (mScarvesIndex == 0) mScarvesIndex = mScarves.size();
        ((ImageView) (findViewById(R.id.scarves_image))).setImageBitmap(mScarves.get(--mScarvesIndex).getImage());
    }

    public void scarvesForward(View v) {
        if (mScarvesIndex +  1 >= mScarves.size()) mScarvesIndex = -1;
        ((ImageView) (findViewById(R.id.scarves_image))).setImageBitmap(mScarves.get(++mScarvesIndex).getImage());
    }

    public void glovesBack(View v) {
        if (mGlovesIndex == 0) mGlovesIndex = mGloves.size();
        ((ImageView) (findViewById(R.id.gloves_image))).setImageBitmap(mGloves.get(--mGlovesIndex).getImage());
    }

    public void glovesForward(View v) {
        if (mGlovesIndex +  1 >= mGloves.size()) mGlovesIndex = -1;
        ((ImageView) (findViewById(R.id.gloves_image))).setImageBitmap(mGloves.get(++mGlovesIndex).getImage());
    }

    public void hatsBack(View v) {
        if (mHatsIndex == 0) mHatsIndex = mHats.size();
        ((ImageView) (findViewById(R.id.hats_image))).setImageBitmap(mHats.get(--mHatsIndex).getImage());
    }

    public void hatsForward(View v) {
        if (mHatsIndex +  1 >= mHats.size()) mHatsIndex = -1;
        ((ImageView) (findViewById(R.id.hats_image))).setImageBitmap(mHats.get(++mHatsIndex).getImage());
    }

    @Override
    protected void onResume() {
        KeyguardManager myKM = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        final AlarmAlertManager mAAManager = new AlarmAlertManager();
        if(!myKM.inKeyguardRestrictedInputMode() && mAAManager.isPlaying()) { // if it's not locked, and it's resuming, kill the alarm
            // Ensure the alarm runs for at least 1 second
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAAManager.stopAlerts();
                }
            }, 1000);
        }
        super.onResume();
    }

    private class SetWornAsyncTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getApplicationContext());
            int days = (int)(System.currentTimeMillis()/86400000);

            if (params[0] != -1) {
                mTops.get(params[0]).setLastUsed(days);
                dbHelper.updateItem(mTops.get(params[0]));
            }

            if (params[1] != -1) {
                mBottoms.get(params[1]).setLastUsed(days);
                dbHelper.updateItem(mBottoms.get(params[1]));
            }

            if (params[2] != -1) {
                mShoes.get(params[2]).setLastUsed(days);
                dbHelper.updateItem(mShoes.get(params[2]));
            }

            if (params[3] != -1) {
                mOuterwear.get(params[3]).setLastUsed(days);
                dbHelper.updateItem(mOuterwear.get(params[3]));
            }

            if (params[4] != -1) {
                mScarves.get(params[4]).setLastUsed(days);
                dbHelper.updateItem(mScarves.get(params[4]));
            }

            if (params[5] != -1) {
                mGloves.get(params[5]).setLastUsed(days);
                dbHelper.updateItem(mGloves.get(params[5]));
            }

            if (params[6] != -1) {
                mHats.get(params[6]).setLastUsed(days);
                dbHelper.updateItem(mHats.get(params[6]));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }

    }

    // Inserts vacation outfit into database
    private class InsertVacationOutfit extends AsyncTask<OutfitModel, Void, Void> {
        private OutfitDatabaseHelper mOutfitDbHelper = new OutfitDatabaseHelper(getApplicationContext());
        @Override
        protected Void doInBackground(OutfitModel... args) {
            long id = mOutfitDbHelper.insertItem(args[0]);
            VacationModel vacation = VacationOutfitsActivity.getVacation();
            switch(mDay){
                case 0:
                    vacation.setDayOne(id);
                    break;
                case 1:
                    vacation.setDayTwo(id);
                    break;
                case 2:
                    vacation.setDayThree(id);
                    break;
                case 3:
                    vacation.setDayFour(id);
                    break;
                case 4:
                    vacation.setDayFive(id);
                    break;
                default:
                    break;
            }
            Log.d("VacationLogD", "dayone: " + vacation.getDayOne() );
            Log.d("VacationLogD", "daytwo: " + vacation.getDayTwo() );
            Log.d("VacationLogD", "daythree: " + vacation.getDayThree() );
            Log.d("VacationLogD", "dayfour: " + vacation.getDayFour() );
            Log.d("VacationLogD", "dayfive: " + vacation.getDayFive() );

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Outfit set!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

