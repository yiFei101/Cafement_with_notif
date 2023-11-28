package com.example.loginauthentication;
public class Student {

    private String EmailID, FirstName, LastName, Password;

    public Student() {
    }

    public Student(String emailID, String firstName, String lastName, String password) {
        EmailID = emailID;
        FirstName = firstName;
        LastName = lastName;
        Password = password;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

