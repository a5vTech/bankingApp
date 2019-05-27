package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
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
import java.util.List;

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.models.BankAccount;
import dk.tangsolutions.bankingapp.services.AuthService;

public class NewBankAccountsActivity extends AppCompatActivity {
    //    private Button btnRequest;
    private Spinner spinAccountTypes;
    private EditText inpAccountName;

    private AuthService auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bank_accounts);
        init();
    }

    private void init() {
//        this.btnRequest = findViewById(R.id.btn_request);
        this.spinAccountTypes = findViewById(R.id.spin_accTypes);
        this.inpAccountName = findViewById(R.id.inp_accountName);
        this.auth = new AuthService();
        this.database = FirebaseDatabase.getInstance();
        // Initialize spinner data
        loadAccountTypes();
    }

    private void loadAccountTypes() {
        List<String> accountTypes = new ArrayList<>();
        accountTypes.add("Default");
        accountTypes.add("Budget");
        accountTypes.add("Pension");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, accountTypes);
        this.spinAccountTypes.setAdapter(adapter);
    }

    public void newAccount(View view) {
        Log.d("TESTER", "CALLED");
        String accountChosen = spinAccountTypes.getSelectedItem().toString();
        String accountName = inpAccountName.getText().toString();
        DatabaseReference accountNumberRef = database.getReference("nextAccNumber");
        accountNumberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Load nextAccountNumber
                long accountNumber = Long.parseLong(dataSnapshot.getValue(String.class));
                // CREATE ACCOUNT
                BankAccount bankAccount = new BankAccount(accountChosen, accountName, 0, "" + accountNumber);

                // Create and save bankaccount in database

                String cpr = auth.getCurrentUser().getCpr();
                int affiliate = auth.getCurrentUser().getAffiliate();

                DatabaseReference bankaccountsRef = database.getReference("bankaccounts/" + affiliate);
                DatabaseReference usersBankAccountsRef = database.getReference("usersBankAccounts/" + cpr);

                bankaccountsRef.child(bankAccount.getAccountNumber()).setValue(bankAccount);
                usersBankAccountsRef.child(bankAccount.getAccountNumber()).setValue(bankAccount.getName());

                // INCREMENT ACCOUNT NUMBER
                accountNumberRef.setValue("" + (accountNumber + 1));
                Toast.makeText(getApplicationContext(), "You have now received a new bankaccount", Toast.LENGTH_LONG).show();
                Intent goToOverview = new Intent(getApplicationContext(), OverviewActivity.class);
                startActivity(goToOverview);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
