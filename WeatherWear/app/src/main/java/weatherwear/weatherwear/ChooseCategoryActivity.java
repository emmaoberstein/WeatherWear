package weatherwear.weatherwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by Emma on 2/27/16.
 */
public class ChooseCategoryActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(extras.getString("CATEGORY_TYPE"));
            if (extras.getString("CATEGORY_TYPE").equals("Shirts")) {
                setContentView(R.layout.shirt_categories);
            } else if (extras.getString("CATEGORY_TYPE").equals("Bottoms")) {
                setContentView(R.layout.bottoms_categories);
            } else if (extras.getString("CATEGORY_TYPE").equals("Outerwear")) {
                setContentView(R.layout.outerwear_categories);
            } else if (extras.getString("CATEGORY_TYPE").equals("Accessories")) {
                setContentView(R.layout.accessories_categories);
            } else {
                setContentView(R.layout.shoes_categories);
            }
        }
    }
        public void showCategory(View v) {
            String[] category = v.getResources().getResourceName(v.getId()).split("/");
            Intent intent = new Intent(this, DisplayCategoryActivity.class);
            intent.putExtra("CATEGORY_TYPE",category[1]);
            startActivity(intent);
        }
}
