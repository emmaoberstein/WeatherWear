package weatherwear.weatherwear;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Emma on 2/16/16.
 */
public class ClosetFragment extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Infaltes the view, and sets the action bar title
        View rootView = inflater.inflate(R.layout.closet_fragment, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.my_closet);

        // Get all buttons and link them to their proper category
        final at.markushi.ui.CircleButton topsButton = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.shirtButton);
        topsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerCategory("Tops");

            }
        });

        final at.markushi.ui.CircleButton bottomsButton = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.bottomsButton);
        bottomsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerCategory("Bottoms");
            }
        });

       final at.markushi.ui.CircleButton outerwearButton = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.outerwearButton);
       outerwearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerCategory("Outerwear");
            }
        });

        final at.markushi.ui.CircleButton shoesButton = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.shoesButton);
        shoesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerCategory("Shoes");
            }
        });

        final at.markushi.ui.CircleButton accessoriesButton = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.accessoriesButton);
        accessoriesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerCategory("Accessories");
            }
        });

        final at.markushi.ui.CircleButton dressButton = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.dressButton);
        dressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerCategory("Dresses");
            }
        });

        return rootView;
    }

    // Calls the correct intent for the type
    private void triggerCategory(String type) {
        Intent intent;
        if (type.equals("Dresses")) {
            intent = new Intent(getActivity(), DisplayCategoryActivity.class);
            intent.putExtra(Utils.SUBCATEGORY_TYPE, "Dresses");
        } else {
            intent = new Intent(getActivity(), ChooseCategoryActivity.class);
        }
        intent.putExtra(Utils.CATEGORY_TYPE,type);
        startActivity(intent);
    }

}
