package com.example.assignment1.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment1.Dashboard;
import com.example.assignment1.R;

public class Login extends AppCompatActivity {

    EditText etName, etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etName = findViewById(R.id.editTextLogName);
        etPass = findViewById(R.id.editTextLogPass);



        SharedPreferences sharedPreferences = getSharedPreferences("AccountRegistration", MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "");


        etName.setText(username);
    }

    public void OnLoginClick(View view) {
        String inputUsername = etName.getText().toString();
        String inputPassword = etPass.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("AccountRegistration", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("Username", "default_username");
        String savedPassword = sharedPreferences.getString("Password", "default_password");

        if (!inputUsername.equals(savedUsername) || !inputPassword.equals(savedPassword)) {
            Toast.makeText(this,"Authentication failure: Username or Password incorrect",Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this,"Login Success",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
        }
    }

    public void OnRegisterClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}