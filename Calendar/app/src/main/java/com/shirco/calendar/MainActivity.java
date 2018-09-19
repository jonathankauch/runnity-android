package com.shirco.calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get View
        listView = (ListView) findViewById(R.id.listView);

        // Set adapter for list
        listView.setAdapter(new EventListAdapter(this));

        searchBar = (EditText) findViewById(R.id.searchBar);

        Button button = (Button) findViewById(R.id.addEventButton);

        // Click Listener for add event button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Add Event Activity
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);

                startActivity(intent);
            }
        });

        Singleton.getInstance().setSearchedEvents(Singleton.getInstance().getEvents());


        // Text Change listener for SearchBar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // Check events  based on search text
                int textlength = cs.length();
                List<Event> tempArrayList = new ArrayList<>();
                for(Event e: Singleton.getInstance().getEvents()){
                    if (textlength <= e.getDescription().length()) {
                        if (e.getDescription().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(e);
                        }
                    }
                }
                Singleton.getInstance().setSearchedEvents(tempArrayList);
                ((EventListAdapter)listView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onResume() {
        Singleton.getInstance().setSearchedEvents(Singleton.getInstance().getEvents());
        ((EventListAdapter)listView.getAdapter()).notifyDataSetChanged();
        searchBar.setText("");
        super.onResume();
    }
}


