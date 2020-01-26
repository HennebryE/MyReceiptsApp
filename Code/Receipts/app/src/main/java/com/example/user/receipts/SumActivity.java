package com.example.user.receipts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.receipts.categories.CategoriesActivity;
import com.example.user.receipts.databaseDetails.MyDBHandler;
import com.example.user.receipts.nfc.NFCReader;
import com.example.user.receipts.nfc.ReturnActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SumActivity extends AppCompatActivity {
    Integer dataBaseVersion = 76;
    TextView sumMonth;
    TextView sum;
    String sumnum;
    String sumnumMonth;
    BottomNavigationView bottomNavigationView;
    Button limitBtn;
    String passedVar;
    Double addingSum = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum);

        Intent intent = getIntent();
        passedVar = intent.getStringExtra("username");

        sumMonth = (TextView) findViewById(R.id.sumTVMonth);
        sum = (TextView) findViewById(R.id.sumTV);
        //limitBtn = (Button) findViewById(R.id.limitBtn);

//        //limitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LimitNotifications();
//            }
//        });
        int todaysMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.i("TodaysMonth", String.valueOf(todaysMonth));

        MyDBHandler myDBHandler = new MyDBHandler(getApplicationContext(), null, null, dataBaseVersion);

        Cursor cursor2 = myDBHandler.getSumMonth();
        if(cursor2.moveToFirst()) {
            do {
                String usernameDB = cursor2.getString(0);
                if(passedVar.equals(usernameDB)) {
                    String date = cursor2.getString(1);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date tdate;
                    try {
                        tdate = dateFormat.parse(date);
                        Log.i("tdate", String.valueOf(tdate));
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(tdate);
                        int month = cal.get(Calendar.MONTH) + 1;
                        Log.i("DBMonth", String.valueOf(month));
                        if (todaysMonth == month) {
                            Double sumdb = cursor2.getDouble(2);
                            addingSum = addingSum + sumdb;
                            sumMonth.setText(String.valueOf((Math.round(addingSum * 100.0) / 100.0)));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } while(cursor2.moveToNext());
        }
        sumnumMonth = sumMonth.getText().toString();
        sumnumMonth = "€" + sumnumMonth;
        sumMonth.setText(sumnumMonth);

        Cursor cursor = myDBHandler.getSum(passedVar);

        if(cursor.moveToFirst()) {
            do {
                Double sumdb = cursor.getDouble(1);
                sum.setText(String.valueOf((Math.round(sumdb * 100.0) / 100.0)));
            } while(cursor.moveToNext());
        }
        sumnum = sum.getText().toString();
        sumnum = "€" + sumnum;
        sum.setText(sumnum);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.icShop:
                        Intent intentHome = new Intent(SumActivity.this, MainActivity.class);
                        intentHome.putExtra("username", passedVar);
                        startActivity(intentHome);
                        break;

                    case R.id.icReturnItem:
                        Intent intentRtnItem = new Intent(SumActivity.this, ReturnActivity.class);
                        intentRtnItem.putExtra("username", passedVar);
                        startActivity(intentRtnItem);
                    break;

                    case R.id.icAdd:
                        Intent intentAdd = new Intent(SumActivity.this, NFCReader.class);
                        intentAdd.putExtra("username", passedVar);
                        startActivity(intentAdd);
                        break;

                    case R.id.icReceipts:
                        Intent intentReceipts = new Intent(SumActivity.this, DisplayReceipts.class);
                        intentReceipts.putExtra("username", passedVar);
                        startActivity(intentReceipts);
                        break;

                    case R.id.icFolder:
                        Intent intentFolder = new Intent(SumActivity.this, CategoriesActivity.class);
                        intentFolder.putExtra("username", passedVar);
                        startActivity(intentFolder);
                        break;
                }
                return false;
            }
        });
    }

//    public void LimitNotifications() {
//
//        Intent intent = new Intent(this, SumActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setSmallIcon(R.drawable.white_euro_symbol)
//                .setContentTitle("Limit Reached")
//                .setContentIntent(pendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, notificationBuilder.build());
//    }
}
