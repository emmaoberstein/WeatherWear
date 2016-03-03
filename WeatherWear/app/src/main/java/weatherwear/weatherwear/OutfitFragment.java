package weatherwear.weatherwear;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
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
import java.util.Random;



import weatherwear.weatherwear.database.ClothingDatabaseHelper;
import weatherwear.weatherwear.database.ClothingItem;

/**
 * Created by Emma on 2/16/16.
 */
public class OutfitFragment extends Fragment {

    private ArrayList<String> mWeatherArray = new ArrayList<String>();
    private ArrayList<ClothingItem> mTops, mBottoms, mShoes, mOuterwear, mScarves, mGloves, mHats;
    private int mTopIndex, mBottomIndex, mShoesIndex, mOuterwearIndex;

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("top_index", mTopIndex);
        outState.putInt("bottom_index", mBottomIndex);
        outState.putInt("top_index", mShoesIndex);
        outState.putInt("bottom_index", mBottomIndex);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.outfit_fragment, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.todays_outfit);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        executeTestWeatherCode();

        final at.markushi.ui.CircleButton topBack = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.top_back);
        topBack.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {
                if (mTopIndex == 0) mTopIndex = mTops.size();
                ((ImageView) (getView().findViewById(R.id.top_image))).setImageBitmap(mTops.get(--mTopIndex).getImage());
            }
        });

        final at.markushi.ui.CircleButton topForward = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.top_forward);
        topForward.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {
                if (mTopIndex +  1 >= mTops.size()) mTopIndex = -1;
                ((ImageView) (getView().findViewById(R.id.top_image))).setImageBitmap(mTops.get(++mTopIndex).getImage());
            }
        });

        final at.markushi.ui.CircleButton bottomBack = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.bottom_back);
        bottomBack.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {
                if (mBottomIndex == 0) mBottomIndex = mBottoms.size();
                ((ImageView) (getView().findViewById(R.id.bottom_image))).setImageBitmap(mBottoms.get(--mBottomIndex).getImage());
            }
        });

        final at.markushi.ui.CircleButton bottomForward = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.bottom_forward);
        bottomForward.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {
                if (mBottomIndex + 1 >= mBottoms.size()) mBottomIndex = -1;
                ((ImageView) (getView().findViewById(R.id.bottom_image))).setImageBitmap(mBottoms.get(++mBottomIndex).getImage());
            }
        });

        final at.markushi.ui.CircleButton shoeBack = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.shoes_back);
        shoeBack.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {
                if (mShoesIndex == 0) mShoesIndex = mShoes.size();
                ((ImageView) (getView().findViewById(R.id.shoes_image))).setImageBitmap(mShoes.get(--mShoesIndex).getImage());
            }
        });

        final at.markushi.ui.CircleButton shoeForward = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.shoes_forward);
        shoeForward.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View v) {
                if (mShoesIndex + 1 >= mShoes.size()) mShoesIndex = -1;
                ((ImageView)(getView().findViewById(R.id.shoes_image))).setImageBitmap(mShoes.get(++mShoesIndex).getImage());
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuitem;
        menuitem = menu.add(Menu.NONE, 0,0, "New");
        menuitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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

    private void setWelcomeMessage(TextView welcomeText){
        String welcomeMessage = "";
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            welcomeMessage+="Good Morning";
        } else if(timeOfDay >= 12 && timeOfDay < 16){
            welcomeMessage+="Good Afternoon";
        } else if(timeOfDay >= 16 && timeOfDay < 21){
            welcomeMessage+="Good Evening";
        } else if(timeOfDay >= 21 && timeOfDay < 24){
            welcomeMessage+="Good Night";
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!sp.getString("editTextPref_DisplayName","-1").equals("-1")) {
            welcomeMessage+= " " + sp.getString("editTextPref_DisplayName","-1");
        }

        welcomeText.setText(welcomeMessage + "!");

    }
    private void generateOutfit(ArrayList<String> weather) {

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d");
        setWelcomeMessage(((TextView)(getView().findViewById(R.id.welcome))));
        ((TextView)(getView().findViewById(R.id.outfit_date))).setText("Outfit Date: " + sdf.format(new Date()));
        ((TextView)(getView().findViewById(R.id.location))).setText("Location: " + weather.get(0));
        ((TextView)(getView().findViewById(R.id.high))).setText("High: " + weather.get(1) + "°F");
        ((TextView)(getView().findViewById(R.id.low))).setText("Low: " + weather.get(2) + "°F");
        ((TextView)(getView().findViewById(R.id.condition))).setText("Condition: " + weather.get(3));

        new ClothingAsyncTask().execute(weather);

    }

    private String getSeason(){
        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);

        if (month >= 10 || month < 4) return "winter";
        if (month >= 8) return "fall";
        if (month >= 6) return "summer";
        return "spring";
    }

    private void getTop(){
        if (mTops == null || mTops.size() == 0) return;
        ((getView().findViewById(R.id.top))).setVisibility(View.VISIBLE);
        ((ImageView)(getView().findViewById(R.id.top_image))).setImageBitmap(mTops.get((int) (Math.random() * mTops.size())).getImage());
        ((getView().findViewById(R.id.top_group))).setVisibility(View.VISIBLE);
    }

    private void getBottoms(){
        if (mBottoms == null || mBottoms.size() == 0) return;
        ((getView().findViewById(R.id.bottom))).setVisibility(View.VISIBLE);
        ((ImageView)(getView().findViewById(R.id.bottom_image))).setImageBitmap(mBottoms.get((int) (Math.random() * mBottoms.size())).getImage());
        ((getView().findViewById(R.id.bottom_group))).setVisibility(View.VISIBLE);
    }

    private void getShoes(){
        if (mShoes == null || mShoes.size() == 0) return;
        ((getView().findViewById(R.id.shoes))).setVisibility(View.VISIBLE);
        ((ImageView)(getView().findViewById(R.id.shoes_image))).setImageBitmap(mShoes.get((int)(Math.random()*mShoes.size())).getImage());
        ((getView().findViewById(R.id.shoes_group))).setVisibility(View.VISIBLE);
    }

    private void getOuterwear(){
        if (mOuterwear == null || mOuterwear.size() == 0) return;
        ((getView().findViewById(R.id.outerwear))).setVisibility(View.VISIBLE);
        ((ImageView)(getView().findViewById(R.id.outerwear_image))).setImageBitmap(mOuterwear.get((int)(Math.random()*mOuterwear.size())).getImage());
        ((getView().findViewById(R.id.outerwear_group))).setVisibility(View.VISIBLE);
    }

    private void getScarves(){
        if (mScarves == null || mScarves.size() == 0) return;
        ((getView().findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
        ((ImageView)(getView().findViewById(R.id.scarves_image))).setImageBitmap(mScarves.get((int)(Math.random()*mScarves.size())).getImage());
        ((getView().findViewById(R.id.scarves_group))).setVisibility(View.VISIBLE);
    }

    private void getGloves(){
        if (mGloves == null || mGloves.size() == 0) return;
        ((getView().findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
        ((ImageView)(getView().findViewById(R.id.gloves_image))).setImageBitmap(mGloves.get((int)(Math.random()*mGloves.size())).getImage());
        ((getView().findViewById(R.id.gloves_group))).setVisibility(View.VISIBLE);
    }

    private void getHats(){
        if (mHats == null || mHats.size() == 0) return;
        ((getView().findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
        ((ImageView)(getView().findViewById(R.id.hats_image))).setImageBitmap(mHats.get((int)(Math.random()*mHats.size())).getImage());
        ((getView().findViewById(R.id.hats_group))).setVisibility(View.VISIBLE);
    }
    private void executeTestWeatherCode() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String zipCode = sp.getString("editTextPref_SetLocation", "");
        boolean current = sp.getBoolean("checkboxPref_CurrentLocation", false);
        if (current || zipCode.equals("")) {
            callWithCurrentZipCode();
        } else {
            new WeatherAsyncTask().execute(zipCode);
        }
    }

    private void callWithCurrentZipCode() {
        final GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .build();

        final Geocoder mGeocoder = new Geocoder(getActivity(), Locale.getDefault());

        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                try {
                    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    List<Address> addresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    mGoogleApiClient.disconnect();
                    if (addresses.size() == 0) {
                        // Failed to obtain zip code
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

    private class WeatherAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<String> weather) {
            if (weather == null) {
                new AlertDialog.Builder(getActivity()).setMessage("Connect to the Internet to Generate Today's Outfit!").show();
            }

            else {
                mWeatherArray = weather;
                generateOutfit(mWeatherArray);
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
                String currentCondition = data.getJSONObject("item").getJSONObject("condition").getString("text");
                String todayLow = ((JSONObject)data.getJSONObject("item").getJSONArray("forecast").get(0)).getString("low");
                String todayHigh = ((JSONObject)data.getJSONObject("item").getJSONArray("forecast").get(0)).getString("high");

                // Can get data for today, tomorrow, day after, next, next.  In total, 5 days including today with indices 1-4 for the four future days. (change in get parameter)
                String tomorrowLow = ((JSONObject)data.getJSONObject("item").getJSONArray("forecast").get(1)).getString("low");
                String tomorrowHigh = ((JSONObject)data.getJSONObject("item").getJSONArray("forecast").get(1)).getString("high");
                String tomorrowCondition = ((JSONObject)data.getJSONObject("item").getJSONArray("forecast").get(1)).getString("text");

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
                Log.d("Tomorrow High", tomorrowHigh);
                return weatherData;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class ClothingAsyncTask extends AsyncTask<ArrayList<String>, Void, ArrayList<ArrayList<ClothingItem>>> {


        @Override
        protected void onPreExecute() { super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<ClothingItem>> clothes) {
            if (clothes.size() == 0) {
                new AlertDialog.Builder(getActivity()).setMessage("Error Generating Outfit!").show();
            }

            else {
                mTops = clothes.get(0);
                mBottoms = clothes.get(1);
                mShoes = clothes.get(2);
                mOuterwear = clothes.get(3);
                mScarves = clothes.get(4);
                mGloves = clothes.get(5);
                mHats = clothes.get(6);
                getTop();
                getBottoms();
                getShoes();
                getOuterwear();
                getScarves();
                getGloves();
                getHats();
            }

        }

        @Override
        protected ArrayList<ArrayList<ClothingItem>> doInBackground(ArrayList<String>... params) {

            ArrayList<ArrayList<ClothingItem>> clothes = new ArrayList<ArrayList<ClothingItem>>();
            ArrayList<String> weather = params[0];
            String season = getSeason();
            ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getActivity());

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
            } else clothes.add(null);

            if (avgTemp <= 32) {
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

}