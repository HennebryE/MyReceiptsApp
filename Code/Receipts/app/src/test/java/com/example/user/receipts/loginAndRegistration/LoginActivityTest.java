package com.example.user.receipts.loginAndRegistration;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;

import com.example.user.receipts.BuildConfig;
import com.example.user.receipts.databaseDetails.MyDBHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class LoginActivityTest {

    int dataBaseVersion = 76;
    LoginActivity activity;
    MyDBHandler myDBHandler;
    TextInputEditText usernameET;
    TextInputEditText passwordET;
    String usernameTyped;
    String userpasswordTyped;

    @Before
    public void setUp() {
        myDBHandler = new MyDBHandler(RuntimeEnvironment.application, null, null, dataBaseVersion);
        //myDBHandler.clearDbAndRecreate();
        activity = new LoginActivity();
    }

    @Test
    public void onCreate() {

    }

    @Test
    public void verityFromDB() throws Exception {
        //Arrange
//        String username = "e";
//        usernameET.setText(username);
//        usernameTyped = usernameET.getText().toString();
//        String password = "e";
//        activity.passwordET.setText(password);

        //Act

        //Assert
    }
}