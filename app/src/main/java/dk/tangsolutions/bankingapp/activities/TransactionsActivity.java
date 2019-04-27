package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.models.BankAccount;

public class TransactionsActivity extends AppCompatActivity {
    private TextView currBal, transaction1;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        init();
        loadBankAccount();
    }

    private void loadBankAccount() {
        // Fetch accountNumber from previous activity
        Intent intent = getIntent();
        BankAccount currBankAccount = intent.getParcelableExtra(getString(R.string.CURRENTBANKACC));

        this.currBal.setText("Current balance: "+currBankAccount.getBalance());
        // Get Database reference
//        DatabaseReference ref = database.getReference("bankaccounts/" + currBankAccountNumber);
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                BankAccount currBankAccount = dataSnapshot.getValue(BankAccount.class);
//                ActionBar actionBar = getSupportActionBar();
//                actionBar.setTitle(currBankAccount.getName());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }

    private void init() {
        this.database = FirebaseDatabase.getInstance();
        this.currBal = findViewById(R.id.tv_currentBal);
        this.transaction1 = findViewById(R.id.transaction1);


        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle(currBankAccount.getAccountName());


    }
}
