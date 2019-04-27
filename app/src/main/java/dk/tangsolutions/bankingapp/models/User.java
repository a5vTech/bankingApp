package dk.tangsolutions.bankingapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    //User and profile data
    private String cpr;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;

    // Account specific data
    private ArrayList<BankAccount> bankaccounts = new ArrayList<>();



    public User() {
    }




    // Getters and setters

}
