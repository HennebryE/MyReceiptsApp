package com.example.user.receipts.nfc;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.receipts.BottomNavigationViewHelper;
import com.example.user.receipts.DisplayReceipts;
import com.example.user.receipts.MainActivity;
import com.example.user.receipts.R;
import com.example.user.receipts.categories.CategoriesActivity;
import com.example.user.receipts.databaseDetails.MyDBHandler;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ReturnActivity extends AppCompatActivity {

    Integer dataBaseVersion = 76;
    protected NfcAdapter nfcAdapter;
    protected PendingIntent nfcPendingIntent; // this lets me pass a future Intent to another application and allow that application to execute that Intent using my application's permission.
    protected String[][] techListsArray;
    protected IntentFilter[] intentFiltersArray;
    private static final String TAG = ReturnActivity.class.getName(); // declaring a TAG constant in class
    String[] values;
    int valuesLength;
    TextView returnReceiptid2;
    TextView returnProductName;
    TextView returnProductPrice;
    TextView returnRecidTV;
    TextView returnProductTV;
    TextView returnPriceTV;
    String receiptid2;
    double productPrice;
    double totalPriceSum = 0.0;
    ImageView tapNFCReturn;
    BottomNavigationView bottomNavigationView;
    String passedVar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        Intent intent = getIntent();
        passedVar = intent.getStringExtra("username");

        returnReceiptid2 = findViewById(R.id.returnReceiptid2);
        returnProductName = findViewById(R.id.returnProductName);
        returnProductPrice = findViewById(R.id.returnProductPrice);
        returnRecidTV = findViewById(R.id.returnRecidTV);
        returnProductTV = findViewById(R.id.returnProductTV);
        returnPriceTV = findViewById(R.id.returnPriceTV);
        tapNFCReturn = findViewById(R.id.tapNFCReturn);

        // Initialize NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(nfcAdapter == null) {
            Toast.makeText(this, "Device doesn't support NFC", Toast.LENGTH_LONG).show();
            // End running if device doesn't have NFC
            finish();
            return;
        }
        if(!nfcAdapter.isEnabled()) {
            // if the NFC is turned off on the device, a Toast (a little pop up) appears
            Toast.makeText(this, "NFC is disabled", Toast.LENGTH_LONG).show();
        }

        // The Foregroud Dispatch
        nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
            intentFiltersArray = new IntentFilter[] {ndef, };
        }
        catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        techListsArray = new String[][] { new String[] { NfcF.class.getName() } };

        // Navigation Bar
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.icShop:
                        Intent intentHome = new Intent(ReturnActivity.this, MainActivity.class);
                        intentHome.putExtra("username", passedVar);
                        startActivity(intentHome);
                        break;

                    case R.id.icReturnItem:
                        break;

                    case R.id.icAdd:
                        Intent intentAdd = new Intent(ReturnActivity.this, NFCReader.class);
                        intentAdd.putExtra("username", passedVar);
                        startActivity(intentAdd);
                        break;

                    case R.id.icReceipts:
                        Intent intentReceipts = new Intent(ReturnActivity.this, DisplayReceipts.class);
                        intentReceipts.putExtra("username", passedVar);
                        startActivity(intentReceipts);
                        break;

                    case R.id.icFolder:
                        Intent intentFolder = new Intent(ReturnActivity.this, CategoriesActivity.class);
                        intentFolder.putExtra("username", passedVar);
                        startActivity(intentFolder);
                        break;
                }
                return false;
            }
        });

    }

    // The Foregroud Dispatch
    public void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    public void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, intentFiltersArray, techListsArray);
    }

    public void onNewIntent(Intent intent) {

        // Tap NFC picture disappears and receipt information appears
        tapNFCReturn.setImageDrawable(null);

        vibrate();

        String action = intent.getAction();

        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        // Initialise string to print
        String s = action + "\n\n" + tagFromIntent.toString();

        // Initialise string
        StringBuilder tagText = new StringBuilder();

        // Get array of data to parse
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        // Check if we have data payload, if not it is an empty but formatted tag
        if (data != null) {
            try {
                // Loop through data and get stored records
                for (Parcelable aData : data) {
                    NdefRecord[] NDEFRecords = ((NdefMessage) aData).getRecords();

                    // Loop through record and print data
                    for (NdefRecord NDEFRecord : NDEFRecords) {
                        if (NDEFRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(NDEFRecord.getType(), NdefRecord.RTD_TEXT)) {
                            byte[] tagPayload = NDEFRecord.getPayload();

                            tagText.append(new String(tagPayload, (tagPayload[0] & 0077) + 1, tagPayload.length - (tagPayload[0] & 0077) - 1, ((tagPayload[0] & 0200) == 0) ? "UTF-8" : "UTF-16")).append(";");
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                // catch malformed tag
                throw new IllegalArgumentException(e);
            }

            values = tagText.toString().split(";");
            valuesLength = values.length;

            returnRecidTV.setText(getString((R.string.returnRecid)));
            returnReceiptid2.setText(values[0]);
            returnProductTV.setText(getString((R.string.returnProduct)));
            returnProductName.setText(values[1]);
            returnPriceTV.setText(getString((R.string.returnPrice)));
            String priceWithoutEuro = values[2];
            String priceWithEuro = "€" + values[2];
            returnProductPrice.setText(priceWithEuro);

            MyDBHandler dbHandler = new MyDBHandler(this, null, null, dataBaseVersion);

            receiptid2 = (returnReceiptid2.getText().toString());
            String productName = returnProductName.getText().toString();
            double productPrice = Double.parseDouble(priceWithoutEuro);

            dbHandler.returnData(receiptid2, productName, productPrice);

            Toast.makeText(ReturnActivity.this, "Item Returned", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(ReturnActivity.this, "Scan NFC", Toast.LENGTH_LONG).show();
        }

        MyDBHandler myDBHandler = new MyDBHandler(getApplicationContext(), null, null, dataBaseVersion);
        Cursor cursor = myDBHandler.getAllListContents();

        if(cursor.moveToFirst()) {
            do {
                String receiptid = cursor.getString(0);
                if (receiptid.equals(receiptid2)) {
                    productPrice = cursor.getDouble(cursor.getColumnIndex("productprice"));
                    totalPriceSum = totalPriceSum + productPrice;
                    myDBHandler.updateTotalPrice(totalPriceSum, receiptid);
                }
            } while(cursor.moveToNext());
        }
    }

    public void vibrate() {
        Log.d(TAG, "vibrate");

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(500);
    }
}
