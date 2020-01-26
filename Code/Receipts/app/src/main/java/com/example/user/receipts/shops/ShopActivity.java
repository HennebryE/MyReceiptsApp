package com.example.user.receipts.shops;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.user.receipts.BottomNavigationViewHelper;
import com.example.user.receipts.DisplayReceipts;
import com.example.user.receipts.MainActivity;
import com.example.user.receipts.R;
import com.example.user.receipts.nfc.ReturnActivity;
import com.example.user.receipts.categories.CategoriesActivity;
import com.example.user.receipts.databaseDetails.MyDBHandler;
import com.example.user.receipts.nfc.NFCReader;

public class ShopActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    String passedVar;
    ListView listview;
    ShopAdapter shopAdapter;
    Integer dataBaseVersion = 76;
    String shop;
    int[] general = {
            R.drawable.shop_argos,
            R.drawable.shop_clothesms,
            R.drawable.shop_dunnesstores,
            R.drawable.shop_foodms
    };

    int[] highStreetClothes = {
            R.drawable.shop_newlook,
            R.drawable.shop_monsoon,
            R.drawable.shop_schuh,
            R.drawable.shop_clothesms
    };

    int[] moreMale = {
            R.drawable.shop_diesel,
            R.drawable.shop_topman,
            R.drawable.shop_barbershop,
            R.drawable.shop_jd
    };

    int[] foodShops = {
            R.drawable.shop_dunnesstores,
            R.drawable.shop_foodms,
            R.drawable.lidl
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        String shop = "";
        String topshop = "Topshop";
        String HandM = "H & M";
        String topman = "Topman";
        String barber = "barber";
        String supervalu = "SuperValu";


        Intent intent = getIntent();
        passedVar = intent.getStringExtra("username");
        listview = (ListView) findViewById(R.id.LVShop);

        MyDBHandler myDBHandler = new MyDBHandler(getApplicationContext(), null, null, dataBaseVersion);
        Cursor cursor = myDBHandler.getListContents(passedVar);

        if(cursor.moveToFirst()) {
            do {
                    String username = cursor.getString(cursor.getColumnIndexOrThrow("usersname"));
                    if (username.equals(passedVar)) {
                        shop = cursor.getString(cursor.getColumnIndex("shopname"));
                        Log.i("shoprecommender", shop);
                        break;
                    }
            } while (cursor.moveToNext());
        }

        Log.i("shoprecommenderchecker", shop);
        if(shop.isEmpty()) {
            shopAdapter = new ShopAdapter(ShopActivity.this, general);
        } else if(shop.equals(topshop) || shop.equals(HandM)) {
            shopAdapter = new ShopAdapter(ShopActivity.this, highStreetClothes);
        } else if(shop.equals(topman) || shop.contains(barber)) {
            shopAdapter = new ShopAdapter(ShopActivity.this, moreMale);
        } else if(shop.equals(supervalu)) {
            shopAdapter = new ShopAdapter(ShopActivity.this, foodShops);
        } else {
            shopAdapter = new ShopAdapter(ShopActivity.this, general);
        }

        listview.setAdapter(shopAdapter);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.icShop:
                        Intent intentHome = new Intent(ShopActivity.this, MainActivity.class);
                        intentHome.putExtra("username", passedVar);
                        startActivity(intentHome);
                        break;

                    case R.id.icReturnItem:
                        Intent intentRtnItem = new Intent(ShopActivity.this, ReturnActivity.class);
                        intentRtnItem.putExtra("username", passedVar);
                        startActivity(intentRtnItem);
                        break;

                    case R.id.icAdd:
                        Intent intentAdd = new Intent(ShopActivity.this, NFCReader.class);
                        intentAdd.putExtra("username", passedVar);
                        startActivity(intentAdd);
                        break;

                    case R.id.icReceipts:
                        Intent intentReceipts = new Intent(ShopActivity.this, DisplayReceipts.class);
                        intentReceipts.putExtra("username", passedVar);
                        startActivity(intentReceipts);
                        break;

                    case R.id.icFolder:
                        Intent intentFolder = new Intent(ShopActivity.this, CategoriesActivity.class);
                        intentFolder.putExtra("username", passedVar);
                        startActivity(intentFolder);
                        break;
                }
                return false;
            }
        });

    }
}
