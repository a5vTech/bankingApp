package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.services.AuthService;

public class MenuActivity extends AppCompatActivity {

    private TextView tv_bankName, tv_bankPhoneNo, tv_location;
    private AuthService auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
    }


    public void requestNewBankAccounts(View view) {
        Intent requestNewAccount = new Intent(this, RequestNewBankAccountsActivity.class);
        startActivity(requestNewAccount);
    }

    private void init() {
        this.tv_bankName = findViewById(R.id.tv_bankName);
        this.tv_bankPhoneNo = findViewById(R.id.tv_bankPhoneNo);
        this.tv_location = findViewById(R.id.tv_bankLocation);
        this.auth = new AuthService();
        this.database = FirebaseDatabase.getInstance();
        loadBankInformation();
    }


    /**
     * This method loads the bank information (From the bank assosiated with the user)
     */
    private void loadBankInformation() {
        int affiliate = auth.getCurrentUser().getAffiliate();
        DatabaseReference bankRef = database.getReference("banks/" + affiliate);
        bankRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    if (value.getKey().equals("name")) {
                        tv_bankName.setText(value.getValue(String.class));
                    } else if (value.getKey().equals("location")) {
                        tv_location.setText("Location: " + value.getValue(String.class));
                    } else if (value.getKey().equals("phonenumber")) {
                        tv_bankPhoneNo.setText("Phonenumber: " + value.getValue());
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void logout(View view) {
        auth.logout(this);
        finish();
    }

}


