package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.models.BankAccount;
import dk.tangsolutions.bankingapp.services.AuthService;

public class TransferActivity extends AppCompatActivity {
    public static final String TAG = "TRANSFERACTIVITY";
    private EditText inpAmount, inpTextForYou, inpTextForReciever, inpDate;
    private Spinner spinnerFrom, spinnerTo;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        init();
        loadAccountData();

    }

    private void loadAccountData() {
        Log.d(TAG, "loadAccountData called");
        AuthService auth = new AuthService();
        ArrayAdapter<BankAccount> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, auth.getCurrentUser().getBankaccounts());
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);


    }


    public void transfer(View view) {
        AuthService auth = new AuthService();
        Log.d(TAG, "Transfer called");
        String to = spinnerTo.getSelectedItem().toString().substring(spinnerTo.getSelectedItem().toString().lastIndexOf(" ") + 1);
        String toText = inpTextForReciever.getText().toString();
        String fromText = inpTextForYou.getText().toString();
        String from = spinnerFrom.getSelectedItem().toString().substring(spinnerFrom.getSelectedItem().toString().lastIndexOf(" ") + 1);
        Double amount = Double.parseDouble(inpAmount.getText().toString());
        Log.d(TAG, "To: " + to);
        Log.d(TAG, "To text: " + toText);
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "From text: " + fromText);
        Log.d(TAG, "Amount: " + amount);


        // Withdraw amount and deposit on receiving account
        DatabaseReference bankaccountsref = database.getReference("bankaccounts/" + auth.getCurrentUser().getAffiliate());

        //withdraw
        bankaccountsref.child(from).child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long currentBalance = dataSnapshot.getValue(Long.class);

                if (amount > currentBalance) {
                    //TODO : Add snackbar error message
                    Snackbar.make(findViewById(R.id.cord_layout), "You don't have enough money to complete the transaction", Snackbar.LENGTH_LONG).show();


                } else {
                    bankaccountsref.child(from).child("balance").setValue(currentBalance - amount);
                    //deposit to new acc
                    bankaccountsref.child(to).child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Long currentBalance = dataSnapshot.getValue(Long.class);
                            bankaccountsref.child(to).child("balance").setValue(currentBalance + amount);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        Snackbar.make()
//        Snackbar.make(findViewById(R.id.cord_layout), "Money has been transferred", Snackbar.LENGTH_LONG).show();


        Intent goBackToOverview = new Intent(getApplicationContext(), OverviewActivity.class);
        startActivity(goBackToOverview);


    }


    private void init() {
        this.inpAmount = findViewById(R.id.inp_transferAmount);
        this.inpTextForYou = findViewById(R.id.inp_textForYou);
        this.inpTextForReciever = findViewById(R.id.inp_textForReceiver);
        this.inpDate = findViewById(R.id.inp_transferDate);

        this.spinnerFrom = findViewById(R.id.spinner_from);
        this.spinnerTo = findViewById(R.id.spinner_to);

        this.database = FirebaseDatabase.getInstance();


    }

}
