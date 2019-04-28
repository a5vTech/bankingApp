package dk.tangsolutions.bankingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class BankAccount implements Parcelable {
    private String type;
    private String name;
    private double balance;
    private String accountNumber;
//    private String regNumber;

//    private ArrayList<Transaction> transactions = new ArrayList<>();


    public BankAccount() {
    }

    public BankAccount(String type, String name, double balance, String accountNumber) {
        this.type = type;
        this.name = name;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    protected BankAccount(Parcel in) {
        type = in.readString();
        name = in.readString();
        balance = in.readDouble();
        accountNumber = in.readString();
    }

    public static final Creator<BankAccount> CREATOR = new Creator<BankAccount>() {
        @Override
        public BankAccount createFromParcel(Parcel in) {
            return new BankAccount(in);
        }

        @Override
        public BankAccount[] newArray(int size) {
            return new BankAccount[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(name);
        dest.writeDouble(balance);
        dest.writeString(accountNumber);
    }
}
