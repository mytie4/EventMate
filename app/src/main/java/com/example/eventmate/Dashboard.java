package com.example.eventmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventmate.provider.EMAViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

public class Dashboard extends AppCompatActivity {
    //Class attributes to use in multiple methods

    //Attributes are toolbar and navigation drawer
    Toolbar myToolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    //Attributes for Event Form
    EditText etEventID, etCatID,etEventName, etTicket;
    Switch ASwitch;

    //Attribute for FAB
    FloatingActionButton fab;


    //ViewModel
    private EMAViewModel emaViewModel;

    //Textview to display gesture
    TextView tvGesture;

    // Help detect basic gestures
    private GestureDetectorCompat mDetector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change layout to drawer_layout
        //This includes coordinator_layout, app_bar_layout, dashboard layout
        setContentView(R.layout.drawer_layout);

        // Initialise editText and switch
        etEventID = findViewById(R.id.editTextEveID);
        etCatID = findViewById(R.id.editTextCategoryID);
        etEventName = findViewById(R.id.editTextEveName);
        etTicket = findViewById(R.id.editTextNumTick);
        ASwitch = findViewById(R.id.switchActive);


        //Initialise toolbar and drawer
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        //Set toolbar as actionbar
        setSupportActionBar(myToolbar);
        //Change the name displayed
        getSupportActionBar().setTitle("Dashboard");

        //Allow toolbar to open and close navigation drawer (hamburger)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Set navigation listener to navigation view
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        //Have fragment inside the dashboard_container
        getSupportFragmentManager().beginTransaction().replace(
                R.id.dashboard_container, new FragmentListCategory()).commit();

        //Initialise FAB
        fab = findViewById(R.id.fab);

        //Initialise ViewModel
        emaViewModel = new ViewModelProvider(this).get(EMAViewModel.class);


        //Set FAB listener (Add new event)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Get input data
                String EventName = etEventName.getText().toString();
                String CatID = etCatID.getText().toString();
                String Ticket = etTicket.getText().toString();
                boolean isActive = ASwitch.isChecked();



                // Check constraints
                if (!isAlphanumeric(EventName)){
                    Toast.makeText(Dashboard.this,"Failure: Event Name must be Alphanumeric (can't be empty or only have empty spaces)", Toast.LENGTH_SHORT).show();
                } else if(!Ticket.isEmpty() && !Ticket.equals(Integer.parseInt(Ticket)+"") && Integer.parseInt(Ticket) < 0){
                    Toast.makeText(Dashboard.this,"Failure: Tickets Available must be an positive integer (no zeros at front)", Toast.LENGTH_SHORT).show();
                } else{

                    if (Ticket.isEmpty()) {
                        Ticket = "0"; // Default value if empty
                    }

                    //Set final value for tickets
                    final String finalTicket = Ticket;

                    //Use observe to check if category ID is valid
                    LiveData<List<Category>> categoryLiveData = emaViewModel.getCategoryById(CatID);
                    categoryLiveData.observe(Dashboard.this, categories -> {
                        categoryLiveData.removeObservers(Dashboard.this);
                        if (categories == null || categories.isEmpty()) {
                            Toast.makeText(Dashboard.this,"Failure: Category does not exist", Toast.LENGTH_SHORT).show();
                        } else {


                            //Generate random Event ID
                            Random random = new Random();

                            String EventID = "E";
                            for (int i = 0; i < 2; i++) {
                                char character = (char) ('A' + random.nextInt(26));
                                EventID += character;
                            }
                            EventID += "-";

                            for (int i = 0; i < 5; i++){
                                int digit = random.nextInt(10);
                                EventID += digit;
                            }

                            //Set generated Event ID as text
                            etEventID.setText(EventID);




                            // Create new instance for event and add to Arraylist
                            Event event = new Event(EventID,CatID,EventName,Integer.parseInt(finalTicket),isActive);

                            // Insert new record to database
                            emaViewModel.insertEvent(event);
                            // Increment event count of category
                            emaViewModel.incrementEventCount(CatID);



                            //Make a Snackbar

                            View.OnClickListener undoListener = createUndoListener(emaViewModel, CatID);
                            Snackbar.make(view, "Added event: " + EventID, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", undoListener).show();




                        }
                    });
                }
            }
        });

        View touchpad = findViewById(R.id.touchpad);
        tvGesture = findViewById(R.id.tv_gesture);

        // initialise new instance of CustomGestureDetector
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        // register GestureDetector and set listener as CustomGestureDetector
        mDetector = new GestureDetectorCompat(this, customGestureDetector);

        //It works without it idk why
        //mDetector.setOnDoubleTapListener(customGestureDetector);

        touchpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mDetector.onTouchEvent(event);

                return true;
            }
        });
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener{




        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            tvGesture.setText("onLongPress");
            // Set edit texts empty
            etEventID.setText("");
            etCatID.setText("");
            etEventName.setText("");
            etTicket.setText("");
            // Set switch to default (false)
            ASwitch.setChecked(false);
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            tvGesture.setText("onDoubleTap");
            fab.performClick();
            return true;
        }

    }



    // Create action for UNDO
    private View.OnClickListener createUndoListener(EMAViewModel viewModel, String catID) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.deleteMostRecentEvent();
                viewModel.decrementEventCount(catID);
            }
        };
    }

    // Function to check whether EventID is alphanumeric or not
    public static boolean isAlphanumeric(String str) {
        //False if empty or ONLY has empty spaces
        if (str == null || str.trim().isEmpty()) {
            return false;
        }

        boolean alphanumeric = false;

        //Check each character if it's either a letter, a digit, or a space
        for (char c : str.toCharArray()) {
            // true if any character is a letter / alphabet
            if (Character.isLetter(c)) {
                alphanumeric = true;
            }
            // Return false if any character isn't a letter, digit, or space
            if (!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c)) {
                return false;
            }
        }

        return alphanumeric;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create options menu in toolbar
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // match the id of selected item and do something
        if (item.getItemId() == R.id.option_ref) {
            // Reload fragment
            Fragment newfragment = new FragmentListCategory();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.dashboard_container,newfragment);
            transaction.commit();

        } else if (item.getItemId() == R.id.option_clear) {
            // Set edit texts empty
            etEventID.setText("");
            etCatID.setText("");
            etEventName.setText("");
            etTicket.setText("");
            // Set switch to default (false)
            ASwitch.setChecked(false);
        } else if (item.getItemId() == R.id.option_del_cat) {

            //Delete all category from database
            emaViewModel.deleteAllCategory();

        } else if (item.getItemId() == R.id.option_del_eve) {

            //Delete all event from database
            emaViewModel.deleteAllEvent();
            //Reset event count for all categories
            emaViewModel.resetAllEventCounts();

        }

        return true;
    }




    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Get the id of the selected item
            int id = item.getItemId();

            // When options on nav view are clicked:
            if (id == R.id.option_view_cat) {
                Intent intent = new Intent(Dashboard.this, ListCategoryActivity.class);
                startActivity(intent);
            } else if (id == R.id.option_add_cat) {
                Intent intent = new Intent(Dashboard.this, NewEventCategory.class);
                startActivity(intent);
            } else if (id == R.id.option_view_eve) {
                Intent intent = new Intent(Dashboard.this, ListEventActivity.class);
                startActivity(intent);
            } else if (id == R.id.option_logout) {
                // finishes Dashboard activity
                // this will automatically send users to log in page, because log in was the previous activity
                // ^ this will always happen due to the fact that other activities such as View Categories and Add Category
                //   will always be "finished" before coming back to the dashboard
                Dashboard.this.finish();
            }
            // Close the drawer
            drawerLayout.closeDrawers();
            // Tell the OS
            return true;
        }
    }
}