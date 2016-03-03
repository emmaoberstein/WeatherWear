package weatherwear.weatherwear;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import weatherwear.weatherwear.database.ClothingDatabaseHelper;
import weatherwear.weatherwear.database.ClothingItem;

/**
 * Created by Emma on 2/17/16.
 */
public class DisplayItemActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
    public static final int REQUEST_CODE_SELECT_FROM_GALLERY = 1;

    public static final int REQUEST_CODE_CROP_PHOTO = 2;
    private Uri mImageCaptureUri, mTempUri;
    private Boolean stateChnaged = false, cameraClicked = false,clickedFromCam=false;
    private ImageView mImageView;

    private Spinner mMainTypeSpinner;
    private Spinner mSubTypeSpinner;
    private EditText mCycleLength;

    private boolean fall = false;
    private boolean winter = false;
    private boolean spring = false;
    private boolean summer = false;

    private ArrayAdapter mSubArrayAdapter;
    private ArrayList<String> mSubArray;
    private static HashMap<String, String[]> types;

    private boolean cancelToDelete = false;
    private ClothingDatabaseHelper dbHelper;
    private ClothingItem item = new ClothingItem();

    static {
        types = new HashMap<>();
        types.put("Tops", new String[] {"Long Sleeve Shirts", "Short Sleeve Shirts", "Sleeveless Shirts", "Cardigan"});
        types.put("Bottoms", new String[] {"Pants", "Shorts", "Skirts"});
        types.put("Outerwear", new String[] {"Coats", "Raincoats"});
        types.put("Accessories", new String[] {"Scarves", "Hats", "Gloves", "Bags"});
        types.put("Dresses", new String[] {});
        types.put("Shoes", new String[] {"Boots", "Rain Boots", "Snow Boots", "Sandals", "Sneakers", "Heels"});
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);

        Bundle extras = getIntent().getExtras();

        // set up database helper
        dbHelper = new ClothingDatabaseHelper(getApplicationContext());

        // identify ImageViews
        mImageView = (ImageView)findViewById(R.id.item_image);

        // Cycle length
        mCycleLength = (EditText)findViewById(R.id.cycle_length);

        // Type spinners setup
        mMainTypeSpinner = (Spinner)findViewById(R.id.type_spinner_main);
        mSubTypeSpinner = (Spinner)findViewById(R.id.type_spinner_sub);

        ArrayAdapter<String> mainArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, new ArrayList<String> (types.keySet()));
        mainArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMainTypeSpinner.setAdapter(mainArrayAdapter);

        mMainTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] options = types.get(parent.getItemAtPosition(position).toString());
                mSubArray.clear();
                mSubArray.addAll(new ArrayList<>(Arrays.asList(options)));
                mSubArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        String[] options = types.get("Tops");
        mSubArray = new ArrayList<>(Arrays.asList(options));
        mSubArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, mSubArray);
        mSubArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSubTypeSpinner.setAdapter(mSubArrayAdapter);

        if (extras != null) {
            mMainTypeSpinner.setSelection(getMainIndex(extras.getString("CATEGORY_TYPE")));
            mSubTypeSpinner.setSelection(getSubIndex(extras.getString("CATEGORY_TYPE"), extras.getString("SUBCATEGORY_TYPE")));

            ActionBar actionBar = getSupportActionBar();

            if (extras.get("CALL_REASON").equals("NEW")) { // a new one
                actionBar.setTitle("New Item");
                if (extras.get("IMAGE_TYPE") == 1) {
                    changeImage();
                } else {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, REQUEST_CODE_SELECT_FROM_GALLERY);
                }
            } else { // load an old one
                actionBar.setTitle("Edit Item");
                ((Button) findViewById(R.id.cancel_item)).setText("Delete");
                cancelToDelete = true;

                item = dbHelper.fetchEntryByIndex(extras.getLong("ITEM_ID"));

                // Set the image
                mImageView.setImageBitmap(item.getImage());

                // Set all of the season buttons
                if (item.getFall())
                    fallButtonClicked(findViewById(R.id.fall_button));
                if (item.getWinter())
                    winterButtonClicked(findViewById(R.id.winter_button));
                if (item.getSpring())
                    springButtonClicked(findViewById(R.id.spring_button));
                if (item.getSummer())
                    summerButtonClicked(findViewById(R.id.summer_button));

                // Set cycle length
                mCycleLength.setText("" + item.getCycleLength());
            }
        }
    }

    public void changeImage(){
        Intent intent;
        // Take photo from camera，
        // Construct an intent with action
        // MediaStore.ACTION_IMAGE_CAPTURE
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Construct temporary image path and name to save the taken
        // photo
        mImageCaptureUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "tmp_"
                + String.valueOf(System.currentTimeMillis()) + ".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        intent.putExtra("return-data", true);
        try {
            // Start a camera capturing activity
            // REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
            // defined to identify the activity in onActivityResult()
            // when it returns
            startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_TAKE_FROM_CAMERA:
                // Send image taken from camera for cropping
                beginCrop(mImageCaptureUri);
                clickedFromCam=true;
                break;

            case REQUEST_CODE_SELECT_FROM_GALLERY:
                Uri srcUri = data.getData();
                beginCrop(srcUri);
                break;

            case Crop.REQUEST_CROP:
                // Update image view after image crop
                // Set the picture image in UI
                handleCrop(resultCode, data);

                // Delete temporary image taken by camera after crop.
                if(clickedFromCam) {
                    File f = new File(mImageCaptureUri.getPath());
                    if (f.exists())
                        f.delete();
                    clickedFromCam=false;
                }

                break;
        }
    }

    private int getMainIndex(String type) {
        //return Arrays.asList(types.values()).indexOf(type);
        if (type.equals("Outerwear")) return 0;
        else if (type.equals("Dresses")) return 1;
        else if (type.equals("Tops")) return 2;
        else if (type.equals("Accessories")) return 3;
        else if (type.equals("Shoes")) return 4;
        else if (type.equals("Bottoms")) return 5;
        return 0;
    }

    private int getSubIndex(String category, String subcategory) {
        return Arrays.asList(types.get(category)).indexOf(subcategory);
    }

    /** Method to start Crop activity using the library
     *	Earlier the code used to start a new intent to crop the image,
     *	but here the library is handling the creation of an Intent, so you don't
     * have to.
     *  **/
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            mTempUri = Crop.getOutput(result);
            cameraClicked=true;
            mImageView.setImageResource(0);
            mImageView.setImageURI(mTempUri);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.x_menu, menu);
        return true;
    }

    public void cancelItem(MenuItem item) {
        finish();
    }

    public void fallButtonClicked(View view) {
        this.fall ^= true;
        toggleButton(view, this.fall);
    }

    public void winterButtonClicked(View view) {
        this.winter ^= true;
        toggleButton(view, this.winter);
    }

    public void springButtonClicked(View view) {
        this.spring ^= true;
        toggleButton(view, this.spring);
    }

    public void summerButtonClicked(View view) {
        this.summer ^= true;
        toggleButton(view, this.summer);
    }

    private void toggleButton(View view, boolean opt) {
        if (opt)
            view.getBackground().setColorFilter(0x994E9A26, PorterDuff.Mode.MULTIPLY);
        else
            view.getBackground().clearColorFilter();
    }

    public void cancelItemClicked(View view) {
        if (cancelToDelete) {
            dbHelper.removeEntry(item.getId());
            Toast.makeText(getApplicationContext(), "Item deleted!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private boolean hasSelectedSeason() {
        return (this.fall || this.winter || this.spring || this.summer);
    }

    public void saveItemClicked(View view) {
        // Let's save this
        if (mCycleLength.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Input a minimum number of days between wears!", Toast.LENGTH_SHORT).show();
        } else if (!hasSelectedSeason()) {
            Toast.makeText(getApplicationContext(), "Item must be wearable in at least one season!", Toast.LENGTH_SHORT).show();
        } else {
            // Set the category
            String category = (String)mSubTypeSpinner.getSelectedItem();
            if (category == null)
                category = (String)mMainTypeSpinner.getSelectedItem();
            item.setType(category);
            // Set the cycle length
            item.setCycleLength(Integer.parseInt(mCycleLength.getText().toString()));
            // Set the seasons
            item.setFall(fall);
            item.setWinter(winter);
            item.setSpring(spring);
            item.setSummer(summer);
            // Set the image
            item.setImage(((BitmapDrawable) mImageView.getDrawable()).getBitmap());

            // Add it to the database (or update it), tell the user
            if (item.getId() == 0) { // new item
                dbHelper.insertItem(item);
                Toast.makeText(getApplicationContext(), "New '" + category + "' item created!", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.removeEntry(item.getId()); // temporary updating
                dbHelper.insertItem(item);
                Toast.makeText(getApplicationContext(), "Item updated!", Toast.LENGTH_SHORT).show();
            }

            // Close the activity
            Intent i = new Intent();
            setResult(DisplayCategoryActivity.OPEN_CODE, i);
            finish();
        }
    }
}
