package com.example.eventmate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Random;
import java.util.StringTokenizer;

public class AddEvent extends AppCompatActivity {

    EditText etEventID, etCatID,etEventName, etTicket;
    Switch ESwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        etEventID = findViewById(R.id.editTextEventID);
        etCatID = findViewById(R.id.editTextCatID2);
        etEventName = findViewById(R.id.editTextEventName);
        etTicket = findViewById(R.id.editTextTicket);
        ESwitch = findViewById(R.id.switchE);

        // Optional thing I've implemented: set category ID to last saved ID
        SharedPreferences catSP = getSharedPreferences("Category",MODE_PRIVATE);
        String CatID = catSP.getString("CategoryID", "");
        etCatID.setText(CatID);

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);

        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);

    }

    public void onSaveEventClick(View view){
        // Get value from editText & Switch
        String EventName = etEventName.getText().toString();
        String CatID = etCatID.getText().toString();
        String Ticket = etTicket.getText().toString();
        boolean isActive = ESwitch.isChecked();

        // Find saved category ID (optional)
        SharedPreferences catSP = getSharedPreferences("Category",MODE_PRIVATE);
        String SavedCatID = catSP.getString("CategoryID", "");

        // Check constraints
        if (EventName.isEmpty() || CatID.isEmpty()){
            Toast.makeText(this,"Failure: Event Name or Category ID cannot be empty", Toast.LENGTH_SHORT).show();
        } else if(!Ticket.isEmpty() && !Ticket.equals(Integer.parseInt(Ticket)+"")){
            Toast.makeText(this,"Failure: Tickets Available must be an integer (no zeros at front)", Toast.LENGTH_SHORT).show();
        } else if(!SavedCatID.equals(CatID))  /* Check if input catID = saved catID (optional)*/ {
            Toast.makeText(this,"Failure: Category ID not found in system", Toast.LENGTH_SHORT).show();
        } else{

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

            //Save values into sharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("Event",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("EventID",EventID);
            editor.putString("EventName",EventName);
            editor.putString("CategoryID",CatID);

            if (Ticket.isEmpty()) {
                Ticket = "0"; // Default value if empty
            }

            editor.putString("EventCount",Ticket);
            editor.putBoolean("EventSwitch",isActive);

            editor.apply();

            //Create Toast if success
            Toast.makeText(this,"Event saved successfully: "+ EventID + " to " + CatID, Toast.LENGTH_SHORT).show();
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
                //Handle cases where message does not start with"event:"
                if (!msg.startsWith("event:")){
                    throw new Exception("Error: unable to read Event info!");
                }

                //Remove "event:" from the message
                String content = msg.replace("event:","");

                // Handle cases where the number of tokens is not 4
                int count = 0;
                char semi = ';';

                for (int i = 0; i < content.length(); i++) {
                    if (content.charAt(i) == semi) {
                        count++;
                    }
                }

                if (count != 3){
                    throw new Exception("Error: unable to read Event info!");
                }

                StringTokenizer sT = new StringTokenizer(content, ";");



                String EventName = sT.nextToken();
                String CatID = sT.nextToken();
                String Ticket = sT.nextToken();
                String IsActive = sT.nextToken().toUpperCase();

                //Handle cases where Tickets Available is not an integer
                Integer IntTicket = Integer.parseInt(Ticket);


                // IsActive constraint must be one of : {TRUE,true,FALSE,false}
                if (!IsActive.equals("TRUE") && !IsActive.equals("FALSE")){
                    throw new Exception("Error: unable to read Event info!");
                }


                etEventName.setText(EventName);
                etCatID.setText(CatID);
                etTicket.setText(String.valueOf(Ticket));
                ESwitch.setChecked(IsActive.equals("TRUE"));




            } catch (Exception e){
                Toast.makeText(AddEvent.this, "Error: unable to read Event info!",Toast.LENGTH_SHORT).show();
            }

        }


    }

}