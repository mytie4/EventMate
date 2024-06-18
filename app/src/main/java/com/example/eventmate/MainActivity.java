package com.example.eventmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etName, etPass, etPassC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.editTextName);
        etPass = findViewById(R.id.editTextPass);
        etPassC = findViewById(R.id.editTextPassCon);
    }

    public void onRegisterClick(View view){
        String inputName = etName.getText().toString();
        String inputPass = etPass.getText().toString();
        String inputPassC = etPassC.getText().toString();

        if (inputName.isEmpty()||inputPass.isEmpty()){
            Toast.makeText(this,"Failure: Username or Password cannot be blank",Toast.LENGTH_SHORT).show();
        } else if(!inputPass.equals(inputPassC)){
            Toast.makeText(this,"Failure: Password does not match Password Confirmation",Toast.LENGTH_SHORT).show();
        } else{
            SharedPreferences sharedPreferences = getSharedPreferences("AccountRegistration",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("Username",inputName);
            editor.putString("Password",inputPass);
            editor.putString("PasswordC",inputPassC);

            editor.apply();

            Toast.makeText(this,"Success: Registration completed",Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(this, Login.class);

            startActivity(intent);
        }
    }

    public void onLoginClick(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}