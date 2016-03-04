package weatherwear.weatherwear;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

import weatherwear.weatherwear.database.ClothingDatabaseHelper;
import weatherwear.weatherwear.database.ClothingItem;

/**
 * Created by Emma on 2/17/16.
 */
public class DisplayCategoryActivity extends AppCompatActivity {

    public static int OPEN_CODE = 30;

    String mCategoryName = "";
    private GridView gridView;
    private ClothingItemGridAdapter mAdapter;
    private ArrayList<ClothingItem> items;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity);

        //getApplicationContext().deleteDatabase(ClothingDatabaseHelper.DATABASE_NAME);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ActionBar actionBar = getSupportActionBar();

            String[] category = extras.getString("SUBCATEGORY_TYPE").split("_");
            for (int i=0; i < category.length; i++) {
                mCategoryName+= Character.toUpperCase(category[i].charAt(0)) + category[i].substring(1) + " ";
            }
            mCategoryName = mCategoryName.trim();
            Log.d("CATEGORY",mCategoryName);

            actionBar.setTitle(mCategoryName);
        }

        // Initialize grids
        mAdapter = new ClothingItemGridAdapter(getApplicationContext());
        gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DisplayCategoryActivity.this, DisplayItemActivity.class);
                intent.putExtra("CALL_REASON", "LOAD");
                // Category Type
                intent.putExtra("CATEGORY_TYPE", getIntent().getExtras().getString("CATEGORY_TYPE"));
                intent.putExtra("SUBCATEGORY_TYPE", mCategoryName);

                intent.putExtra("ITEM_ID", items.get(position).getId());
                startActivityForResult(intent, OPEN_CODE);
            }
        });

        // Call runnable to initialize items (don't block UI)
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateClothingItems();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.plus_menu, menu);

        return true;
    }

    public void changeImage(){
        final Intent intent = new Intent(this, DisplayItemActivity.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Image");
        // Set up click listener, firing intents open camera or gallery based on
        // choice.
        builder.setItems(new String[]{"Select from Gallery", "Take a Picture"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Item can be: ID_PHOTO_PICKER_FROM_CAMERA
                        // or ID_PHOTO_PICKER_FROM_GALLERY
                        intent.putExtra("CALL_REASON", "NEW");
                        intent.putExtra("IMAGE_TYPE", item);
                        intent.putExtra("CATEGORY_TYPE", getIntent().getExtras().getString("CATEGORY_TYPE"));
                        intent.putExtra("SUBCATEGORY_TYPE", mCategoryName);
                        startActivityForResult(intent, OPEN_CODE);
                    }
                });
        builder.create().show();
    }

    public void addItem(MenuItem item) {
       changeImage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_CODE) {
            updateClothingItems();
        }
    }

    private void updateClothingItems() {
        ClothingDatabaseHelper dbHelper = new ClothingDatabaseHelper(getApplicationContext());
        ArrayList<ClothingItem> items = dbHelper.fetchEntriesInCategory(mCategoryName);
        this.items = items;
        mAdapter.setClothingItems(items);
        gridView.setAdapter(mAdapter);
    }

    public class ClothingItemGridAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<ClothingItem> clothingItems;

        public ClothingItemGridAdapter(Context c) {
            mContext = c;
            clothingItems = new ArrayList<ClothingItem>();
        }

        public int getCount() {
            return clothingItems.size();
        }

        public Object getItem(int position) {
            return clothingItems.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public void setClothingItems(ArrayList<ClothingItem> items) {
            clothingItems.clear();
            clothingItems.addAll(items);
        }

        // create a new ImageView for each ClothingItem referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);

                int imageWidth = gridView.getColumnWidth();
                // set image to width (to fill columns)
                imageView.setLayoutParams(new AbsListView.LayoutParams(imageWidth, imageWidth));
                imageView.setMaxHeight(imageWidth);
                imageView.setMaxWidth(imageWidth);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageBitmap(clothingItems.get(position).getImage());
            return imageView;
        }
    }
}
