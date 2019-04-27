package dk.tangsolutions.bankingapp.services;

import android.content.Context;
import android.content.Intent;

import dk.tangsolutions.bankingapp.activities.LoginActivity;
import dk.tangsolutions.bankingapp.models.User;

public class AuthService {

    private static User currentUser;

    public AuthService() {

    }


    public boolean login(String cpr, String password) {
        //TODO: Call api with cpr and password

        //If login is not successful return error msg

        return false;
    }

    public void logout(Context context) {
        currentUser = null;
        Intent logout = new Intent(context, LoginActivity.class);
        context.startActivity(logout);
    }


    public User getCurrentUser() {
        return currentUser;
    }





    public void test(){
        //START





        //END
    }


}
