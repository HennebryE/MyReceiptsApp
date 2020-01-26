package com.example.user.receipts.databaseDetails;

public class Product {
    private String _receiptid;
    private String _username;
    private String _shopname;
    private double _price;
    private String _date;
    private String _time;
    private int _productsQuantity;
    private int _extraQuantities;

    private int _id2;
    private String _receiptid2;
    private String _productname;
    private double _productprice;

    public Product() {}

    public Product(String receiptid, String username, String shopname, double price, String date, String time, int pQuantity, int eQuantities) {
        this._receiptid = receiptid;
        this._username = username;
        this._shopname = shopname;
        this._price = price;
        this._date = date;
        this._time = time;
        this._productsQuantity = pQuantity;
        this._extraQuantities = eQuantities;
    }

    public Product(String receiptid, String shopname, double price, String date, String time, int pQuantity, int eQuantities) {
        this._shopname = shopname;
        this._receiptid = receiptid;
        this._price = price;
        this._date = date;
        this._time = time;
        this._productsQuantity = pQuantity;
        this._extraQuantities = eQuantities;
    }

    public Product(String receiptid, String shopname, double price, String date, String time) {
        this._shopname = shopname;
        this._receiptid = receiptid;
        this._price = price;
        this._date = date;
        this._time = time;
    }

    public Product(String receiptid, String shopname, double price, String date) {
        this._shopname = shopname;
        this._receiptid = receiptid;
        this._price = price;
        this._date = date;
    }

    public Product(String receiptid, double price, String date) {
        this._receiptid = receiptid;
        this._price = price;
        this._date = date;
    }

    public Product(int id, String receiptid, String productname, double price) {
        this._id2 = id;
        this._receiptid2 = receiptid;
        this._productname = productname;
        this._productprice = price;
    }

    public Product(String receiptid, String productname, double price) {
        this._receiptid2 = receiptid;
        this._productname = productname;
        this._productprice = price;
    }

    public Product(String productname, double price) {
        this._productname = productname;
        this._productprice = price;
    }


    public void setReceiptID(String id) {
        this._receiptid = id;
    }

    public String getReceiptID() {
        return this._receiptid;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public void setShopName(String shopname) {
        this._shopname = shopname;
    }

    public String getShopName() {
        return this._shopname;
    }

    public void setPrice(double price) { this._price = price; }

    public double getPrice() {
        return this._price;
    }

    public String getDateDB() {
        return this._date;
    }

    public void setDateDB(String date) {
        this._date = date;
    }

    public String getTimeDB() {
        return this._time;
    }

    public void setTimeDB(String time) {
        this._time = time;
    }

    public void setProductsQuantity(int productsQuantity) {
        this._productsQuantity = productsQuantity;
    }

    public int getProductsQuantity() {
        return this._productsQuantity;
    }

    public void setExtraQuantities(int extraQuantities) {
        this._extraQuantities = extraQuantities;
    }

    public int getExtraQuantities() {
        return this._extraQuantities;
    }



    public void setID2(int id) { this._id2 = id; }

    public int getID2() { return this._id2; }

    public void setReceiptID2(String id) {
        this._receiptid2 = id;
    }

    public String getReceiptID2() {
        return this._receiptid2;
    }

    public void setProductName(String productname) {
        this._productname = productname;
    }

    public String getProductName() { return this._productname; }

    public void setProductPrice(double price) { this._productprice = price; }

    public double getProductPrice() { return this._productprice; }

}
