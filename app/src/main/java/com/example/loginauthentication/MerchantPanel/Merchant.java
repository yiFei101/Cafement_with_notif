package com.example.loginauthentication.MerchantPanel;

public class Merchant {

    private String Fname,Lname,Email,Password;

    public Merchant(String fname, String lname, String email, String password) {
        Fname = fname;
        Lname = lname;
        Email = email;
        Password = password;


    }

    public Merchant() {
    }
    public String getFname() {
        return Fname;
    }
    public String getLname() {
        return Lname;
    }
    public String getEmail() {
        return Email;
    }
    public String getPassword() {
        return Password;
    }
}
