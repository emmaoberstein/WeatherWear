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
    private TextView outfitDate;
    private TextView high;
    private TextView low;
    private TextView location;
    private TextView condition;
    private TextView outfit;
    private Double zipCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.outfit_fragment, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.todays_outfit);
        setHasOptionsMenu(true);

        TextView welcomeText = (TextView)rootView.findViewById(R.id.welcome);
        outfitDate = (TextView)rootView.findViewById(R.id.outfit_date);
        high = (TextView)rootView.findViewById(R.id.high);
        low = (TextView)rootView.findViewById(R.id.low);
        condition = (TextView)rootView.findViewById(R.id.condition);
        location = (TextView)rootView.findViewById(R.id.location);
        outfit = (TextView)rootView.findViewById(R.id.outfit);
        setWelcomeMessage(welcomeText);
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
        outfitDate.setText("Outfit Date: " + sdf.format(new Date()));
        location.setText("Location: " + weather.get(0));
        high.setText("High: " + weather.get(1) + "°F");
        low.setText("Low: " + weather.get(2) + "°F");
        condition.setText("Condition: " + weather.get(3));

        ArrayList<String> outfitCriteria = new ArrayList<String>();
        int avgTemp = ((Integer.valueOf(weather.get(1)) + Integer.valueOf(weather.get(2)))/2);

        // top
        if (avgTemp >= 85) {
            outfitCriteria.add("Sleeveless Shirts");
        } else if (avgTemp >= 50) {
            outfitCriteria.add("Short Sleeve Shirts");
        } else {
            outfitCriteria.add("Long Sleeve Shirts");
        }

        // bottom
        if (avgTemp >= 75) {
            outfitCriteria.add("Shorts");
        } else {
            outfitCriteria.add("Pants");
        }

        // shoes
        if (weather.get(3).toLowerCase().contains("snow")) {
            outfitCriteria.add("Snow Boots");
        } else if (weather.get(3).toLowerCase().contains("rain") ||
                weather.get(3).toLowerCase().contains("shower")) {
            outfitCriteria.add("Rain Boots");
            outfitCriteria.add("Raincoats");
        } else if (avgTemp <= 50) {
            outfitCriteria.add("Boots");
        } else if (avgTemp <= 75) {
            outfitCriteria.add("Sneakers");
        } else {
            outfitCriteria.add("Sandals");
        }

        // outerwear
        if (avgTemp <= 50) {
            outfitCriteria.add("Coats");
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
        outfit.setText(o);

      /*  ((getView().findViewById(R.id.top))).setVisibility(View.VISIBLE);
        ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getActivity());
        ArrayList<ClothingItem> items = dbHelper.fetchEntriesInCategory(outfitCriteria.get(0));
        ((ImageView)(getView().findViewById(R.id.top_image))).setImageBitmap(items.get(0).getImage());
        ((getView().findViewById(R.id.top_image))).setVisibility(View.VISIBLE);

        items = dbHelper.fetchEntriesInCategory(outfitCriteria.get(1));
        ((ImageView)(getView().findViewById(R.id.bottom_image))).setImageBitmap(items.get(0).getImage());
        ((getView().findViewById(R.id.bottom_image))).setVisibility(View.VISIBLE);

        items = dbHelper.fetchEntriesInCategory(outfitCriteria.get(2));
        ((ImageView)(getView().findViewById(R.id.shoes_image))).setImageBitmap(items.get(0).getImage());
        ((getView().findViewById(R.id.shoes_image))).setVisibility(View.VISIBLE);

        ((getView().findViewById(R.id.bottom))).setVisibility(View.VISIBLE);
        ((getView().findViewById(R.id.shoes))).setVisibility(View.VISIBLE);*/

    }

    private void executeTestWeatherCode() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!sp.getString("editTextPref_SetLocation\"","-1").equals("-1")) {
            zipCode = Double.valueOf(sp.getString("editTextPref_SetLocation", "-1"));
        }
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
                weatherData.add(params[0]);
                weatherData.add(todayHigh);
                weatherData.add(todayLow);
                weatherData.add(currentCondition);

                Log.d("Location", params[0]);
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