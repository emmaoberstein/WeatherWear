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
        View rootView = inflater.inflate(R.layout.closet_activity_vertical, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.my_closet);

        final at.markushi.ui.CircleButton button = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.shirtButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getActivity(), ChooseCategoryActivity.class);
                intent.putExtra("CATEGORY_TYPE", "Shirts");
                startActivity(intent);

            }
        });

        final at.markushi.ui.CircleButton bottomsButton = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.bottomsButton);
        bottomsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getActivity(), ChooseCategoryActivity.class);
                intent.putExtra("CATEGORY_TYPE","Bottoms");
                startActivity(intent);

            }
        });

       final at.markushi.ui.CircleButton outerwearButton = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.outerwearButton);
       outerwearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getActivity(), ChooseCategoryActivity.class);
                intent.putExtra("CATEGORY_TYPE","Outerwear");
                startActivity(intent);

            }
        });

        final at.markushi.ui.CircleButton shoesButton = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.shoesButton);
        shoesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getActivity(), ChooseCategoryActivity.class);
                intent.putExtra("CATEGORY_TYPE","Shoes");
                startActivity(intent);

            }
        });

        final at.markushi.ui.CircleButton accessoriesButton = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.accessoriesButton);
        accessoriesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getActivity(), ChooseCategoryActivity.class);
                intent.putExtra("CATEGORY_TYPE","Accessories");
                startActivity(intent);

            }
        });

        final at.markushi.ui.CircleButton dressButton = (at.markushi.ui.CircleButton) rootView.findViewById(R.id.dressButton);
        dressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getActivity(), DisplayCategoryActivity.class);
                intent.putExtra("CATEGORY_TYPE","Dresses");
                intent.putExtra("SUBCATEGORY_TYPE","Dresses");
                startActivity(intent);
            }
        });

        return rootView;
    }

}
