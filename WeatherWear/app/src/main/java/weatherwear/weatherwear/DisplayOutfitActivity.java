package weatherwear.weatherwear;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import weatherwear.weatherwear.database.ClothingDatabaseHelper;
import weatherwear.weatherwear.database.ClothingItem;
import weatherwear.weatherwear.vacation.OutfitDatabaseHelper;
import weatherwear.weatherwear.vacation.OutfitModel;
import weatherwear.weatherwear.vacation.VacationOutfitsActivity;

/*
 * Created by Emma on 3/6/16.
 */
public class DisplayOutfitActivity extends AppCompatActivity {
    OutfitDatabaseHelper mOutfitDbHelper = new OutfitDatabaseHelper(this);
    OutfitModel mOutfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_outfit_fragment);
        Intent i = getIntent();
        mOutfit = mOutfitDbHelper.fetchEntryByIndex(i.getLongExtra(VacationOutfitsActivity.ID_KEY, 1));
    }

    @Override
    public void onResume(){
        super.onResume();

        String day = mOutfit.getmDay();
        ((TextView) findViewById(R.id.welcome)).setText("Outfit for Day " + day);

        String date = Utils.parseVacationDate(mOutfit.getmDate());
        if (date != null) ((TextView) findViewById(R.id.outfit_date)).setText("Outfit date: " + date);

        String location = mOutfit.getmLocation();
        if (location != null) ((TextView) findViewById(R.id.location)).setText("Location: " + location);

        String high = "" + mOutfit.getmHigh();
        if (high != null) ((TextView) findViewById(R.id.high)).setText("High: " + high);

        String low = "" + mOutfit.getmLow();
        if (low != null) ((TextView) findViewById(R.id.low)).setText("Low: " + low);

        String condition = mOutfit.getmCondition();
        if (condition != null) ((TextView) findViewById(R.id.condition)).setText("Condition: " + condition);

        new LoadOutfitAsyncTask().execute(mOutfit.getmTop(), mOutfit.getmBottom(), mOutfit.getmShoes(),
                mOutfit.getmOuterwear(), mOutfit.getmGloves(), mOutfit.getmHat(), mOutfit.getmScarves());
    }

    private class LoadOutfitAsyncTask extends AsyncTask<Long, Void, ArrayList<ClothingItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<ClothingItem> clothes) {
            super.onPostExecute(clothes);

            findViewById(R.id.outfit_description).setVisibility(View.VISIBLE);

            if (clothes.get(0) != null)  {
                ((findViewById(R.id.top))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.top_image))).setImageBitmap(clothes.get(0).getImage());
                ((findViewById(R.id.top_group))).setVisibility(View.VISIBLE);
            } else {
                ((findViewById(R.id.top))).setVisibility(View.GONE);
                ((findViewById(R.id.top_group))).setVisibility(View.GONE);
            }

            if (clothes.get(1) != null)  {
                ((findViewById(R.id.bottom))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.bottom_image))).setImageBitmap(clothes.get(1).getImage());
                ((findViewById(R.id.bottom_group))).setVisibility(View.VISIBLE);
            }else {
                ((findViewById(R.id.bottom))).setVisibility(View.GONE);
                ((findViewById(R.id.bottom_group))).setVisibility(View.GONE);
            }

            if (clothes.get(2) != null)  {
                ((findViewById(R.id.shoes))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.shoes_image))).setImageBitmap(clothes.get(2).getImage());
                ((findViewById(R.id.shoes_group))).setVisibility(View.VISIBLE);
            } else {
                ((findViewById(R.id.shoes))).setVisibility(View.GONE);
                ((findViewById(R.id.shoes_group))).setVisibility(View.GONE);
            }

            if (clothes.get(3) != null)  {
                ((findViewById(R.id.outerwear))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.outerwear_image))).setImageBitmap(clothes.get(3).getImage());
                ((findViewById(R.id.outerwear_group))).setVisibility(View.VISIBLE);
            } else {
                ((findViewById(R.id.outerwear))).setVisibility(View.GONE);
                ((findViewById(R.id.outerwear_group))).setVisibility(View.GONE);
            }

            if (clothes.get(4) != null)  {
                ((findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.gloves_image))).setImageBitmap(clothes.get(4).getImage());
                ((findViewById(R.id.gloves_group))).setVisibility(View.VISIBLE);
            } else {
                ((findViewById(R.id.gloves_group))).setVisibility(View.GONE);
            }

            if (clothes.get(5) != null)  {
                ((findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.hats_image))).setImageBitmap(clothes.get(5).getImage());
                ((findViewById(R.id.hats_group))).setVisibility(View.VISIBLE);
            } else {
                ((findViewById(R.id.hats_group))).setVisibility(View.GONE);
            }

            if (clothes.get(6) != null)  {
                ((findViewById(R.id.accessories))).setVisibility(View.VISIBLE);
                ((ImageView) (findViewById(R.id.scarves_image))).setImageBitmap(clothes.get(6).getImage());
                ((findViewById(R.id.scarves_group))).setVisibility(View.VISIBLE);
            } else {
                ((findViewById(R.id.scarves_group))).setVisibility(View.GONE);
            }

            if (clothes.get(4) == null && clothes.get(5) == null && clothes.get(6) == null) {
                ((findViewById(R.id.accessories))).setVisibility(View.GONE);
            }
        }

        @Override
        protected ArrayList<ClothingItem> doInBackground(Long... params) {
            ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getApplicationContext());
            ArrayList<ClothingItem> clothes = new ArrayList<ClothingItem>();
            
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

            return clothes;
        }
    }
}
