package weatherwear.weatherwear;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Emma on 2/27/16.
 */
public class ChooseCategoryActivity extends AppCompatActivity {

    // Handle the creation to load in proper contentView
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String categoryType = extras.getString(Utils.CATEGORY_TYPE, "Tops");
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(categoryType);
            if (categoryType.equals("Tops")) {
                setContentView(R.layout.shirt_categories);
            } else if (categoryType.equals("Bottoms")) {
                setContentView(R.layout.bottoms_categories);
            } else if (categoryType.equals("Outerwear")) {
                setContentView(R.layout.outerwear_categories);
            } else if (categoryType.equals("Accessories")) {
                setContentView(R.layout.accessories_categories);
            } else {
                setContentView(R.layout.shoes_categories);
            }
        }
    }

    // Handles starting the proper subcategory display
    public void showCategory(View v) {
        String[] category = v.getResources().getResourceName(v.getId()).split("/");
        Intent intent = new Intent(this, DisplayCategoryActivity.class);
        // Passes category type and subcategory type to the DisplayCategoryActivity
        intent.putExtra(Utils.CATEGORY_TYPE, getIntent().getExtras().getString(Utils.CATEGORY_TYPE));
        intent.putExtra(Utils.SUBCATEGORY_TYPE, category[1]);
        startActivity(intent);
    }
}
