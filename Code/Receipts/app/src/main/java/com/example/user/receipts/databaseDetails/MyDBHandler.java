package com.example.user.receipts.databaseDetails;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import java.math.BigInteger;

public class MyDBHandler extends SQLiteOpenHelper {

    //Setting variables for tables
    private static final int DATABASE_VERSION = 76;
    private static final String DATABASE_NAME = "receiptsDB.db";

    private static final String TABLE_USERS = "users";
    public static final String COLUMN_USERID = "userid";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USEREMAIL = "useremail";
    public static final String COLUMN_USERPASSWORD = "userpassword";
    public static final String COLUMN_USERSTUDENT = "userstudent";

    //"receipts" table and its columns
    private static final String TABLE_RECEIPTS = "receipts";
    public static final String COLUMN_USERSNAME = "usersname";
    public static final String COLUMN_SHOPNAME = "shopname";
    public static final String COLUMN_RECEIPTID = "receiptid";
    public static final String COLUMN_TOTALPRICE = "price";
    public static final String COLUMN_DATE = "receiptDate";
    public static final String COLUMN_TIME = "receiptTime";
    public static final String COLUMN_PRODUCTSQUANTITY = "productsQuantity";
    public static final String COLUMN_EXTRASQUANTITIES = "extrasQuantities";

    // "products" table and its columns
    private static final String TABLE_PRODUCT = "products";
    public static final String COLUMN_ID2 = "_id";
    public static final String COLUMN_RECEIPTID2 = "receiptid2";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_PRODUCTPRICE = "productprice";

    // "folders" table and its column ... to store folders names and the receiptids that go with it
    private static final String TABLE_FOLDER = "folders";
    public static final String COLUMN_FOLDERID = "folderid";
    public static final String COLUMN_RECEIPTID3 = "receiptid3";
    public static final String COLUMN_FOLDERS = "folders";

    // "category" table and its column ... to store folders names for spinner
    private static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATID = "categoryid";
    public static final String COLUMN_USERNAMEFOLDER = "usernamefolder";
    public static final String COLUMN_CATEGORY = "category";

    // "extras" are the extra information on a receipt with expiry dates, such as, Warranties and Special Offers
    private static final String TABLE_EXTRAS = "extras";
    public static final String COLUMN_ID4 = "_id";
    public static final String COLUMN_RECEIPTID4 = "receiptid4";
    public static final String COLUMN_EXTRANAME = "extraname";
    public static final String COLUMN_EXTRAEXPIRYDATE = "expirydate";


    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USERID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_USEREMAIL + " TEXT,"
                + COLUMN_USERPASSWORD + " TEXT,"
                + COLUMN_USERSTUDENT + " TEXT "
                + ");";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_RECEIPTS_TABLE = "CREATE TABLE " + TABLE_RECEIPTS + "("
                + COLUMN_RECEIPTID + " TEXT PRIMARY KEY,"
                + COLUMN_USERSNAME + " TEXT,"
                + COLUMN_SHOPNAME + " TEXT,"
                + COLUMN_TOTALPRICE + " REAL,"
                + COLUMN_DATE + " TEXT," + COLUMN_TIME + " TEXT,"
                + COLUMN_PRODUCTSQUANTITY + " INTEGER,"
                + COLUMN_EXTRASQUANTITIES + " INTEGER "
                + ");";
        db.execSQL(CREATE_RECEIPTS_TABLE);

        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + COLUMN_ID2 + " INTEGER PRIMARY KEY,"
                + COLUMN_RECEIPTID2 + " TEXT,"
                + COLUMN_PRODUCTNAME + " TEXT," + COLUMN_PRODUCTPRICE + " REAL,"
                + " FOREIGN KEY(" + COLUMN_RECEIPTID2 + ") REFERENCES " + TABLE_RECEIPTS + "(" + COLUMN_RECEIPTID + ")"
                + ");";
        db.execSQL(CREATE_PRODUCT_TABLE);

        String CREATE_FOLDER_TABLE = "CREATE TABLE " + TABLE_FOLDER + "("
                + COLUMN_FOLDERID + " INTEGER PRIMARY KEY,"
                + COLUMN_RECEIPTID3 + " TEXT,"
                + COLUMN_FOLDERS + " TEXT,"
                + " FOREIGN KEY(" + COLUMN_RECEIPTID3 + ") REFERENCES " + TABLE_RECEIPTS + "(" + COLUMN_RECEIPTID + ")"
                + ");";
        db.execSQL(CREATE_FOLDER_TABLE);

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + COLUMN_CATID + " INTEGER PRIMARY KEY,"
                + COLUMN_USERNAMEFOLDER + " TEXT,"
                + COLUMN_CATEGORY + " TEXT "
                + ");";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        String CREATE_EXTRA_TABLE = "CREATE TABLE " + TABLE_EXTRAS + "("
                + COLUMN_ID4 + " INTEGER PRIMARY KEY,"
                + COLUMN_RECEIPTID4 + " TEXT,"
                + COLUMN_EXTRANAME + " TEXT," + COLUMN_EXTRAEXPIRYDATE + " TEXT,"
                + " FOREIGN KEY(" + COLUMN_RECEIPTID4 + ") REFERENCES " + TABLE_RECEIPTS + "(" + COLUMN_RECEIPTID + ")"
                + ");";
        db.execSQL(CREATE_EXTRA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEIPTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXTRAS);
        onCreate(db);
    }

    public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_USEREMAIL, user.getEmail());
        values.put(COLUMN_USERPASSWORD, user.getPassword());
        //values.put(COLUMN_USERSTUDENT, user.getStudent());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public boolean checkIfUserExists(String username) {
        String[] columns = {COLUMN_USERID};

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USERNAME + " = ?";

        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_USERS,
                columns, selection, selectionArgs, null, null, null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if(cursorCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUser(String username, String password) {
        String[] columns = {COLUMN_USERID};

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_USERPASSWORD + " = ?";

        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_USERS,
                columns, selection, selectionArgs, null, null, null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if(cursorCount > 0) {
            return true;
        }

        return false;
    }

    //Adds vales from the tag when it is scanned
    public void addReceipt(Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECEIPTID, product.getReceiptID());
        values.put(COLUMN_USERSNAME, product.get_username());
        values.put(COLUMN_SHOPNAME, product.getShopName());
        values.put(COLUMN_TOTALPRICE, product.getPrice());
        values.put(COLUMN_DATE, product.getDateDB());
        values.put(COLUMN_TIME, product.getTimeDB());
        values.put(COLUMN_PRODUCTSQUANTITY, product.getProductsQuantity());
        values.put(COLUMN_EXTRASQUANTITIES, product.getExtraQuantities());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_RECEIPTS, null, values);
        db.close();
    }

    public void addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECEIPTID2, product.getReceiptID2());
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_PRODUCTPRICE, product.getProductPrice());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PRODUCT, null, values);
        db.close();
    }

    public void addReceiptToFolder(Folders folders) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_RECEIPTID3, folders.getReceiptID());
        values.put(COLUMN_FOLDERS, folders.getFolder());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_FOLDER, null, values);
        db.close();
    }

    public void addFolder(Categories categories) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAMEFOLDER, categories.get_usernamefolder());
        values.put(COLUMN_CATEGORY, categories.getFolder());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public void deleteFolder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CATEGORIES, COLUMN_CATID + " = " + id, null);
        db.close();
    }

    public void addExtraInformation(ExtraInformation extraInformation) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECEIPTID4, extraInformation.get_receiptid4());
        values.put(COLUMN_EXTRANAME, extraInformation.get_extraname());
        values.put(COLUMN_EXTRAEXPIRYDATE, extraInformation.get_expirydate());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_EXTRAS, null, values);
        db.close();
    }

    public Cursor getUserContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_USERS,
                null);
        return data;
    }

    //Used to get information from the receipts table
    public Cursor getListContents(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor data = db.rawQuery("SELECT * FROM " + TABLE_RECEIPTS + " WHERE " + COLUMN_USERSNAME + " = ?"
                       // + " ORDER BY DATE(" + COLUMN_DATE + ") ASC", new String[] {username});
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_RECEIPTS + " WHERE " + COLUMN_USERSNAME + " = ?"
                        + " ORDER BY " + COLUMN_SHOPNAME, new String[] {username});
        return data;
    }


    //Used to get the information from the receipts table and products table joined together by foreign kry
    public Cursor getAllListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_RECEIPTS +
                " JOIN " + TABLE_PRODUCT +
                " ON " + COLUMN_RECEIPTID + " = " + COLUMN_RECEIPTID2,
                null);
        return data;
    }

    public Cursor getAllFolderListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_RECEIPTS +
                        " JOIN " + TABLE_FOLDER +
                        " ON " + COLUMN_RECEIPTID + " = " + COLUMN_RECEIPTID3,
                null);
        return data;
    }

    public Cursor getFolderListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_CATEGORIES,
                null);
        return data;
    }

    public Cursor getAllExtraListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_RECEIPTS +
                        " JOIN " + TABLE_EXTRAS +
                        " ON " + COLUMN_RECEIPTID + " = " + COLUMN_RECEIPTID4,
                null);
        return data;
    }

    //Used to return products. Sets the idea to "Return" and value to zero
    public void returnData(String receiptid2, String productName, double productPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int zeroValue = 0;
        values.put(COLUMN_PRODUCTNAME, "Return");
        values.put(COLUMN_PRODUCTPRICE, zeroValue);

        db.update(TABLE_PRODUCT, values, COLUMN_RECEIPTID2 + " = ? AND " +
                        COLUMN_PRODUCTNAME + " = ? AND " + COLUMN_PRODUCTPRICE + " = ?",
                        new String[] {String.valueOf(receiptid2), productName, String.valueOf(productPrice)});
        db.close();
    }

    //Used to get the total of all the products
    public void updateTotalPrice(double sum, String receiptid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TOTALPRICE, sum);
        db.update(TABLE_RECEIPTS, values, COLUMN_RECEIPTID + " = ?", new String[] {String.valueOf(receiptid)});
        db.close();
    }

    //Adds up all the total prices
    public Cursor getSumMonth() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_USERSNAME + ", " + COLUMN_DATE + ", "+ COLUMN_TOTALPRICE + " FROM " + TABLE_RECEIPTS, null);
        return data;
    }

    //Adds up all the total prices
    public Cursor getSum(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_USERSNAME + ", SUM(" + COLUMN_TOTALPRICE + ") FROM " + TABLE_RECEIPTS + " WHERE " + COLUMN_USERSNAME + " = ?", new String[] {username});
        return data;
    }

    public int checkIfEmpty(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String tableName = TABLE_CATEGORIES;
        String selecion = COLUMN_USERNAMEFOLDER + " = ?";
        String[] selectionArgs = {username};

        Cursor data = db.query(tableName, null, selecion, selectionArgs, null,null, null);
        int result = data.getCount();
        data.close();
        return result;
    }

}