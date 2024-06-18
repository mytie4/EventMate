package com.example.eventmate;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ListEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_bar_layout);

        //Initialise toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarEvent);
        //Set toolbar as actionbar
        setSupportActionBar(myToolbar);
        //Change the name displayed
        getSupportActionBar().setTitle("Events List");

        //Set navigation
        myToolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListEventActivity.this.finish();
            }
        });

        //Have fragment inside the event_container
        getSupportFragmentManager().beginTransaction().replace(
                R.id.event_container, new FragmentListEvent()).commit();

    }

}