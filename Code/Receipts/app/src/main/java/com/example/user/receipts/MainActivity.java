package com.example.user.receipts;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.receipts.categories.CategoriesActivity;
import com.example.user.receipts.databaseDetails.ExtraInformation;
import com.example.user.receipts.databaseDetails.MyDBHandler;
import com.example.user.receipts.nfc.NFCReader;
import com.example.user.receipts.nfc.ReturnActivity;
import com.example.user.receipts.shops.ShopActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    Integer dataBaseVersion = 76;
    String extraName;
    String extraExpiryDate;
    String receiptid;
    android.support.v7.widget.GridLayout mainGrid;
    CardView cardview1;
    CardView cardview2;
    CardView cardview3;
    CardView cardview4;
    CardView cardview5;
    CardView cardview6;
    TextView username;
    String passedVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        passedVar = intent.getStringExtra("username");
        username = (TextView) findViewById(R.id.usernameTV);
        username.setText(passedVar);

        mainGrid = findViewById(R.id.mainGrid);
        cardview1 = (CardView) findViewById(R.id.cardview1);
        cardview2 = (CardView) findViewById(R.id.cardview2);
        cardview3 = (CardView) findViewById(R.id.cardview3);
        cardview4 = (CardView) findViewById(R.id.cardview4);
        cardview5 = (CardView) findViewById(R.id.cardview5);
        cardview6 = (CardView) findViewById(R.id.cardview6);

        cardview1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nfcIntent = new Intent(MainActivity.this, NFCReader.class);
                nfcIntent.putExtra("username", passedVar);
                startActivity(nfcIntent);
            }
        });

        cardview2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rtnIntent = new Intent(MainActivity.this, ReturnActivity.class);
                startActivity(rtnIntent);
            }
        });

        cardview3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receiptIntent = new Intent(MainActivity.this, DisplayReceipts.class);
                receiptIntent.putExtra("username", passedVar);
                startActivity(receiptIntent);
            }
        });

        cardview4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receiptIntent = new Intent(MainActivity.this, CategoriesActivity.class);
                receiptIntent.putExtra("username", passedVar);
                startActivity(receiptIntent);
            }
        });

        cardview5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shopIntent = new Intent(MainActivity.this, ShopActivity.class);
                shopIntent.putExtra("username", passedVar);
                startActivity(shopIntent);
            }
        });

        cardview6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sumIntent = new Intent(MainActivity.this, SumActivity.class);
                sumIntent.putExtra("username", passedVar);
                startActivity(sumIntent);
            }
        });

        MyDBHandler myDBHandler = new MyDBHandler(getApplicationContext(), null, null, dataBaseVersion);
        Cursor cursor3 = myDBHandler.getAllExtraListContents();

        if(cursor3.moveToFirst()) {
            do {
                Integer receiptidInt = cursor3.getInt(cursor3.getColumnIndex("receiptid"));
                receiptid = String.valueOf(receiptidInt);
                extraName = cursor3.getString(cursor3.getColumnIndex("extraname"));
                Log.i("extraName", extraName);
                extraExpiryDate = cursor3.getString(cursor3.getColumnIndex("expirydate"));
                Log.i("extraExpiryDate", extraExpiryDate);
                ExtraInformation extraInformation = new ExtraInformation(extraName, extraExpiryDate);
                try {
                    extraExpiryDate(extraExpiryDate, receiptid);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while(cursor3.moveToNext());
        } else {
            Toast.makeText(MainActivity.this, "Add Receipts", Toast.LENGTH_LONG).show();
        }
    }

    public void extraExpiryDate(String extraExpiryDate, String value1) throws ParseException {
        String todaysDate = getDate();
        String dateAfter = getTomorrowsDate(todaysDate);
        if(extraExpiryDate.equals(dateAfter)) {
            expiryNotifications(value1);
            Log.i("ID_EXTRAaa", value1);
        }
    }

    public void expiryNotifications(String value1) {
        Intent intent = new Intent(MainActivity.this, DisplayReceipts.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DisplayReceipts.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        String text = "Receipt no.: " + value1 + " has expiry date tomorrow.";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.white_euro_symbol)
                .setContentTitle("Expiry Date")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }

    public String getTomorrowsDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        //String today = getDate();
        Date tdate = dateFormat.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tdate);
        calendar.add(Calendar.DATE, +1);
        Date tomDate = calendar.getTime();
        String tomorrowDate = dateFormat.format(tomDate);
        Log.i("", tomorrowDate);
        return tomorrowDate;
    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}

