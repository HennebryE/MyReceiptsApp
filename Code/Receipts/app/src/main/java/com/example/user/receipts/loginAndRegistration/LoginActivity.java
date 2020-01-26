package com.example.user.receipts.loginAndRegistration;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.receipts.DisplayReceiptDetails;
import com.example.user.receipts.DisplayReceipts;
import com.example.user.receipts.MainActivity;
import com.example.user.receipts.R;
import com.example.user.receipts.databaseDetails.MyDBHandler;
import com.example.user.receipts.databaseDetails.User;

public class LoginActivity extends AppCompatActivity  {

    int dataBaseVersion = 76;
    TextInputEditText usernameET;
    TextInputEditText passwordET;
    AppCompatButton appCompatButtonLogin;
    String usernameTyped;
    String userpasswordTyped;
    AppCompatTextView toRegisterTV;
    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.textInputEditTextUserName);
        passwordET = findViewById(R.id.textInputEditTextPassword);
        toRegisterTV = findViewById(R.id.textViewLinkRegister);
        appCompatButtonLogin = (AppCompatButton)findViewById(R.id.appCompatButtonLogin);
        textInputLayoutUsername = findViewById(R.id.textInputLayoutUserName);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        toRegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        });

        appCompatButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verityFromDB();
            }
        });
    }

    // Validation of login in and checking if the username and password are correct
    public void verityFromDB() {
        usernameTyped = usernameET.getText().toString();
        Log.i("usernameTyped", usernameTyped);
        userpasswordTyped = passwordET.getText().toString();

        MyDBHandler myDBHandler = new MyDBHandler(this, null, null, dataBaseVersion);
        if(usernameTyped.isEmpty()) {
            textInputLayoutPassword.setErrorEnabled(false);
            textInputLayoutUsername.setError(getString(R.string.error_message_username));
        } else if(userpasswordTyped.isEmpty()) {
            textInputLayoutUsername.setErrorEnabled(false);
            textInputLayoutPassword.setError(getString((R.string.error_message_password)));
        } else if(myDBHandler.checkUser(usernameTyped, userpasswordTyped)) {
            textInputLayoutUsername.setErrorEnabled(false);
            textInputLayoutPassword.setErrorEnabled(false);
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            myIntent.putExtra("username", usernameTyped);
            startActivity(myIntent);
            usernameET.setText("");
            passwordET.setText("");
        } else {
            Toast.makeText(LoginActivity.this, "Entered incorrect username or password", Toast.LENGTH_LONG).show();
        }
    }
}
