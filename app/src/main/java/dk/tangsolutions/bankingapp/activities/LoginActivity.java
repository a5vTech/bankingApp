package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.models.BankAccount;
import dk.tangsolutions.bankingapp.models.User;
import dk.tangsolutions.bankingapp.services.UserService;

public class LoginActivity extends AppCompatActivity {
    private String TAG = "LOGINACTIVITY";
    private final String LOGINPREFS = "LOGINPREFS";

    private EditText username, password;
    private Button btn_login;
    private CheckBox cb_remember;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        createUser();


    }

    private void init() {
        Log.d(TAG, "Init method called");
        this.username = findViewById(R.id.inp_userName);
        this.password = findViewById(R.id.inp_password);
        this.btn_login = findViewById(R.id.btn_login);
        this.cb_remember = findViewById(R.id.cb_remember);
    }


    public void tempLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), OverviewActivity.class);
        startActivity(intent);
    }

    public void createUser() {

    }


}
