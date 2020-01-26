package com.example.user.receipts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.receipts.categories.CategoriesActivity;
import com.example.user.receipts.databaseDetails.MyDBHandler;
import com.example.user.receipts.databaseDetails.Product;
import com.example.user.receipts.nfc.NFCReader;
import com.example.user.receipts.nfc.ReturnActivity;

public class DisplayReceipts extends AppCompatActivity {
    Integer dataBaseVersion = 76;
    MyDBHandler myDBHandler;

    String ID_;
    String value;
    Product receiptList;
    ReceiptDisplayListAdapter listAdapter;
    BottomNavigationView bottomNavigationView;
    ListView listview;
    String passedVar;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.display_receipts);

        Intent intent = getIntent();
        passedVar = intent.getStringExtra("username");

        listview = (ListView) findViewById(R.id.listViewReceipts);
        listAdapter = new ReceiptDisplayListAdapter(getApplicationContext(), R.layout.row_layout);
        listview.setAdapter(listAdapter);
        myDBHandler = new MyDBHandler(getApplicationContext(), null, null, dataBaseVersion);
        cursor = myDBHandler.getListContents(passedVar);

        if(cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("usersname"));
                if(username.equals(passedVar)) {
                    ID_ = cursor.getString(cursor.getColumnIndexOrThrow("receiptid"));
                    String shop;
                    double price;
                    String date;
                    shop = cursor.getString(cursor.getColumnIndex("shopname"));
                    date = cursor.getString(cursor.getColumnIndex("receiptDate"));
                    price = cursor.getDouble(cursor.getColumnIndex("price"));
                    double priceRounded = (Math.round(price * 100.0) / 100.0);
                    receiptList = new Product(ID_, shop, priceRounded, date);
                    listAdapter.add(receiptList);
                }
            } while(cursor.moveToNext());


            // Clicking on receipt
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent myIntent = new Intent(DisplayReceipts.this, DisplayReceiptDetails.class);
                    TextView listText = (TextView) view.findViewById(R.id.textReceipt);
                    String value = listText.getText().toString();
                    myIntent.putExtra("username", passedVar);
                    myIntent.putExtra("ID_EXTRA", value);
                    Log.i("ID_EXTRA", value);
                    myIntent.putExtra("ID_EXTRA2", String.valueOf(id));
                    startActivity(myIntent);
                }
            });
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.icShop:
                        Intent intentHome = new Intent(DisplayReceipts.this, MainActivity.class);
                        intentHome.putExtra("username", passedVar);
                        startActivity(intentHome);
                        break;

                    case R.id.icReturnItem:
                        Intent intentRtnItem = new Intent(DisplayReceipts.this, ReturnActivity.class);
                        intentRtnItem.putExtra("username", passedVar);
                        startActivity(intentRtnItem);
                        break;

                    case R.id.icAdd:
                        Intent intentAdd = new Intent(DisplayReceipts.this, NFCReader.class);
                        intentAdd.putExtra("username", passedVar);
                        startActivity(intentAdd);
                        break;

                    case R.id.icReceipts:
                        break;

                    case R.id.icFolder:
                        Intent intentFolder = new Intent(DisplayReceipts.this, CategoriesActivity.class);
                        intentFolder.putExtra("username", passedVar);
                        startActivity(intentFolder);
                        break;
                }
                return false;
            }
        });

    }
}