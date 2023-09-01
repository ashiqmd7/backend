package com.G2T5203.wingit.entities;

import java.util.Date;

public class User {
    private String USER_ID;
    private String password;
    private String first_name;
    private String last_name;
    private Date dob;
    private String email;
    private int phone;
    private String salutation;

    public User(String USER_ID, String password, String first_name, String last_name, Date dob, String email, int phone, String salutation) {
        this.USER_ID = USER_ID;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.salutation = salutation;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    @Override
    public String toString() {
        return "User{" +
                "USER_ID='" + USER_ID + '\'' +
                ", password='" + password + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", dob=" + dob +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", salutation='" + salutation + '\'' +
                '}';
    }
}
