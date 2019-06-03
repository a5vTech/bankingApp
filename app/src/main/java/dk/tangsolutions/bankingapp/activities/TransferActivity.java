package dk.tangsolutions.bankingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.models.AutoPayment;
import dk.tangsolutions.bankingapp.models.BankAccount;
import dk.tangsolutions.bankingapp.services.AuthService;
import dk.tangsolutions.bankingapp.services.SendMail;

public class TransferActivity extends AppCompatActivity {
    public static final String TAG = "TRANSFERACTIVITY";
    private EditText inpAmount, inpTextForYou, inpTextForReciever, inpDate, inpToAff, inpToAccountNumber;
    private Spinner spinnerFrom, spinnerTo;
    private static int TRANSFERSTATE = 1;
    private static Boolean PAYBILL;
    private CheckBox cb_autoPay;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        init();
        loadAccountData();


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
        this.cb_autoPay = findViewById(R.id.cb_autoPay);
        // Get transferState from intent
        TRANSFERSTATE = getIntent().getIntExtra(getString(R.string.TRANSFERSTATE), 0);
        PAYBILL = getIntent().getBooleanExtra("BILLS", false);


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

    private void loadAccountData() {
        Log.d(TAG, "loadAccountData called");
        AuthService auth = new AuthService();
        ArrayAdapter<BankAccount> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, auth.getCurrentUser().getBankaccounts());
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);


    }


    public void transfer(View view) {
        Log.d(TAG, "Transfer called");

        // Check if amount is entered
        if (inpAmount.getText().toString().length() < 1) {
            Toast.makeText(getApplicationContext(), "Please enter a valid amount", Toast.LENGTH_LONG).show();
            return;
        }

        AuthService auth = new AuthService();
        String to = spinnerTo.getSelectedItem().toString().substring(spinnerTo.getSelectedItem().toString().lastIndexOf(" ") + 1);
        String toText = inpTextForReciever.getText().toString();
        String fromText = inpTextForYou.getText().toString();
        String from = spinnerFrom.getSelectedItem().toString().substring(spinnerFrom.getSelectedItem().toString().lastIndexOf(" ") + 1);
        Double amount = Double.parseDouble(inpAmount.getText().toString());


        // Log data from view
        Log.d(TAG, "To: " + to);
        Log.d(TAG, "To text: " + toText);
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "From text: " + fromText);
        Log.d(TAG, "Amount: " + amount);

        // TransferState 1 => Own accounts
        if (TRANSFERSTATE == 1) {
            //Withdraw
            transferMoney(from, auth.getCurrentUser().getAffiliate(), fromText, amount, false);
            // Deposit
            transferMoney(to, auth.getCurrentUser().getAffiliate(), toText, amount, true);

            if (cb_autoPay.isChecked()) {
                DatabaseReference autoPayRef = database.getReference("autoPayments");
                AutoPayment autoPayment = new AutoPayment(amount, to, from, "end");
                autoPayRef.push().setValue(autoPayment);
            }
            success();
        }
        // TransferState 2 => Other accounts
        else if (TRANSFERSTATE == 2) {

            // Check if affiliate is empty
            if (inpToAff.getText().toString().length() < 1) {
                Toast.makeText(getApplicationContext(), "Please enter an affiliate! ", Toast.LENGTH_LONG).show();
                // Empty return - Stops the method from executing more code
                return;
            }

            //Check if accNumber is empty

            if (inpToAccountNumber.getText().toString().length() < 1) {
                Toast.makeText(getApplicationContext(), "Please enter an account number! ", Toast.LENGTH_LONG).show();
                return;
            }


            int transferToAff = Integer.parseInt(inpToAff.getText().toString());


            String transferToAccountNumber = inpToAccountNumber.getText().toString();


            //Check if account exists
            DatabaseReference checkAccRef = database.getReference("bankaccounts").child("" + transferToAff).child(transferToAccountNumber);
            checkAccRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        // Open EasyId activity

                        Intent easyId = new Intent(getApplicationContext(), EasyIdActivity.class);
                        String code = randomEasyIdCode();
                        new SendMail(getApplicationContext(), auth.getCurrentUser().getEmail(), "EasyId code from KEA BANK", "Here is your EasyId code: " + code).execute();

                        easyId.putExtra("EasyIdCode", code);
                        startActivityForResult(easyId, 22);

//                        // Withdraw
//                        transferMoney(from, auth.getCurrentUser().getAffiliate(), fromText, amount, false);
//                        //Deposit
//                        transferMoney(transferToAccountNumber, transferToAff, toText, amount, true);
//                        success();
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
                if (amount > currentBalance && !deposit) {
                    //TODO : Add snackbar error message
                    Snackbar.make(findViewById(R.id.cord_layout), "You don't have enough money to complete the transaction", Snackbar.LENGTH_LONG).show();

                } else {

                    if (deposit) {
                        bankaccountsref.child(accNumber).child(getString(R.string.BALANCE)).setValue(currentBalance + amount);
                    } else {
                        bankaccountsref.child(accNumber).child(getString(R.string.BALANCE)).setValue(currentBalance - amount);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public String randomEasyIdCode() {
        Random random = new Random();
        return "" + random.nextInt(99999) + 1300;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AuthService auth = new AuthService();
        String toText = inpTextForReciever.getText().toString();
        String fromText = inpTextForYou.getText().toString();
        String from = spinnerFrom.getSelectedItem().toString().substring(spinnerFrom.getSelectedItem().toString().lastIndexOf(" ") + 1);
        Double amount = Double.parseDouble(inpAmount.getText().toString());
        int transferToAff = Integer.parseInt(inpToAff.getText().toString());


        String transferToAccountNumber = inpToAccountNumber.getText().toString();

        if (requestCode == 22) {
            if (resultCode == RESULT_OK) {
                // Withdraw
                transferMoney(from, auth.getCurrentUser().getAffiliate(), fromText, amount, false);
                //Deposit
                transferMoney(transferToAccountNumber, transferToAff, toText, amount, true);


                if (cb_autoPay.isChecked()) {
                    DatabaseReference autoPayRef = database.getReference("autoPayments");
                    AutoPayment autoPayment = new AutoPayment(amount, transferToAccountNumber, from, "end");
                    autoPayRef.push().setValue(autoPayment);
                }


                success();
            }

        }
    }
}
