package dk.tangsolutions.bankingapp.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.models.BankAccount;
import dk.tangsolutions.bankingapp.models.User;

public class RegisterActivity extends AppCompatActivity implements LocationListener {
    private Button btnRegister;
    private EditText inpCpr, inpFirstname, inpLastname, inpEmail, inpPhonenumber, inpAddress, inpPassword;
    private FirebaseDatabase database;
    private Spinner spinnerAffiliate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        // Handle state changes
        if (savedInstanceState != null) {
            User tempUser = savedInstanceState.getParcelable("tempUser");
            inpCpr.setText(tempUser.getCpr());
            inpFirstname.setText(tempUser.getFirstName());
            inpLastname.setText(tempUser.getLastName());
            inpEmail.setText(tempUser.getEmail());
            inpPhonenumber.setText(tempUser.getPhoneNumber());
            inpAddress.setText(tempUser.getAddress());
            inpPassword.setText(tempUser.getPassword());
        }


    }

    private void init() {
        this.btnRegister = findViewById(R.id.btn_register);
        this.inpCpr = findViewById(R.id.inp_cpr);
        this.inpFirstname = findViewById(R.id.inp_firstname);
        this.inpLastname = findViewById(R.id.inp_lastname);
        this.inpEmail = findViewById(R.id.inp_email);
        this.inpPhonenumber = findViewById(R.id.inp_phonenumber);
        this.inpAddress = findViewById(R.id.inp_address);
        this.inpPassword = findViewById(R.id.inp_password);
        this.database = FirebaseDatabase.getInstance();
        this.spinnerAffiliate = findViewById(R.id.spinner_affiliate);
        ArrayList<String> affiliates = new ArrayList<>();
        affiliates.add("Copenhagen");
        affiliates.add("Odense");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, affiliates);
        this.spinnerAffiliate.setAdapter(adapter);

    }


    public void register(View view) {


        if (isValid()) {
            DatabaseReference ref = database.getReference("users/" + inpCpr.getText().toString());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        //Create and save new user

                        Log.d("REGISTER", "Registering");
                        User newUser = new User(
                                inpCpr.getText().toString(),
                                inpFirstname.getText().toString(),
                                inpLastname.getText().toString(),
                                inpEmail.getText().toString(),
                                inpPhonenumber.getText().toString(),
                                inpAddress.getText().toString(), inpPassword.getText().toString());

                        //TODO: CALC BASED ON LOCATION
                        if (spinnerAffiliate.getSelectedItem().toString().equals("Copenhagen")) {
                            newUser.setAffiliate(1);
                        } else {
                            newUser.setAffiliate(2);
                        }

                        //TODO: CALC ABOVE BASED ON LOCATION
                        ref.setValue(newUser);

                        //Create and attatch new bankaccounts to user
                        DatabaseReference accountNumberRef = database.getReference("nextAccNumber");
                        accountNumberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Fetch account number
                                long accountNumber = Long.parseLong(dataSnapshot.getValue(String.class));

                                //Create default and budget bankaccount
                                createDefaultAccounts(accountNumber, newUser.getCpr(), newUser.getAffiliate());
                                accountNumberRef.setValue("" + (accountNumber + 3));


                                // GO back to login
                                Intent backToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(backToLogin);
                                Toast.makeText(getApplicationContext(), "Welcome to KEA Bank - You can now login", Toast.LENGTH_LONG).show();
                                finish();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "A user with that cpr aldready exists!", Toast.LENGTH_LONG).show();
                    }

                    Log.d("DEBUGS", "" + dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }


    public void createDefaultAccounts(Long accountNumber, String cpr, int affiliate) {
        //Create bankaccounts
        DatabaseReference bankaccountsRef = database.getReference("bankaccounts/" + affiliate);
        long defaultAccountNumber = accountNumber;
        long budgetAccountNumber = defaultAccountNumber + 1;
        long pensionAccountNumber = defaultAccountNumber + 2;
        BankAccount defaultAcc = new BankAccount("Default", "Default", 0, Long.toString(defaultAccountNumber));
        BankAccount budgetAcc = new BankAccount("Budget", "Budget", 0, Long.toString(budgetAccountNumber));
        BankAccount pensionAcc = new BankAccount("Pension", "Pension", 500, Long.toString(pensionAccountNumber));
        bankaccountsRef.child(defaultAcc.getAccountNumber()).setValue(defaultAcc);
        bankaccountsRef.child(budgetAcc.getAccountNumber()).setValue(budgetAcc);
        bankaccountsRef.child(pensionAcc.getAccountNumber()).setValue(pensionAcc);

        // Attach new bankaccounts to user
        DatabaseReference usersBankAccountsRef = database.getReference("usersBankAccounts/" + cpr);
        usersBankAccountsRef.child(defaultAcc.getAccountNumber()).setValue(defaultAcc.getName());
        usersBankAccountsRef.child(budgetAcc.getAccountNumber()).setValue(budgetAcc.getName());
        usersBankAccountsRef.child(pensionAcc.getAccountNumber()).setValue(pensionAcc.getName());


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        User tempUser = new User();
        tempUser.setCpr(inpCpr.getText().toString());
        tempUser.setFirstName(inpFirstname.getText().toString());
        tempUser.setLastName(inpLastname.getText().toString());
        tempUser.setEmail(inpEmail.getText().toString());
        tempUser.setPhoneNumber(inpPhonenumber.getText().toString());
        tempUser.setAddress(inpAddress.getText().toString());
        tempUser.setPassword(inpPassword.getText().toString());
        outState.putParcelable("tempUser", tempUser);

    }

    public boolean isValid() {
        String cpr = inpCpr.getText().toString();
        String firstName = inpFirstname.getText().toString();
        String lastName = inpLastname.getText().toString();
        String email = inpEmail.getText().toString();
        String phoneNumber = inpPhonenumber.getText().toString();
        String address = inpAddress.getText().toString();
        String password = inpPassword.getText().toString();

        if (cpr.length() != 10) {
            Toast.makeText(this, "Please enter a valid cpr!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (firstName.isEmpty()) {
            Toast.makeText(this, "Please fill out a firstname!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (lastName.isEmpty()) {
            Toast.makeText(this, "Please fill out a lastname!", Toast.LENGTH_LONG).show();

            return false;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Please fill in an email!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill out a phonenumber!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (address.isEmpty()) {
            Toast.makeText(this, "Please fill out an address!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Please fill out a password!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
            return false;
        }

        // Default
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("LOC", "Lat: " + location.getLatitude() + " Lon: " + location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
