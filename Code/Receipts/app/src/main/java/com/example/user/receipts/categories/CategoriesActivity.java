package com.example.user.receipts.categories;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.receipts.BottomNavigationViewHelper;
import com.example.user.receipts.DisplayReceipts;
import com.example.user.receipts.MainActivity;
import com.example.user.receipts.nfc.ReturnActivity;
import com.example.user.receipts.databaseDetails.Categories;
import com.example.user.receipts.databaseDetails.MyDBHandler;
import com.example.user.receipts.R;
import com.example.user.receipts.nfc.NFCReader;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity implements ItemClickListener {

    Integer dataBaseVersion = 75;
    EditText folderName;
    Button folderBtn;
    RecyclerView recyclerView;
    MyDBHandler myDBHandler;
    List<Categories> folderList;
    Categories categories;
    FolderAdapter folderAdapter;
    BottomNavigationView bottomNavigationView;
    String passedVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Intent intent = getIntent();
        passedVar = intent.getStringExtra("username");

        folderName = (EditText) findViewById(R.id.folderET);
        folderBtn = (Button) findViewById(R.id.folderbtn);
        recyclerView = (RecyclerView) findViewById(R.id.folderRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, dataBaseVersion);

        int countFolders = dbHandler.checkIfEmpty(passedVar);
        if(countFolders == 0) {
            String folderString = "Select Folder";
            Categories folder = new Categories(passedVar, folderString);
            dbHandler.addFolder(folder);
        } else if(countFolders == 1) {
            Toast.makeText(CategoriesActivity.this, "No Folders", Toast.LENGTH_SHORT).show();
        } else {

            folderList = new ArrayList<>();

            MyDBHandler myDBHandler = new MyDBHandler(this.getApplicationContext(), null, null, dataBaseVersion);
            Cursor cursor = myDBHandler.getFolderListContents();

            if (cursor.moveToFirst()) {
                do {
                        String folderName = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                        String folderString = "Select Folder";
                        if (!folderString.equals(folderName)) {
                            String username = cursor.getString(cursor.getColumnIndexOrThrow("usernamefolder"));
                            int folderid = cursor.getInt(cursor.getColumnIndexOrThrow("categoryid"));
                            if(username.equals(passedVar)) {
                                categories = new Categories(folderid, username ,folderName);
                                folderList.add(categories);
                                folderAdapter = new FolderAdapter(this, folderList, R.layout.row_layout);
                                recyclerView.setAdapter(folderAdapter);
                            }
                        }
                } while (cursor.moveToNext());
                folderAdapter.setClickListener(this);
            }

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    removeFolder((int) (viewHolder.itemView.getTag()));
                }
            }).attachToRecyclerView(recyclerView);
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.icShop:
                        Intent intentHome = new Intent(CategoriesActivity.this, MainActivity.class);
                        intentHome.putExtra("username", passedVar);
                        startActivity(intentHome);
                        break;

                    case R.id.icReturnItem:
                        Intent intentRtnItem = new Intent(CategoriesActivity.this, ReturnActivity.class);
                        intentRtnItem.putExtra("username", passedVar);
                        startActivity(intentRtnItem);
                        break;

                    case R.id.icAdd:
                        Intent intentAdd = new Intent(CategoriesActivity.this, NFCReader.class);
                        intentAdd.putExtra("username", passedVar);
                        startActivity(intentAdd);
                        break;

                    case R.id.icReceipts:
                        Intent intentReceipts = new Intent(CategoriesActivity.this, DisplayReceipts.class);
                        intentReceipts.putExtra("username", passedVar);
                        startActivity(intentReceipts);
                        break;

                    case R.id.icFolder:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view, int position) {
        //final Categories folder = folderList.get(position);
        Intent i = new Intent(CategoriesActivity.this, DisplayFolderReceipts.class);
        TextView listText = (TextView) view.findViewById(R.id.folderRow);
        String value = listText.getText().toString();
        //Log.i("Folder NAME", folder.getFolder());
        i.putExtra("username", passedVar);
        i.putExtra("FOLDER", value);
        startActivity(i);
    }

    public void addCategories(View view) {
        if(folderName != null) {
            MyDBHandler dbHandler = new MyDBHandler(this, null, null, dataBaseVersion);

            String folderString = folderName.getText().toString();

            Categories folder = new Categories(passedVar, folderString);

            dbHandler.addFolder(folder);
            //folderAdapter.notifyDataSetChanged();
            folderName.setText("");
            Toast.makeText(CategoriesActivity.this, "Added Folder", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(CategoriesActivity.this, "Please Type Folder Name", Toast.LENGTH_LONG).show();
        }
        // Refresh Page
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    public void removeFolder(int id) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, dataBaseVersion);

        dbHandler.deleteFolder(id);
        // Refresh Page
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}