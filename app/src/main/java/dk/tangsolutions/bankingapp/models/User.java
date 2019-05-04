package dk.tangsolutions.bankingapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    //User and profile data
    private String cpr;
    private int affiliate;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;

    // Account specific data
    private ArrayList<BankAccount> bankaccounts = new ArrayList<>();



    public User() {
    }

    public User(String cpr, String firstName, String lastName, String email, String phoneNumber, String address, String password) {
        this.cpr = cpr;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
    }

    // Getters and setters


    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<BankAccount> getBankaccounts() {
        return bankaccounts;
    }

    public void setBankaccounts(ArrayList<BankAccount> bankaccounts) {
        this.bankaccounts = bankaccounts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(int affiliate) {
        this.affiliate = affiliate;
    }
}
