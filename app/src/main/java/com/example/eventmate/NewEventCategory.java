package com.example.eventmate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventmate.provider.EMAViewModel;

import java.util.Random;
import java.util.StringTokenizer;

public class NewEventCategory extends AppCompatActivity {

    EditText etCatID, etCatName, etEventCount, etEventLocation;
    Switch NCSwitch;

//    //For recycler view
//    ArrayList<Category> listCat = new ArrayList<>();
//
//    //Gson for Sharedpref
//    Gson gson = new Gson();

    //ViewModel
    private EMAViewModel emaViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialise ViewModel
        emaViewModel = new ViewModelProvider(this).get(EMAViewModel.class);

        setContentView(R.layout.addcat_bar_layout);

        etCatID = findViewById(R.id.editTextCatID);
        etCatName = findViewById(R.id.editTextCatName);
        etEventCount = findViewById(R.id.editTextEventCount);
        NCSwitch = findViewById(R.id.switchNC);

        etEventLocation = findViewById(R.id.editTextEventLocation);





        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);

        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);


        //Initialise toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarAddCat);
        //Set toolbar as actionbar
        setSupportActionBar(myToolbar);
        //Change the name displayed
        getSupportActionBar().setTitle("Add New Category");

        //Set navigation
        myToolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NewEventCategory.this.finish();
            }
        });
    }

    // To check whether Category ID is alphanumeric
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

    // Save list of categories to shared pref
//    private void saveArrayListAsText(){
//        //Convert array list to string
//        String arrayListString = gson.toJson(listCat);
//
//        //Save to shared pref
//        SharedPreferences sharedPreferences = getSharedPreferences("category",MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("category_key",arrayListString);
//        editor.apply();
//    }

    public void onSaveCategoryClick(View view){

//        // Use shared pref to keep track of data
//        String arrayListStringRestored = getSharedPreferences("category", MODE_PRIVATE).getString("category_key", "[]");
//        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
//        listCat = gson.fromJson(arrayListStringRestored, type);


        // Get value from editText & Switch
        String CatName = etCatName.getText().toString();
        String EventCount = etEventCount.getText().toString();
        boolean isActive = NCSwitch.isChecked();

        String EventLocation = etEventLocation.getText().toString();

        // Check constraints
        if (!isAlphanumeric(CatName)){
            Toast.makeText(this,"Failure: Category must be alphanumeric", Toast.LENGTH_SHORT).show();
        } else if(!EventCount.isEmpty() && !EventCount.equals(Integer.parseInt(EventCount)+"")&& Integer.parseInt(EventCount) < 0){
            Toast.makeText(this,"Failure: Event Count must be a positive integer (no zeros at front)", Toast.LENGTH_SHORT).show();
        }
        else{

            //Generate random Category ID
            Random random = new Random();

            String CatID = "C";
            for (int i = 0; i < 2; i++) {
                char character = (char) ('A' + random.nextInt(26));
                CatID += character;
            }
            CatID += "-";

            for (int i = 0; i < 4; i++){
                int digit = random.nextInt(10);
                CatID += digit;
            }

            //Set generated Category ID as text
            etCatID.setText(CatID);


            if (EventCount.isEmpty()) {
                EventCount = "0"; // Default value if empty
            }

            // Create new category object and add to list of categories
            Category category = new Category(CatID,CatName,Integer.parseInt(EventCount),isActive, EventLocation);

            // Insert new record to database
            emaViewModel.insertCategory(category);

            //Create Toast if success
            Toast.makeText(this,"Category saved successfully: "+ CatID, Toast.LENGTH_SHORT).show();

            this.finish();
        }
    }



    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Tokenize received message here
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            myStringTokenizer(msg);
        }

        public void myStringTokenizer(String msg){
            try {
                //Handle cases where message does not start with"category:"
                if (!msg.startsWith("category:")){
                    throw new Exception("Error: unable to read Category info!");
                }

                //Remove "category:" from the message
                String content = msg.replace("category:","");

                // Handle cases where the number of tokens is not 3
                int count = 0;
                char semi = ';';

                for (int i = 0; i < content.length(); i++) {
                    if (content.charAt(i) == semi) {
                        count++;
                    }
                }

                if (count != 2){
                    throw new Exception("Error: unable to read Category info!");
                }


                StringTokenizer sT = new StringTokenizer(content, ";");



                String CatName = sT.nextToken();
                String EventCount = sT.nextToken();
                String IsActive = sT.nextToken().toUpperCase();

                //Handle cases where EvenCount is not an integer
                Integer IntEventCount = Integer.parseInt(EventCount);

                // IsActive constraint must be one of : {TRUE,true,FALSE,false}
                if (!IsActive.equals("TRUE") && !IsActive.equals("FALSE")){
                    throw new Exception("Error: unable to read Category info!");
                }


                etCatName.setText(CatName);
                etEventCount.setText(String.valueOf(EventCount));
                NCSwitch.setChecked(IsActive.equals("TRUE"));




            } catch (Exception e){
                Toast.makeText(NewEventCategory.this, "Error: unable to read Category info!",Toast.LENGTH_SHORT).show();
            }

        }


    }


}