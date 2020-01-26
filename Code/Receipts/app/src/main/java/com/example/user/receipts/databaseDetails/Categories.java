package com.example.user.receipts.databaseDetails;

public class Categories {
    private int _id;
    private String _usernamefolder;
    private String _folder;

    public Categories() {}

    public Categories(int id, String usernamefolder, String folder) {
        this._id = id;
        this._usernamefolder = usernamefolder;
        this._folder = folder;
    }

    public Categories(String usernamefolder, String folder) {
        this._usernamefolder = usernamefolder;
        this._folder = folder;
    }

    public Categories(String folder) {
        this._folder = folder;
    }

    public void setFolderID(int id) {
        this._id = id;
    }

    public int getFolderID() {
        return this._id;
    }

    public String get_usernamefolder() {
        return _usernamefolder;
    }

    public void set_usernamefolder(String _usernamefolder) {
        this._usernamefolder = _usernamefolder;
    }

    public void setFolder(String folder) {
        this._folder = folder;
    }

    public String getFolder() {
        return this._folder;
    }

}
