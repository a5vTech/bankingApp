package dk.tangsolutions.bankingapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    //User and profile data
    private String firstName;
    private String lastName;
    private String cpr;
    private String email;
    private String phoneNumber;
    private String address;

    // Account specific data
    private String Role;
    private Map<String, BankAccount> bankAccounts = new HashMap<>();


    public User() {
    }


}
