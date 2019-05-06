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

    }

    private void init() {
        Log.d(TAG, "Init method called");
        this.inpUsername = findViewById(R.id.inp_userName);
        this.inpPassword = findViewById(R.id.inp_loginpassword);
        this.btn_login = findViewById(R.id.btn_login);
        this.cb_remember = findViewById(R.id.cb_remember);
    }


    public void tempLogin(View view) {
        AuthService auth = new AuthService();
        String cpr = inpUsername.getText().toString();
        String password = inpPassword.getText().toString();
        auth.login(cpr, password, getApplicationContext());


    }

    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }


    public void test() {
        new SendMail(getApplicationContext(), "jesper2604@gmail.com", "Test mail", "besked her").execute();

    }

}
