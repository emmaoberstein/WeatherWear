package weatherwear.weatherwear;

import android.app.Fragment;
import android.content.SharedPreferences;
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

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;



import weatherwear.weatherwear.database.ClothingDatabaseHelper;
import weatherwear.weatherwear.database.ClothingItem;

/**
 * Created by Emma on 2/16/16.
 */
public class OutfitFragment extends Fragment {

    private ArrayList<String> mWeatherArray = new ArrayList<String>();
    private ArrayList<ClothingItem> mTops, mBottoms, mShoes, mOuterwear;
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

        TextView welcomeText = (TextView)rootView.findViewById(R.id.welcome);
        setWelcomeMessage(welcomeText);

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

        String season = getSeason();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d");
        ((TextView)(getView().findViewById(R.id.outfit_date))).setText("Outfit Date: " + sdf.format(new Date()));
        ((TextView)(getView().findViewById(R.id.location))).setText("Location: " + weather.get(0));
        ((TextView)(getView().findViewById(R.id.high))).setText("High: " + weather.get(1) + "°F");
        ((TextView)(getView().findViewById(R.id.low))).setText("Low: " + weather.get(2) + "°F");
        ((TextView)(getView().findViewById(R.id.condition))).setText("Condition: " + weather.get(3));

        ArrayList<String> outfitCriteria = new ArrayList<String>();
        int avgTemp = ((Integer.valueOf(weather.get(1)) + Integer.valueOf(weather.get(2)))/2);

        // top
        if (avgTemp >= 85) {
            getTop(season, "Sleeveless Shirts");
        } else if (avgTemp >= 50) {
            getTop(season, "Short Sleeve Shirts");
        } else {
            getTop(season, "Long Sleeve Shirts");
        }

        // bottom
        if (avgTemp >= 70) {
            getBottoms(season, "Shorts");
        } else {
            getBottoms(season, "Pants");
        }

        // shoes
        if (weather.get(3).toLowerCase().contains("snow")) {
            getShoes(season, "Snow Boots");
        } else if (weather.get(3).toLowerCase().contains("rain") ||
                weather.get(3).toLowerCase().contains("shower")) {
            getShoes(season, "Rain Boots");
        } else if (avgTemp <= 50) {
            getShoes(season, "Boots");
        } else if (avgTemp <= 75) {
            getShoes(season, "Sneakers");
        } else {
            getShoes(season, "Sandals");
        }

        // outerwear
        if (avgTemp <= 50) {
            getOuterwear(season, "Coats");
        }

        // accessories
        if (avgTemp <= 32) {
            outfitCriteria.add("Hats");
            outfitCriteria.add("Scarves");
            outfitCriteria.add("Gloves");
        }

        String o = String.valueOf(avgTemp)+ ": ";
        for (String temp : outfitCriteria) {
            o += temp + "; ";
        }
        Log.d("outfit", o);
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

    private void getTop(String season, String type){
        ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getActivity());
        ((getView().findViewById(R.id.top))).setVisibility(View.VISIBLE);
        mTops = dbHelper.fetchEntriesByCategoryAndSeason(type, season);
        ((ImageView)(getView().findViewById(R.id.top_image))).setImageBitmap(mTops.get((int) (Math.random() * mTops.size())).getImage());
        ((getView().findViewById(R.id.top_group))).setVisibility(View.VISIBLE);
    }

    private void getBottoms(String season, String type){

        ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getActivity());
        ((getView().findViewById(R.id.bottom))).setVisibility(View.VISIBLE);
        mBottoms = dbHelper.fetchEntriesByCategoryAndSeason(type, season);
        ((ImageView)(getView().findViewById(R.id.bottom_image))).setImageBitmap(mBottoms.get((int)(Math.random()*mBottoms.size())).getImage());
        ((getView().findViewById(R.id.bottom_group))).setVisibility(View.VISIBLE);
    }

    private void getShoes(String season, String type){

        ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getActivity());
        ((getView().findViewById(R.id.shoes))).setVisibility(View.VISIBLE);
        mShoes = dbHelper.fetchEntriesByCategoryAndSeason(type, season);
        ((ImageView)(getView().findViewById(R.id.shoes_image))).setImageBitmap(mShoes.get((int)(Math.random()*mShoes.size())).getImage());
        ((getView().findViewById(R.id.shoes_group))).setVisibility(View.VISIBLE);
    }

    private void getOuterwear(String season, String type){

        ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getActivity());
        ((getView().findViewById(R.id.outerwear))).setVisibility(View.VISIBLE);
        mOuterwear = dbHelper.fetchEntriesByCategoryAndSeason(type, season);
        ((ImageView)(getView().findViewById(R.id.outerwear_image))).setImageBitmap(mOuterwear.get((int)(Math.random()*mOuterwear.size())).getImage());
        ((getView().findViewById(R.id.outerwear_group))).setVisibility(View.VISIBLE);
    }

    private void executeTestWeatherCode() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        new WeatherAsyncTask().execute(sp.getString("editTextPref_SetLocation", "-1"));
    }

    private class WeatherAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {


        @Override
        protected void onPreExecute() { super.onPreExecute();
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
}