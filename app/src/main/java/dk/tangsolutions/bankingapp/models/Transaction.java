package dk.tangsolutions.bankingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Transaction implements Parcelable {
    private String title;
    private String to;
    private String from;
    private Double amount;
    private Boolean deposite;
    private Date date;

    public Transaction() {
    }

    public Transaction(String to, String from, Double amount, Boolean deposite, Date date) {
        this.to = to;
        this.from = from;
        this.amount = amount;
        this.deposite = deposite;
        this.date = date;
    }

    protected Transaction(Parcel in) {
        to = in.readString();
        from = in.readString();
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readDouble();
        }
        byte tmpDeposite = in.readByte();
        deposite = tmpDeposite == 0 ? null : tmpDeposite == 1;
    }


    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getDeposite() {
        return deposite;
    }

    public void setDeposite(Boolean deposite) {
        this.deposite = deposite;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(to);
        dest.writeString(from);
        if (amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(amount);
        }
        dest.writeByte((byte) (deposite == null ? 0 : deposite ? 1 : 2));
    }
}
