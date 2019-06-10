package dk.tangsolutions.bankingapp.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.services.SendMail;
import dk.tangsolutions.bankingapp.services.AuthService;

public class LoginActivity extends AppCompatActivity {
    private String TAG = "LOGINACTIVITY";
    private final String LOGINPREFS = "LOGINPREFS";

    private EditText inpUsername, inpPassword;
    private Button btn_login;
    private CheckBox cb_remember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();


        // Load saved instance data
        if (savedInstanceState != null) {
            inpUsername.setText(savedInstanceState.getString("cpr"));
            inpPassword.setText(savedInstanceState.getString("password"));
        }
        loadSharedPref();
    }

    private void loadSharedPref() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.SHARED_PREF_LOGIN), Context.MODE_PRIVATE);
        this.inpUsername.setText(preferences.getString("cpr", ""));
        this.cb_remember.setChecked(preferences.getBoolean("remember_me", false));
    }

    /**
     * This method initializes xml to java
     */

    private void init() {
        Log.d(TAG, "Init method called");
        this.inpUsername = findViewById(R.id.inp_userName);
        this.inpPassword = findViewById(R.id.inp_loginpassword);
        this.btn_login = findViewById(R.id.btn_login);
        this.cb_remember = findViewById(R.id.cb_remember);
    }


    public void login(View view) {
        AuthService auth = new AuthService();
        String cpr = inpUsername.getText().toString();
        String password = inpPassword.getText().toString();
        // Call auth service Login
        auth.login(cpr, password, getApplicationContext());

        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.SHARED_PREF_LOGIN), MODE_PRIVATE).edit();
        if (!this.cb_remember.isChecked()) {
            editor.clear();
        } else {
            editor.putString("cpr", cpr);
            editor.putBoolean("remember_me", this.cb_remember.isChecked());
        }
        editor.apply();


    }

    /**
     * This method starts the register activity
     */
    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }


    /**
     * This method starts the Forgot password Activity
     */
    public void forgotPassword(View view) {
        Intent forgotPassword = new Intent(this, ForgotPasswordActivity.class);
        startActivity(forgotPassword);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String cpr = inpUsername.getText().toString();
        String password = inpPassword.getText().toString();
        outState.putString("cpr", cpr);
        outState.putString("password", password);
    }
}
