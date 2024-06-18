package com.example.assignment1.assignment2;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.assignment1.FragmentListCategory;
import com.example.assignment1.R;

public class ListCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat_bar_layout);

        //Initialise toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarCat);
        //Set toolbar as actionbar
        setSupportActionBar(myToolbar);
        //Change the name displayed
        getSupportActionBar().setTitle("Categories List");

        //Set navigation
        myToolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListCategoryActivity.this.finish();
            }
        });

        //Have fragment inside the category_container
        getSupportFragmentManager().beginTransaction().replace(
                R.id.category_container, new FragmentListCategory()).commit();
    }
}