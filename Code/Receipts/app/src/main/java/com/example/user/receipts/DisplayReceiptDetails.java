package com.example.user.receipts;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.receipts.databaseDetails.Categories;
import com.example.user.receipts.databaseDetails.ExtraInformation;
import com.example.user.receipts.databaseDetails.Folders;
import com.example.user.receipts.databaseDetails.MyDBHandler;
import com.example.user.receipts.databaseDetails.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DisplayReceiptDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Integer dataBaseVersion = 74;
    MyDBHandler myDBHandler;
    Cursor cursor;
    Bundle extras;
    String passedVar;
    TextView textNumber;
    TextView receiptNum;
    TextView shopDisplay;
    TextView dateDisplay;
    TextView timeDisplay;
    TextView totalPrice;
    TextView quantityDisplay;
    Spinner catSpinner;
    Categories categories;
    double totalPriceSum;
    String extraName;
    String extraExpiryDate;
    String passedVar2;
    String passusername;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.display_receipt_detail);

        Intent intent = getIntent();
        passedVar = intent.getStringExtra("ID_EXTRA");
        passedVar2 = intent.getStringExtra("ID_EXTRA2");
        passusername = intent.getStringExtra("username");
        //Integer passedToInt = Integer.parseInt(passedVar);
        Integer passedToInt2 = Integer.parseInt(passedVar2);
        passedToInt2 = passedToInt2 + 1;
        receiptNum = findViewById(R.id.receiptNum);
        receiptNum.setText(passedVar);
        textNumber = findViewById(R.id.textNumber);
        textNumber.setText(String.valueOf(passedToInt2));
        Product receiptList;

        shopDisplay = findViewById(R.id.shopDisplay);
        totalPrice = findViewById(R.id.totalPrice);
        dateDisplay = findViewById(R.id.dateDisplay);
        timeDisplay = findViewById(R.id.timeDisplay);
        quantityDisplay = findViewById(R.id.quantityDisplay);
        catSpinner = (Spinner) findViewById(R.id.spinnerCategories);

        final ListView listview = (ListView) findViewById(R.id.listViewReceipts);
        final MyListAdapter listAdapter = new MyListAdapter(getApplicationContext(), R.layout.row_layout2);
        listview.setAdapter(listAdapter);

        myDBHandler = new MyDBHandler(getApplicationContext(), null, null, dataBaseVersion);
        cursor = myDBHandler.getAllListContents();

        if(cursor.moveToFirst()) {
            do {
                String receiptid = cursor.getString(cursor.getColumnIndex("receiptid"));
                if (receiptid.equals(passedVar)) {
                    String shop= null;
                    double price = 0;
                    String date = null;
                    String time = null;
                    Integer quantity;
                    shop = cursor.getString(cursor.getColumnIndex("shopname"));
                    price = cursor.getDouble(cursor.getColumnIndex("price"));
                    date = cursor.getString(cursor.getColumnIndex("receiptDate"));
                    time = cursor.getString(cursor.getColumnIndex("receiptTime"));
                    quantity = cursor.getInt(cursor.getColumnIndex("productsQuantity"));
                    shopDisplay.setText(shop);
                    totalPrice.setText(String.valueOf(price));
                    dateDisplay.setText(date);
                    timeDisplay.setText(time);
                    quantityDisplay.setText(String.valueOf(quantity));

                    String productName;
                    double productPrice;
                    productName = cursor.getString(cursor.getColumnIndex("productname"));
                    productPrice = cursor.getDouble(cursor.getColumnIndex("productprice"));
                    totalPriceSum = totalPriceSum + productPrice;
                    receiptList = new Product(productName, productPrice);
                    listAdapter.add(receiptList);
                    myDBHandler.updateTotalPrice(totalPriceSum, receiptid);
               }
            } while(cursor.moveToNext());
        } else {
            Toast.makeText(DisplayReceiptDetails.this, "No receipts", Toast.LENGTH_LONG).show();
        }

        final ListView listview2 = (ListView) findViewById(R.id.listViewExtra);
        final MyListAdapter2 listAdapter2 = new MyListAdapter2(getApplicationContext(), R.layout.row_layout2);
        listview2.setAdapter(listAdapter2);

        Cursor cursor3 = myDBHandler.getAllExtraListContents();

        if(cursor3.moveToFirst()) {
            do {
                String receiptid = cursor3.getString(cursor3.getColumnIndex("receiptid"));
                if(receiptid.equals(passedVar)) {
                    extraName = cursor3.getString(cursor3.getColumnIndex("extraname"));
                    Log.i("extraName", extraName);
                    extraExpiryDate = cursor3.getString(cursor3.getColumnIndex("expirydate"));
                    Log.i("extraExpiryDate", extraExpiryDate);
                    ExtraInformation extraInformation = new ExtraInformation(extraName, extraExpiryDate);
                    listAdapter2.add(extraInformation);
                }
            } while(cursor3.moveToNext());
        } else {
            Toast.makeText(DisplayReceiptDetails.this, "No Extras", Toast.LENGTH_LONG).show();
        }

        MyDBHandler dbHandler = new MyDBHandler(DisplayReceiptDetails.this, null, null, dataBaseVersion);
        //Cursor cursor2 = dbHandler.checkIfEmpty(passusername);

        //cursor2.moveToFirst();
        //int countFolders = cursor2.getInt(0);
        int countFolders = dbHandler.checkIfEmpty(passusername);
        if(countFolders == 0) {
            String folderString = "Select Folder";
            Categories folder = new Categories(passusername, folderString);
            dbHandler.addFolder(folder);
        } else {
            catSpinner.setOnItemSelectedListener(this);
            loadSpinnerData();
        }
    }

    public void loadSpinnerData() {
        List<String> folderList = new ArrayList<>();
        MyDBHandler myDBHandler = new MyDBHandler(getApplicationContext(), null, null, dataBaseVersion);
        Cursor cursor = myDBHandler.getFolderListContents();

        if(cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("usernamefolder"));
                if(username.equals(passusername)) {
                    String folderName = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                    folderList.add(folderName);
                }
            } while (cursor.moveToNext());
        }

        ArrayAdapter<String> mySpinAd = new ArrayAdapter<String>(DisplayReceiptDetails.this,
                R.layout.spinner_item, folderList) {
            @Override
            public boolean isEnabled(int position) {
                if(position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View view1, ViewGroup parent) {
                View view2 = super.getDropDownView(position, view1, parent);
                TextView tv = (TextView) view2;
                if(position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view2;
            }
        };
        mySpinAd.setDropDownViewResource(R.layout.spinner_item);
        catSpinner.setAdapter(mySpinAd);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String folderSelect = parent.getItemAtPosition(position).toString();

        String folderString = "Select Folder";
        if(!folderSelect.equals(folderString)) {
            MyDBHandler myDBHandler = new MyDBHandler(getApplicationContext(), null, null, dataBaseVersion);

            Intent intent = getIntent();
            String receiptidString = intent.getStringExtra("ID_EXTRA");
            //Integer receiptid = Integer.parseInt(receiptidString);

            Folders folders = new Folders(folderSelect, receiptidString);
            myDBHandler.addReceiptToFolder(folders);
            String folderToDBStr = "Receipt added to " + folderSelect;
            Toast.makeText(DisplayReceiptDetails.this, folderToDBStr, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
