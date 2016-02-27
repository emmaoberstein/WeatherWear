package weatherwear.weatherwear;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;

/**
 * Created by Emma on 2/17/16.
 */
public class DisplayCategoryActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
    public static final int REQUEST_CODE_SELECT_FROM_GALLERY = 1;

    public static final int REQUEST_CODE_CROP_PHOTO = 2;
    private Uri mImageCaptureUri, mTempUri;
    private Boolean stateChnaged = false, cameraClicked = false,clickedFromCam=false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ActionBar actionBar = getSupportActionBar();

            String categoryName = "";
            String[] category = extras.getString("CATEGORY_TYPE").split("_");
            for (int i=0; i < category.length; i++) {
                categoryName+= Character.toUpperCase(category[i].charAt(0)) + category[i].substring(1) + " ";
            }

            actionBar.setTitle(categoryName);
        }
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
                        intent.putExtra("IMAGE_TYPE", item);
                        intent.putExtra("CATEGORY_TYPE", getIntent().getExtras().getString("CATEGORY_TYPE"));
                        startActivity(intent);
                    }
                });
        builder.create().show();

    }

    public void addItem(MenuItem item) {
       changeImage();
    }
}
