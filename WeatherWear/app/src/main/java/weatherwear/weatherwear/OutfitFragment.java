package weatherwear.weatherwear;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.current_outfit_fragment, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.todays_outfit);
        setHasOptionsMenu(true);
        setRetainInstance(true);
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
                Intent intent = new Intent(getActivity(), NewOutfitActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        String mKey = getString(R.string.preference_name);
        SharedPreferences mPrefs = getActivity().getSharedPreferences(mKey, getActivity().MODE_PRIVATE);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String name = "";
        if (!sp.getString("editTextPref_DisplayName", "-1").equals("-1")) {
            name = sp.getString("editTextPref_DisplayName", "-1") + "'s ";
        }

        ((TextView)getView().findViewById(R.id.welcome)).setText(name + "Latest Outfit");

        String date = mPrefs.getString("DATE_INDEX", null);
        if (date != null) ((TextView)getView().findViewById(R.id.outfit_date)).setText(date);


        String location = mPrefs.getString("LOCATION_INDEX", null);
        if (location != null) ((TextView)getView().findViewById(R.id.location)).setText(location);

        Integer high = mPrefs.getInt("HIGH_INDEX", -500);
        if (high != -500) {
            if (!sp.getString("listPref_Temp","-1").equals("Celsius")) {
                ((TextView) (getView().findViewById(R.id.high))).setText("High: " + high + "°F");
            } else {
                ((TextView) (getView().findViewById(R.id.high))).setText("Low: " +  String.valueOf(Math.round((((Double.valueOf(high)-32)*5/9)) * 10) / 10) + "°C");
            }
        }

        Integer low = mPrefs.getInt("LOW_INDEX", -500);
        if (low != -500) {
            if (!sp.getString("listPref_Temp","-1").equals("Celsius")) {
                ((TextView) (getView().findViewById(R.id.low))).setText("Low: " + low + "°F");
            } else {
               ((TextView) (getView().findViewById(R.id.low))).setText("Low: " +  String.valueOf(Math.round((((Double.valueOf(low)-32)*5/9)) * 10) / 10) + "°C");
            }
        }

        String condition = mPrefs.getString("CONDITION_INDEX", null);
        if (condition != null) ((TextView)getView().findViewById(R.id.condition)).setText(condition);

        new LoadOutfitAsyncTask().execute();

    }

    private class LoadOutfitAsyncTask extends AsyncTask<Void, Void, ArrayList<ClothingItem>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<ClothingItem> clothes) {
            super.onPostExecute(clothes);

            getView().findViewById(R.id.outfit_description).setVisibility(View.VISIBLE);

            if (clothes.get(0) != null)  {
                ((getView().findViewById(R.id.top))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.top_image))).setImageBitmap(clothes.get(0).getImage());
                ((getView().findViewById(R.id.top_group))).setVisibility(View.VISIBLE);
            } else {
                ((getView().findViewById(R.id.top))).setVisibility(View.GONE);
                ((getView().findViewById(R.id.top_group))).setVisibility(View.GONE);
            }

            if (clothes.get(1) != null)  {
                ((getView().findViewById(R.id.bottom))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.bottom_image))).setImageBitmap(clothes.get(1).getImage());
                ((getView().findViewById(R.id.bottom_group))).setVisibility(View.VISIBLE);
            }else {
                ((getView().findViewById(R.id.bottom))).setVisibility(View.GONE);
                ((getView().findViewById(R.id.bottom_group))).setVisibility(View.GONE);
            }

            if (clothes.get(2) != null)  {
                ((getView().findViewById(R.id.shoes))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.shoes_image))).setImageBitmap(clothes.get(2).getImage());
                ((getView().findViewById(R.id.shoes_group))).setVisibility(View.VISIBLE);
            } else {
                ((getView().findViewById(R.id.shoes))).setVisibility(View.GONE);
                ((getView().findViewById(R.id.shoes_group))).setVisibility(View.GONE);
            }

            if (clothes.get(3) != null)  {
                ((getView().findViewById(R.id.outerwear))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.outerwear_image))).setImageBitmap(clothes.get(3).getImage());
                ((getView().findViewById(R.id.outerwear_group))).setVisibility(View.VISIBLE);
            } else {
                ((getView().findViewById(R.id.outerwear))).setVisibility(View.GONE);
                ((getView().findViewById(R.id.outerwear_group))).setVisibility(View.GONE);
            }

            if (clothes.get(4) != null)  {
                ((getView().findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.gloves_image))).setImageBitmap(clothes.get(4).getImage());
                ((getView().findViewById(R.id.gloves_group))).setVisibility(View.VISIBLE);
            } else {
                ((getView().findViewById(R.id.gloves_group))).setVisibility(View.GONE);
            }

            if (clothes.get(5) != null)  {
                ((getView().findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.hats_image))).setImageBitmap(clothes.get(5).getImage());
                ((getView().findViewById(R.id.hats_group))).setVisibility(View.VISIBLE);
            } else {
                ((getView().findViewById(R.id.hats_group))).setVisibility(View.GONE);
            }

            if (clothes.get(6) != null)  {
                ((getView().findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
                ((ImageView) (getView().findViewById(R.id.scarves_image))).setImageBitmap(clothes.get(6).getImage());
                ((getView().findViewById(R.id.scarves_group))).setVisibility(View.VISIBLE);
            } else {
                ((getView().findViewById(R.id.scarves_group))).setVisibility(View.GONE);
            }

            if (clothes.get(4) == null && clothes.get(5) == null && clothes.get(6) == null) {
                ((getView().findViewById(R.id.accessories))).setVisibility(View.GONE);
            }
        }

        @Override
        protected ArrayList<ClothingItem> doInBackground(Void... params) {
            ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getActivity());
            ArrayList<ClothingItem> clothes = new ArrayList<ClothingItem>();
            String mKey = getString(R.string.preference_name);
            SharedPreferences mPrefs = getActivity().getSharedPreferences(mKey, getActivity().MODE_PRIVATE);

            Long top = mPrefs.getLong("TOP_INDEX", -1);
            if (top != -1) {
                clothes.add(dbHelper.fetchEntryByIndex(top));
            } else clothes.add(null);

            Long bottom = mPrefs.getLong("BOTTOM_INDEX", -1);
            if (bottom != -1) {
                clothes.add(dbHelper.fetchEntryByIndex(bottom));
            } else clothes.add(null);

            Long shoes = mPrefs.getLong("SHOES_INDEX", -1);
            if (shoes != -1) {
                clothes.add(dbHelper.fetchEntryByIndex(shoes));
            } else clothes.add(null);

            Long outerwear = mPrefs.getLong("OUTERWEAR_INDEX", -1);
            if (outerwear != -1) {
                clothes.add(dbHelper.fetchEntryByIndex(outerwear));
            } else clothes.add(null);

            Long gloves = mPrefs.getLong("GLOVES_INDEX", -1);
            if (gloves != -1) {
                clothes.add(dbHelper.fetchEntryByIndex(gloves));
            } else clothes.add(null);

            Long hats = mPrefs.getLong("HATS_INDEX", -1);
            if (hats != -1) {
                clothes.add(dbHelper.fetchEntryByIndex(hats));
            } else clothes.add(null);

            Long scarves = mPrefs.getLong("SCARVES_INDEX", -1);
            if (scarves != -1) {
                clothes.add(dbHelper.fetchEntryByIndex(scarves));
            } else clothes.add(null);

            return clothes;
        }
    }

}