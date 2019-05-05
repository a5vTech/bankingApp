package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.dialogs.Dialog;
import dk.tangsolutions.bankingapp.models.BankAccount;
import dk.tangsolutions.bankingapp.services.AuthService;

public class TransferActivity extends AppCompatActivity {
    public static final String TAG = "TRANSFERACTIVITY";
    private EditText inpAmount, inpTextForYou, inpTextForReciever, inpDate, inpToAff, inpToAccountNumber;
    private Spinner spinnerFrom, spinnerTo;
    private static int TRANSFERSTATE = 1;

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


        if (TRANSFERSTATE == 1) {
            //Withdraw
            transferMoney(from, auth.getCurrentUser().getAffiliate(), fromText, amount, false);
            // Deposit
            transferMoney(to, auth.getCurrentUser().getAffiliate(), toText, amount, true);
            success();
        } else {
            int transferToAff = Integer.parseInt(inpToAff.getText().toString());
            String transferToAccountNumber = inpToAccountNumber.getText().toString();


            //Check if account exists
            DatabaseReference checkAccRef = database.getReference("bankaccounts").child("" + transferToAff).child(transferToAccountNumber);
            checkAccRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        // Withdraw
                        transferMoney(from, auth.getCurrentUser().getAffiliate(), fromText, amount, false);
                        //Deposit
                        transferMoney(transferToAccountNumber, transferToAff, toText, amount, true);
                        success();
                    } else {
                        Snackbar.make(findViewById(R.id.cord_layout), "The account number or affiliate does not exist", Snackbar.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }


    public void success() {
        Toast.makeText(getApplicationContext(), "Your money has been transferred successfully!", Toast.LENGTH_SHORT).show();
        Intent goBackToOverview = new Intent(getApplicationContext(), OverviewActivity.class);
        startActivity(goBackToOverview);
    }

    public void transferMoney(String accNumber, int affiliate, String text, Double amount, Boolean deposit) {
        DatabaseReference bankaccountsref = database.getReference("bankaccounts/" + affiliate);
        bankaccountsref.child(accNumber).child(getString(R.string.BALANCE)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long currentBalance = dataSnapshot.getValue(Long.class);
                if (amount > currentBalance) {
                    //TODO : Add snackbar error message
                    Snackbar.make(findViewById(R.id.cord_layout), "You don't have enough money to complete the transaction", Snackbar.LENGTH_LONG).show();

                } else {

                    if (deposit) {
                        bankaccountsref.child(accNumber).child("balance").setValue(currentBalance + amount);
                    } else {
                        bankaccountsref.child(accNumber).child("balance").setValue(currentBalance - amount);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void init() {
        this.inpAmount = findViewById(R.id.inp_transferAmount);
        this.inpTextForYou = findViewById(R.id.inp_textForYou);
        this.inpTextForReciever = findViewById(R.id.inp_textForReceiver);
        this.inpDate = findViewById(R.id.inp_transferDate);
        this.spinnerFrom = findViewById(R.id.spinner_from);
        this.spinnerTo = findViewById(R.id.spinner_to);
        this.database = FirebaseDatabase.getInstance();
        this.inpToAff = findViewById(R.id.inp_affNumber);
        this.inpToAccountNumber = findViewById(R.id.inp_toAccountNumber);

        // Get transferState from intent
        TRANSFERSTATE = getIntent().getIntExtra(getString(R.string.TRANSFERSTATE), 0);


        // Change view dynamically to match transferState
        if (TRANSFERSTATE == 1) {
            spinnerTo.setVisibility(View.VISIBLE);
            inpToAff.setVisibility(View.GONE);
            inpToAccountNumber.setVisibility(View.GONE);
        } else {
            spinnerTo.setVisibility(View.GONE);
            inpToAff.setVisibility(View.VISIBLE);
            inpToAccountNumber.setVisibility(View.VISIBLE);
        }

    }


}
