package com.example.user.receipts.databaseDetails;

public class User {

    private int id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String student;

    public User() {

    }

    public User(int _id, String _name, String _username, String _email, String _password, String _student) {
        this.id = _id;
        this.name = _name;
        this.username = _username;
        this.email = _email;
        this.password = _password;
        this.student = _student;
    }

    public User(String _name, String _username, String _email, String _password) {
        this.name = _name;
        this.username = _username;
        this.email = _email;
        this.password = _password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }
}
