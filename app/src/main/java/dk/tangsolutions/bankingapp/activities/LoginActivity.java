package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import dk.tangsolutions.bankingapp.R;

public class LoginActivity extends AppCompatActivity {
    private String TAG = "LOGINACTIVITY";
    private EditText username, password;
    private Button btn_login;
    private CheckBox cb_remember;
    private final String LOGINPREFS = "LOGINPREFS";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();


    }

    private void init() {
        Log.d(TAG, "Init method called");
        this.username = findViewById(R.id.userName);
        this.password = findViewById(R.id.password);
        this.btn_login = findViewById(R.id.btn_login);
        this.cb_remember = findViewById(R.id.cb_remember);
    }


    public void tempLogin(View view){
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }








}
