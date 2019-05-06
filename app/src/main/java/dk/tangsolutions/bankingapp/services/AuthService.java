package dk.tangsolutions.bankingapp.services;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dk.tangsolutions.bankingapp.activities.LoginActivity;
import dk.tangsolutions.bankingapp.activities.OverviewActivity;
import dk.tangsolutions.bankingapp.models.User;

public class AuthService {

    private static User currentUser;
    private FirebaseDatabase database;

    public AuthService() {
        database = FirebaseDatabase.getInstance();
    }


    public void login(String cpr, String password, Context context) {
        if (cpr.length() > 0 && password.length() > 1) {
            // Create reference to the user who is trying to login
            DatabaseReference userRef = database.getReference("users/" + cpr);
            // Add a listener to check if the user exists and the password matches
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Check if the user exists
                    if (dataSnapshot.getValue() != null) {
                        // If user exists check if the password provided is correct
                        if (dataSnapshot.child("password").getValue().equals(password) && dataSnapshot.hasChild("password")) {
                            Intent loginIntent = new Intent(context, OverviewActivity.class);
                            // Add 'NEW TASK' flag to allow the start of a new activity from outside an activity
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(loginIntent);

                            // Set the current user to the user who's signed in
                            currentUser = dataSnapshot.getValue(User.class);
                        } else {
                            Toast.makeText(context, "Wrong cpr or password!", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(context, "A user with this cpr does not exist!", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(context, "Please enter valid info!", Toast.LENGTH_SHORT).show();
        }

        //If login is not successful return error msg

    }

    public void logout(Context context) {
        currentUser = null;
        Intent logout = new Intent(context, LoginActivity.class);
        context.startActivity(logout);
    }


    public User getCurrentUser() {
        return currentUser;
    }


}
