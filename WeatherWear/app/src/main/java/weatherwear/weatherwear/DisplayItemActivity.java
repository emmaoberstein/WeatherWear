package weatherwear.weatherwear;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import weatherwear.weatherwear.clothing.ClothingDatabaseHelper;
import weatherwear.weatherwear.clothing.ClothingItem;

/**
 * Created by Emma on 2/17/16.
 */
public class DisplayItemActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
    public static final int REQUEST_CODE_SELECT_FROM_GALLERY = 1;

    private Uri mImageCaptureUri, mTempUri;
    private Boolean clickedFromCam = false;
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

    private boolean editing = false;
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
                // ignore
            }
        });

        if (extras != null) {
            mMainTypeSpinner.setSelection(getMainIndex(extras.getString(Utils.CATEGORY_TYPE)));

            // Manually instantiate as setSelection doesn't consistently run 'OnItemSelected'
            String[] options = types.get(extras.getString(Utils.CATEGORY_TYPE));
            mSubArray = new ArrayList<>(Arrays.asList(options));
            mSubArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, mSubArray);
            mSubArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSubTypeSpinner.setAdapter(mSubArrayAdapter);

            // Set the proper subcategory selection
            mSubTypeSpinner.setSelection(getSubIndex(extras.getString(Utils.CATEGORY_TYPE), extras.getString(Utils.SUBCATEGORY_TYPE)));

            ActionBar actionBar = getSupportActionBar();

            if (extras.get(DisplayCategoryActivity.CALL_REASON).equals(DisplayCategoryActivity.CALL_REASON_NEW)) { // a new one
                actionBar.setTitle(R.string.display_item_new_item_bar);
                if (extras.get(DisplayCategoryActivity.IMAGE_TYPE) == 1) { // Take a picture
                    changeImage();
                } else { // Load from gallery
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, REQUEST_CODE_SELECT_FROM_GALLERY);
                }
            } else { // load an old one
                actionBar.setTitle(R.string.display_item_edit_item_bar);
                // Set editing flag to true so the program knows it isn't a new one
                editing = true;

                item = dbHelper.fetchItemByIndex(extras.getLong(DisplayCategoryActivity.ITEM_ID));

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
        } else {
            String[] options = types.get("Tops");
            mSubArray = new ArrayList<>(Arrays.asList(options));
            mSubArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, mSubArray);
            mSubArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSubTypeSpinner.setAdapter(mSubArrayAdapter);
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
                clickedFromCam = true;
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
                    clickedFromCam = false;
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
            mImageView.setImageResource(0);
            mImageView.setImageURI(mTempUri);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Adds the delete option to the top menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);
        return true;
    }

    // Handles pressing the delete button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_button: // If you click delete, either remove it, or just cancel w/ message
                if (editing) {
                    new UpdateItemAsyncTask(dbHelper, "Delete").execute(this.item); // delete it
                } else {
                    Toast.makeText(getApplicationContext(), R.string.display_item_cancel_message, Toast.LENGTH_SHORT).show();
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Handles all toggle buttons being pressed
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
            view.getBackground().setColorFilter(0x994E9A26, PorterDuff.Mode.MULTIPLY); // shade green
        else
            view.getBackground().clearColorFilter(); // remove shade
    }

    // Handler for 'Cancel' button click
    public void cancelItemClicked(View view) {
        Toast.makeText(getApplicationContext(), R.string.display_item_cancel_message, Toast.LENGTH_SHORT).show();
        finish();
    }

    // Check that at least one season has been clicked
    private boolean hasSelectedSeason() {
        return (this.fall || this.winter || this.spring || this.summer);
    }

    // Handles the save item being clicked
    public void saveItemClicked(View view) {
        // Let's save this
        if (mCycleLength.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.display_item_minimum_warning, Toast.LENGTH_SHORT).show();
        } else if (!hasSelectedSeason()) {
            Toast.makeText(getApplicationContext(), R.string.display_item_season_warning, Toast.LENGTH_SHORT).show();
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
            Bitmap image = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            Bitmap resizedImage = Bitmap.createScaledBitmap(image, 200, 200, false);
            item.setImage(resizedImage);

            // Add it to the database (or update it), tell the user
            if (item.getId() == 0) { // new item
                new UpdateItemAsyncTask(dbHelper, "New").execute(item);
            } else {
                new UpdateItemAsyncTask(dbHelper, "Update").execute(item);
            }
        }
    }

    // Asynchronously handles all clothing updating (creation, updating, deletion)
    private class UpdateItemAsyncTask extends AsyncTask<ClothingItem, Void, Void> {
        private ClothingDatabaseHelper dbHelper;
        private String task;

        public UpdateItemAsyncTask(ClothingDatabaseHelper dbHelper, String task) {
            this.dbHelper = dbHelper;
            this.task = task;
        }

        @Override
        protected Void doInBackground(ClothingItem... params) {
            ClothingItem item = params[0];
            if (task.equals("Delete")) { // deletes the item
                dbHelper.removeItem(item.getId());
            } else if (task.equals("New")) { // creates a new item
                dbHelper.insertItem(item);
            } else { // updates the item
                dbHelper.updateItem(item);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Handles toast display post-action
            if (task.equals("Delete")) {
                Toast.makeText(getApplicationContext(), R.string.display_item_deleted_message, Toast.LENGTH_SHORT).show();
            } else if (task.equals("New")) {
                Toast.makeText(getApplicationContext(), "New '" + item.getType() + "' item created!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.display_item_updated_message, Toast.LENGTH_SHORT).show();
            }
            // Close the activity
            Intent i = new Intent();
            setResult(DisplayCategoryActivity.OPEN_CODE, i);
            finish();
        }
    }
}