package weatherwear.weatherwear;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Created by Emma on 2/16/16.
 */
public class OutfitFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alarm_fragment, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.todays_outfit);
        setHasOptionsMenu(true);
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
                Toast.makeText(getActivity(), "For Show and Tell 2", Toast.LENGTH_SHORT).show();
                executeTestWeatherCode();
                return true;
            default:
                return false;
        }
    }

    private void executeTestWeatherCode() {
        new WeatherAsyncTask().execute("03755");
    }

    private class WeatherAsyncTask extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(String... params) {
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

                Log.d("Current Wind Chill", windChillTemperature);
                Log.d("Current Temperature", currentTemperature);
                Log.d("Current Condition", currentCondition);
                Log.d("Current Low", todayLow);
                Log.d("Current High", todayHigh);

                Log.d("Tomorrow Condition", tomorrowCondition);
                Log.d("Tomorrow Low", tomorrowLow);
                Log.d("Tomorrow High", tomorrowHigh);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}