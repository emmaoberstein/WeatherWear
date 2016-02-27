package weatherwear.weatherwear;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
    private ArrayAdapter mSubArrayAdapter;
    private ArrayList<String> mSubArray;
    private static HashMap<String, String[]> types;

    static {
        types = new HashMap<>();
        types.put("Tops", new String[] {"Long Sleeve", "Short Sleeve", "Sleeveless", "Cardigan"});
        types.put("Bottoms", new String[] {"Pants", "Shorts", "Skirts"});
        types.put("Outerwear", new String[] {"Coats", "Raincoats"});
        types.put("Accessories", new String[] {"Scarves", "Hats", "Gloves", "Bags"});
        types.put("Jewelry", new String[] {"Necklaces", "Bracelets", "Earrings"});
        types.put("Shoes", new String[] {"Boots", "Rain Boots", "Snow Boots", "Sandals", "Sneakers", "Heels"});
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("New Item");
        }

        // identify ImageViews
        mImageView = (ImageView)findViewById(R.id.item_image);
        //changeImage();

        // Type spinners setup
        mMainTypeSpinner = (Spinner)findViewById(R.id.type_spinner_main);
        mSubTypeSpinner = (Spinner)findViewById(R.id.type_spinner_sub);

        ArrayAdapter<String> mainArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String> (types.keySet()));
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
        mSubArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mSubArray);
        mSubArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSubTypeSpinner.setAdapter(mSubArrayAdapter);
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
        if (resultCode != RESULT_OK)
            return;

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
}
