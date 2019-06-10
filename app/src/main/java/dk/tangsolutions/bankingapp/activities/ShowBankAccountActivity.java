package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.models.BankAccount;

public class ShowBankAccountActivity extends AppCompatActivity {
    private TextView tv_currBal, tv_accountName;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bank_account);
        init();
        loadBankAccount();
    }

    private void loadBankAccount() {
        // Fetch accountNumber from previous activity
        Intent intent = getIntent();
        BankAccount currBankAccount = intent.getParcelableExtra(getString(R.string.CURRENT_BANK_ACC));

        this.tv_currBal.setText("Current balance: " + currBankAccount.getBalance());
        this.tv_accountName.setText("Bank account: "+currBankAccount.getName());


    }

    private void init() {
        this.database = FirebaseDatabase.getInstance();
        this.tv_currBal = findViewById(R.id.tv_currentBal);
        this.tv_accountName = findViewById(R.id.tv_accountName);

//        ActionBar actionBar = getSupportActionBar();


    }
}
