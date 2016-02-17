package weatherwear.weatherwear;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emma on 2/16/16.
 */
public class ClosetFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.closet_activity, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.my_closet);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.listview_layout, getResources().getStringArray(R.array.main_closet));

        final ListView listView = (ListView) rootView.findViewById(android.R.id.list);

        // Assign the adapter to ListView
        listView.setAdapter(mAdapter);

        // Define the listener interface
        AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // When clicked, show dialog box based on option chosen
                if (getResources().getStringArray(R.array.main_closet)[0]
                        .equals((listView.getItemAtPosition(position)))) {

                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.listview_layout, getResources().getStringArray(R.array.Tops_array));
                    listView.setAdapter(mAdapter);

                } else if (getResources().getStringArray(R.array.main_closet)[1]
                        .equals((listView.getItemAtPosition(position)))) {

                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.listview_layout, getResources().getStringArray(R.array.Bottoms_array));
                    listView.setAdapter(mAdapter);

                } else if (getResources().getStringArray(R.array.main_closet)[3]
                        .equals((listView.getItemAtPosition(position)))) {

                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.listview_layout, getResources().getStringArray(R.array.Outerwear_array));
                    listView.setAdapter(mAdapter);

                } else if (getResources().getStringArray(R.array.main_closet)[4]
                        .equals((listView.getItemAtPosition(position)))) {

                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.listview_layout, getResources().getStringArray(R.array.Accessories_array));
                    listView.setAdapter(mAdapter);

                } else if (getResources().getStringArray(R.array.main_closet)[5]
                        .equals((listView.getItemAtPosition(position)))) {

                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.listview_layout, getResources().getStringArray(R.array.Jewelry_array));
                    listView.setAdapter(mAdapter);
                } else  if (getResources().getStringArray(R.array.main_closet)[6]
                        .equals((listView.getItemAtPosition(position)))) {

                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.listview_layout, getResources().getStringArray(R.array.Shoes_array));
                    listView.setAdapter(mAdapter);
                } else {
                    Intent intent = new Intent(getActivity(), DisplayCategoryActivity.class);
                    intent.putExtra("CATEGORY_TYPE", (String)(listView.getItemAtPosition(position)));
                    startActivity(intent);
                }
            }


        };

        // Get the ListView and wired the listener
        listView.setOnItemClickListener(mListener);

        return rootView;
    }
}
