package com.example.user.receipts.loginAndRegistration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.receipts.DisplayReceiptDetails;
import com.example.user.receipts.R;
import com.example.user.receipts.databaseDetails.MyDBHandler;
import com.example.user.receipts.databaseDetails.User;

public class RegisterActivity extends AppCompatActivity {

    int dataBaseVersion = 76;
    EditText nameET;
    EditText usernameET;
    EditText useremailET;
    TextInputEditText userpasswordET;
    TextInputEditText userpasswordConfirmET;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    TextView toLoginTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameET = (EditText)findViewById(R.id.textInputEditTextName);
        usernameET = (EditText)findViewById(R.id.textInputEditTextUserName);
        useremailET = (EditText)findViewById(R.id.textInputEditTextEmail);
        userpasswordET = findViewById(R.id.textInputEditTextPassword);
        userpasswordConfirmET = findViewById(R.id.textInputEditTextConfirmPassword);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutUsername = (TextInputLayout) findViewById(R.id.textInputLayoutUserName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);


        toLoginTV = (TextView) findViewById(R.id.appCompatTextViewLoginLink);

        toLoginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });

    }

    // Register Validation and adding user to database if successful
    public void newUser (View view) {
        MyDBHandler myDBHandler = new MyDBHandler(this, null, null, dataBaseVersion);
        String name = nameET.getText().toString().trim();
        String username = usernameET.getText().toString().trim();
        Log.i("username", username);
        String useremail = useremailET.getText().toString();
        String userpassword = userpasswordET.getText().toString();
        String userpasswordConfirm = userpasswordConfirmET.getText().toString();
        if(name.isEmpty()) {
            textInputLayoutUsername.setErrorEnabled(false);
            textInputLayoutEmail.setErrorEnabled(false);
            textInputLayoutPassword.setErrorEnabled(false);
            textInputLayoutConfirmPassword.setErrorEnabled(false);
            textInputLayoutName.setError(getString(R.string.error_message_name));
        } else if(username.isEmpty()) {
            textInputLayoutName.setErrorEnabled(false);
            textInputLayoutEmail.setErrorEnabled(false);
            textInputLayoutPassword.setErrorEnabled(false);
            textInputLayoutConfirmPassword.setErrorEnabled(false);
            textInputLayoutUsername.setError(getString(R.string.error_message_username));
        } else if(myDBHandler.checkIfUserExists(username)) {
            textInputLayoutName.setErrorEnabled(false);
            textInputLayoutEmail.setErrorEnabled(false);
            textInputLayoutPassword.setErrorEnabled(false);
            textInputLayoutConfirmPassword.setErrorEnabled(false);
            textInputLayoutUsername.setError(getString(R.string.error_username_exists));
        } else if(useremail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(useremail).matches()) {
            textInputLayoutName.setErrorEnabled(false);
            textInputLayoutEmail.setErrorEnabled(false);
            textInputLayoutPassword.setErrorEnabled(false);
            textInputLayoutConfirmPassword.setErrorEnabled(false);
            textInputLayoutEmail.setError(getString(R.string.error_message_email));
        } else if(userpassword.isEmpty()) {
            textInputLayoutName.setErrorEnabled(false);
            textInputLayoutUsername.setErrorEnabled(false);
            textInputLayoutEmail.setErrorEnabled(false);
            textInputLayoutConfirmPassword.setErrorEnabled(false);
            textInputLayoutPassword.setError(getString(R.string.error_message_password));
        } else if(userpasswordConfirm.isEmpty()) {
            textInputLayoutName.setErrorEnabled(false);
            textInputLayoutUsername.setErrorEnabled(false);
            textInputLayoutEmail.setErrorEnabled(false);
            textInputLayoutPassword.setErrorEnabled(false);
            textInputLayoutConfirmPassword.setError(getString(R.string.error_message_password));
        } else if(!userpassword.equals(userpasswordConfirm)) {
            textInputLayoutName.setErrorEnabled(false);
            textInputLayoutUsername.setErrorEnabled(false);
            textInputLayoutEmail.setErrorEnabled(false);
            textInputLayoutPassword.setErrorEnabled(false);
            textInputLayoutConfirmPassword.setError(getString(R.string.error_password_match));
        } else {
            textInputLayoutName.setErrorEnabled(false);
            textInputLayoutUsername.setErrorEnabled(false);
            textInputLayoutEmail.setErrorEnabled(false);
            textInputLayoutPassword.setErrorEnabled(false);
            textInputLayoutConfirmPassword.setErrorEnabled(false);
            User user = new User(name, username, useremail, userpassword);
            myDBHandler.addUser(user);
            Toast.makeText(RegisterActivity.this, getString(R.string.success_message), Toast.LENGTH_LONG).show();
            emptyEditTexts();
            Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(myIntent);
        }
    }

    private void emptyEditTexts() {
        nameET.setText("");
        usernameET.setText("");
        useremailET.setText("");
        userpasswordET.setText("");
        userpasswordConfirmET.setText("");
    }


}
