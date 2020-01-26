package com.example.user.receipts.categories;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.receipts.BottomNavigationViewHelper;
import com.example.user.receipts.DisplayReceiptDetails;
import com.example.user.receipts.DisplayReceipts;
import com.example.user.receipts.MainActivity;
import com.example.user.receipts.R;
import com.example.user.receipts.ReceiptDisplayListAdapter;
import com.example.user.receipts.nfc.ReturnActivity;
import com.example.user.receipts.databaseDetails.MyDBHandler;
import com.example.user.receipts.databaseDetails.Product;
import com.example.user.receipts.nfc.NFCReader;

import static java.lang.String.valueOf;

public class DisplayFolderReceipts extends  AppCompatActivity {

    Integer dataBaseVersion = 76;
    String ID_;
    String value;
    Product receiptList;
    TextView folderSelected;
    BottomNavigationView bottomNavigationView;
    String passedusername;
    TextView folderTotal;
    String totalNumString;
    double totalNumber;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.display_folder_receipts);

        Intent intent = getIntent();
        String passedVar = intent.getStringExtra("FOLDER");
        folderSelected = (TextView) findViewById(R.id.folderSelected);
        String folderString = "Receipts from: " + passedVar;
        folderSelected.setText(folderString);

        passedusername = intent.getStringExtra("username");

        folderTotal = (TextView) findViewById(R.id.folderTotal);

        final ListView listview = (ListView) findViewById(R.id.listViewReceipts);
        final ReceiptDisplayListAdapter listAdapter = new ReceiptDisplayListAdapter(getApplicationContext(), R.layout.row_layout);
        listview.setAdapter(listAdapter);
        final MyDBHandler myDBHandler = new MyDBHandler(getApplicationContext(), null, null, dataBaseVersion);
        Cursor cursor = myDBHandler.getAllFolderListContents();

        if(cursor.moveToFirst()) {
                do {
                    String username = cursor.getString(cursor.getColumnIndexOrThrow("usersname"));
                    if(username.equals(passedusername)) {
                        String folderName = cursor.getString(cursor.getColumnIndexOrThrow("folders"));
                        if (folderName.equals(passedVar)) {
                            ID_ = cursor.getString(cursor.getColumnIndexOrThrow("receiptid"));
                            //value = Integer.toString(ID_);
                            String shop;
                            double price;
                            String date;
                            shop = cursor.getString(cursor.getColumnIndex("shopname"));
                            date = cursor.getString(cursor.getColumnIndex("receiptDate"));
                            price = cursor.getDouble(cursor.getColumnIndex("price"));
                            receiptList = new Product(ID_, shop, price, date);
                            listAdapter.add(receiptList);
                            totalNumber = totalNumber + price;
                        }
                    }
                } while (cursor.moveToNext());

            totalNumString = "Total: " + (valueOf(totalNumber));
            folderTotal.setText(totalNumString);

            // Clicking on receipt
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent myIntent = new Intent(com.example.user.receipts.categories.DisplayFolderReceipts.this, DisplayReceiptDetails.class);
                    TextView listText = (TextView) view.findViewById(R.id.textReceipt);
                    String value = listText.getText().toString();
                    myIntent.putExtra("username", passedusername);
                    myIntent.putExtra("ID_EXTRA", value);
                    myIntent.putExtra("ID_EXTRA2", valueOf(id));
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
                        Intent intentHome = new Intent(DisplayFolderReceipts.this, MainActivity.class);
                        intentHome.putExtra("username", passedusername);
                        startActivity(intentHome);
                        break;

                    case R.id.icReturnItem:
                        Intent intentRtnItem = new Intent(DisplayFolderReceipts.this, ReturnActivity.class);
                        intentRtnItem.putExtra("username", passedusername);
                        startActivity(intentRtnItem);
                        break;

                    case R.id.icAdd:
                        Intent intentAdd = new Intent(DisplayFolderReceipts.this, NFCReader.class);
                        intentAdd.putExtra("username", passedusername);
                        startActivity(intentAdd);
                        break;

                    case R.id.icReceipts:
                        Intent intentReceipts = new Intent(DisplayFolderReceipts.this, DisplayReceipts.class);
                        intentReceipts.putExtra("username", passedusername);
                        startActivity(intentReceipts);
                        break;

                    case R.id.icFolder:
                        Intent intentFolder = new Intent(DisplayFolderReceipts.this, CategoriesActivity.class);
                        intentFolder.putExtra("username", passedusername);
                        startActivity(intentFolder);
                        break;
                }
                return false;
            }
        });
    }
}
