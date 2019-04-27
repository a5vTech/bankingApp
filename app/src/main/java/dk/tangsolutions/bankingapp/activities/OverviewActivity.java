package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.models.OverviewAdapter;
import dk.tangsolutions.bankingapp.models.BankAccount;
import dk.tangsolutions.bankingapp.models.User;
import dk.tangsolutions.bankingapp.services.UserService;

public class OverviewActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase database;
    private FirebaseAuth auth;

    private BottomNavigationView navigation;
    private RecyclerView rv_account_list;
    private String TAG = "OVERVIEW";

    private ArrayList<BankAccount> bankAccounts = new ArrayList<>();
    private OverviewAdapter adapter = new OverviewAdapter(this, bankAccounts);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Log.d("DGDG", "OVERVIEW LOADED");
        init();
        loadData();
        Log.d("DGDG", "DATA LOADED");


    }

    private void loadData() {
        // Create reference to the current users bankaccounts
        DatabaseReference ref = database.getReference("usersBankAccounts/" + "1234567777");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bankAccounts.clear();
                Log.d("DGDG", "DATA HAS CHANGED: " + dataSnapshot.getValue());
                Log.d("DGDG", "CHILDREN:   " + dataSnapshot.getChildren());

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("DGDG", "DATA POSTSNAP KEY: " + postSnapshot.getKey());

                    DatabaseReference accRef = database.getReference("bankaccounts/" + postSnapshot.getKey());
                    //Load once (Update on reload)
                    accRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            BankAccount bankAccount = dataSnapshot.getValue(BankAccount.class);
                            Log.d("DGDG", postSnapshot.getKey());
                            bankAccount.setAccountNumber(postSnapshot.getKey());
                            bankAccounts.add(dataSnapshot.getValue(BankAccount.class));
                            adapter.notifyDataSetChanged();
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


        // Add Value listener to the users bankaccounts (get live updates)


    }


    private void loadData1() {
        DatabaseReference ref = database.getReference("bankaccounts");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bankAccounts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BankAccount bankAccount = postSnapshot.getValue(BankAccount.class);
//                    Log.d("DEBUGGGGGG", bankAccount.getTransactions().get(0).getTo());
                    bankAccounts.add(bankAccount);
                    adapter.notifyDataSetChanged();

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void init() {
        this.auth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.navigation = findViewById(R.id.navigation);
        this.navigation.setOnNavigationItemSelectedListener(this);
        this.rv_account_list = findViewById(R.id.rv_account_list);
        this.rv_account_list.setAdapter(adapter);
        this.rv_account_list.setLayoutManager(new LinearLayoutManager(this));


//    createDummyValuesInDB();


//        //TEST to create entry in db
//        Transaction transaction = new Transaction();
//        transaction.setAmount(200.0);
//        transaction.setTo("ME");
//        transaction.setDate(new Date());
//        ArrayList<Transaction> transactions = new ArrayList<>();
//        transactions.add(transaction);
//        Transaction transaction1 = new Transaction();
//
//        transaction1.setTo("STRANGER");
//        transaction1.setAmount(300.0);
//        transaction1.setDate(new Date());
//        transactions.add(transaction1);
//        bankAccount.setTransactions(transactions);
//
//
//
//        //Add bankAccount to DB as well as update any fields in bankAccount - transactions + more
//        DatabaseReference reference = database.getReference("bankaccounts/"+bankAccount.getAccountNumber());
//        reference.setValue(bankAccount);


    }

    private void createDummyValuesInDB() {
        BankAccount bankAccount = new BankAccount();
//        bankAccount.setAccountName("Løn");
//        bankAccount.transferMoney("Jesper", "Mikkel", 500.0, true, new Date());
//        bankAccount.transferMoney("Jesper", "Mikkel", 500.0, false, new Date());
//        bankAccount.transferMoney("Jesper", "Mikkel", 20, true, new Date());
//        bankAccount.setAccountNumber("785225");

        //Add bankAccount to DB as well as update any fields in bankAccount - transactions + more
//        DatabaseReference reference = database.getReference("bankaccounts/" + bankAccount.getAccountNumber());
//        reference.setValue(bankAccount);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_overview:
                Intent intent = new Intent(this, OverviewActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.navigation_pay:

                return true;
            case R.id.navigation_transfer:

                return true;

            case R.id.navigation_menu:

                return true;
        }
        return false;
    }


}
