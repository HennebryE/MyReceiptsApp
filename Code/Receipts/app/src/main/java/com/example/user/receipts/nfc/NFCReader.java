package com.example.user.receipts.nfc;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Vibrator;
import android.content.Context;
import android.widget.Toast;
import android.content.IntentFilter;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import android.nfc.NdefMessage;
import android.os.Parcelable;
import android.nfc.NdefRecord;

import com.example.user.receipts.BottomNavigationViewHelper;
import com.example.user.receipts.DisplayReceipts;
import com.example.user.receipts.MainActivity;
import com.example.user.receipts.categories.CategoriesActivity;
import com.example.user.receipts.databaseDetails.ExtraInformation;
import com.example.user.receipts.databaseDetails.MyDBHandler;
import com.example.user.receipts.databaseDetails.Product;
import com.example.user.receipts.R;

public class NFCReader extends AppCompatActivity {

    Integer dataBaseVersion = 76;
    protected NfcAdapter nfcAdapter;
    protected PendingIntent nfcPendingIntent; // this lets me pass a future Intent to another application and allow that application to execute that Intent using my application's permission.
    protected String[][] techListsArray;
    protected IntentFilter[] intentFiltersArray;
    protected TextView shopBox;
    protected TextView totalPriceBox;
    protected TextView receiptDateBox;
    protected TextView receiptTimeBox;
    protected TextView receiptNoBox;
    protected Button buttonView;
    String[] values;
    int valuesLength;
    String productName;
    double productprice;
    String extraName;
    String extraExpiryDate;
    ImageView tapNFC;
    Spinner catSpinner;
    BottomNavigationView bottomNavigationView;
    private static final String TAG = NFCReader.class.getName(); // declaring a TAG constant in class
    String passedVar;
    TextView receiptidTV;
    TextView dateAndTimeTV;
    TextView shopTV;
    TextView priceTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader_nfc);

        Intent intent = getIntent();
        passedVar = intent.getStringExtra("username");

        shopBox = (TextView) findViewById(R.id.readtagShop);
        totalPriceBox = (TextView) findViewById(R.id.readtagPrice);
        receiptDateBox = (TextView) findViewById(R.id.receiptsDate);
        receiptTimeBox = (TextView) findViewById(R.id.receiptsTime);
        receiptNoBox = (TextView) findViewById(R.id.readtagReceiptNo);
        tapNFC = (ImageView) findViewById(R.id.tapNFC);
        receiptNoBox.setText("");
        receiptidTV = (TextView) findViewById(R.id.recidTV);
        dateAndTimeTV = (TextView) findViewById(R.id.DateAndTimeTV);
        shopTV = (TextView) findViewById(R.id.shopTV);
        priceTV = (TextView) findViewById(R.id.priceTV);


        buttonView = (Button) findViewById(R.id.buttonView);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NFCReader.this, DisplayReceipts.class);
                intent.putExtra("username", passedVar);
                startActivity(intent);
            }
        });

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
            Toast.makeText(this, "NFC is disabled, go to phone setting to enable", Toast.LENGTH_LONG).show();
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

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.icShop:
                        Intent intentHome = new Intent(NFCReader.this, MainActivity.class);
                        intentHome.putExtra("username", passedVar);
                        startActivity(intentHome);
                        break;

                    case R.id.icReturnItem:
                        Intent intentRtnItem = new Intent(NFCReader.this, ReturnActivity.class);
                        startActivity(intentRtnItem);
                        break;

                    case R.id.icAdd:
                        break;

                    case R.id.icReceipts:
                        Intent intentReceipts = new Intent(NFCReader.this, DisplayReceipts.class);
                        intentReceipts.putExtra("username", passedVar);
                        startActivity(intentReceipts);
                        break;

                    case R.id.icFolder:
                        Intent intentFolder = new Intent(NFCReader.this, CategoriesActivity.class);
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

        tapNFC.setImageDrawable(null);

        vibrate();

        // Initialise StringBuilder
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
                        if (NDEFRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(NDEFRecord.getType(),
                                NdefRecord.RTD_TEXT)) {
                            byte[] tagPayload = NDEFRecord.getPayload();

                            tagText.append(new String(tagPayload, (tagPayload[0] & 0077) + 1,
                                    tagPayload.length - (tagPayload[0] & 0077) - 1,
                                    ((tagPayload[0] & 0200) == 0) ? "UTF-8" : "UTF-16")).append(";");
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                // catch malformed tag
                throw new IllegalArgumentException(e);
            }

            values = tagText.toString().split(";");
            valuesLength = values.length;
            if(valuesLength < 7) {
                Toast.makeText(NFCReader.this, "NFC contains incorrect information", Toast.LENGTH_LONG).show();
                return;
            }
            receiptidTV.setText(getString((R.string.NFCRecid)));
            dateAndTimeTV.setText(getString((R.string.NFCDAT)));
            shopTV.setText(getString((R.string.NFCShop)));
            priceTV.setText(getString((R.string.NFCPrice)));
            shopBox.setText(values[1]);
            totalPriceBox.setText(values[2]);
            String dateString = getDate();
            receiptDateBox.setText(dateString);
            String timeString = getTime();
            receiptTimeBox.setText(timeString);
            receiptNoBox.setText(values[0]);

            MyDBHandler dbHandler = new MyDBHandler(this, null, null, dataBaseVersion);

            String receiptid = (receiptNoBox.getText().toString());
            String receiptid2 = (receiptNoBox.getText().toString());
            String receiptid4 = (receiptNoBox.getText().toString());
            String shopName = shopBox.getText().toString();
            String totalPriceStr = totalPriceBox.getText().toString();

            try {
                double value = Double.parseDouble(totalPriceStr);
            } catch (NumberFormatException nfe) {
                if(!isDoubleValue(totalPriceStr)) {
                    Toast.makeText(NFCReader.this, "NFC contains incorrect information", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            double quantity = Double.parseDouble(totalPriceBox.getText().toString());
            String receiptDate = receiptDateBox.getText().toString();
            String receiptTime = receiptTimeBox.getText().toString();
            int pQuantity = Integer.parseInt(values[3]);
            int eQuntities = Integer.parseInt(values[4]);

            Product receipt = new Product(receiptid, passedVar, shopName, quantity, receiptDate, receiptTime, pQuantity, eQuntities);
            dbHandler.addReceipt(receipt);
            int productFirstPostion = 5;
            int listOfProductsLength = productFirstPostion + pQuantity + 2;
            int listOfExtrasLength = valuesLength;

            int i;

            if(eQuntities != 0) {
                for (i = productFirstPostion; i < listOfProductsLength; i++) {
                    productName = values[i];
                    productprice = Double.parseDouble(values[i + 1]);
                    Product product = new Product(receiptid2, productName, productprice);
                    dbHandler.addProduct(product);
                    i++;
                }

                for (int j = i; j < listOfExtrasLength; j++) {
                    extraName = values[j];
                    extraExpiryDate = values[j + 1];
                    ExtraInformation extraInformation = new ExtraInformation(receiptid4, extraName, extraExpiryDate);
                    dbHandler.addExtraInformation(extraInformation);
                    j++;
                }
            }

            String totalWithEuro = "€" + values[2];
            totalPriceBox.setText(totalWithEuro);

            Toast.makeText(NFCReader.this, "Sent to database", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(NFCReader.this, "Scan NFC", Toast.LENGTH_LONG).show();
        }

    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void vibrate() {
        Log.d(TAG, "vibrate");

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        assert vibe != null;
        vibe.vibrate(500);
    }

    public static boolean isDoubleValue(String str) {
        try {
            double value = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
