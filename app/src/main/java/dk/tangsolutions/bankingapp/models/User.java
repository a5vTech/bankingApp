package dk.tangsolutions.bankingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User implements Parcelable {

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


    protected User(Parcel in) {
        cpr = in.readString();
        affiliate = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        password = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
        bankaccounts = in.createTypedArrayList(BankAccount.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cpr);
        dest.writeInt(affiliate);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(phoneNumber);
        dest.writeString(address);
        dest.writeTypedList(bankaccounts);
    }
}
