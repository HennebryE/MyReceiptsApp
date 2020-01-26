package com.example.user.receipts.databaseDetails;

public class Folders {
    private int _id;
    private String _folder;
    private String _receiptid;

    public Folders() {}

    public Folders(int id, String folder, String receiptid) {
        this._id = id;
        this._folder = folder;
        this._receiptid = receiptid;
    }

    public Folders(String folder, String receiptid) {
        this._folder = folder;
        this._receiptid = receiptid;
    }

    public Folders(String folder) {
        this._folder = folder;
    }

    public void setFolderID(int id) {
        this._id = id;
    }

    public int getFolderID() {
        return this._id;
    }

    public void setFolder(String folder) {
        this._folder = folder;
    }

    public String getFolder() {
        return this._folder;
    }

    public void setReceiptID(String id) {
        this._receiptid = id;
    }

    public String getReceiptID() {
        return this._receiptid;
    }
}
