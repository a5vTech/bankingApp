package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.dialogs.Dialog;
import dk.tangsolutions.bankingapp.models.OverviewAdapter;
import dk.tangsolutions.bankingapp.models.BankAccount;
import dk.tangsolutions.bankingapp.services.AuthService;

public class OverviewActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "OVERVIEWACTIVITY";
    private FirebaseDatabase database;

    private BottomNavigationView navigation;
    private RecyclerView rv_account_list;

    private ArrayList<BankAccount> bankAccounts = new ArrayList<>();
    private OverviewAdapter adapter = new OverviewAdapter(this, bankAccounts);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Log.d(TAG, "onCreate called");
        init();
        loadData();



    }


    // Load users bankAccounts
    private void loadData() {
        Log.d(TAG, "Load data called");
        AuthService auth = new AuthService();

        // Create reference to the current users bankaccounts
        DatabaseReference ref = database.getReference("usersBankAccounts/" + auth.getCurrentUser().getCpr());

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
                auth.getCurrentUser().setBankaccounts(bankAccounts);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    // Initialize activity ( Map xml to java )
    private void init() {
        Log.d(TAG, "Init called");
        this.database = FirebaseDatabase.getInstance();
        this.navigation = findViewById(R.id.navigation);
        this.navigation.setOnNavigationItemSelectedListener(this);
        this.rv_account_list = findViewById(R.id.rv_account_list);
        this.rv_account_list.setAdapter(adapter);
        this.rv_account_list.setLayoutManager(new LinearLayoutManager(this));
    }




    // Navigation bar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_overview:
                Intent overviewIntent = new Intent(this, OverviewActivity.class);
                startActivity(overviewIntent);
                finish();
                return true;
            case R.id.navigation_pay:

                return true;
            case R.id.navigation_transfer:
                DialogFragment dialogFragment = new Dialog();
                ((Dialog) dialogFragment).setContext(this);
                dialogFragment.show(getSupportFragmentManager(), "miss");

                return true;

            case R.id.navigation_menu:

                return true;
        }
        return false;
    }


}
