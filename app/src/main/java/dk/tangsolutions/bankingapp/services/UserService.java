package dk.tangsolutions.bankingapp.services;

import com.google.firebase.database.FirebaseDatabase;

import dk.tangsolutions.bankingapp.models.User;


public class UserService {
    private FirebaseDatabase database;

    public UserService() {
        this.database = FirebaseDatabase.getInstance();
    }


    public void createBankAccount() {

    }

}

