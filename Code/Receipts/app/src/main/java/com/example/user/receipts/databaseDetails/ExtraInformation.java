package com.example.user.receipts.databaseDetails;

public class ExtraInformation {
    private int _id4;
    private String _receiptid4;
    private String _extraname;
    private String _expirydate;

    public ExtraInformation() {}

    public ExtraInformation(int id, String receiptid, String extraname, String expirydate) {
        this._id4 = id;
        this._receiptid4 = receiptid;
        this._extraname = extraname;
        this._expirydate = expirydate;
    }

    public ExtraInformation(String receiptid, String extraname, String expirydate) {
        this._receiptid4 = receiptid;
        this._extraname = extraname;
        this._expirydate = expirydate;
    }

    public ExtraInformation(String extraname, String expirydate) {
        this._extraname = extraname;
        this._expirydate = expirydate;
    }

    public int get_id4() {
        return _id4;
    }

    public void set_id4(int _id4) {
        this._id4 = _id4;
    }

    public String get_receiptid4() {
        return _receiptid4;
    }

    public void set_receiptid4(String _receiptid4) {
        this._receiptid4 = _receiptid4;
    }

    public String get_extraname() {
        return _extraname;
    }

    public void set_extraname(String _extraname) {
        this._extraname = _extraname;
    }

    public String get_expirydate() {
        return _expirydate;
    }

    public void set_expirydate(String _expirydate) {
        this._expirydate = _expirydate;
    }
}