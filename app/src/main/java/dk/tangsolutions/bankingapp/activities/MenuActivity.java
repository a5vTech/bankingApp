package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import dk.tangsolutions.bankingapp.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }


    public void requestNewBankAccounts(View view) {
        Intent requestNewAccount = new Intent(this, NewBankAccountsActivity.class);
        startActivity(requestNewAccount);
    }
}
