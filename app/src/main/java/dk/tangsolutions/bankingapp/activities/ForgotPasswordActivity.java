package dk.tangsolutions.bankingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.services.SendMail;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#";

    private EditText inpCpr, inpNewPassword, inpConfirmPassword, inpResetCode;
    private Button btnNext, btnUpdate;

    private FirebaseDatabase database;

    private String resetCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();

    }

    private void init() {
        this.database = FirebaseDatabase.getInstance();
        this.inpCpr = findViewById(R.id.inp_forgotCpr);
        this.btnNext = findViewById(R.id.btn_passwordReset);
        this.inpResetCode = findViewById(R.id.inp_resetCode);
        this.inpNewPassword = findViewById(R.id.inp_newPassword);
        this.inpConfirmPassword = findViewById(R.id.inp_confirmPassword);
        this.btnUpdate = findViewById(R.id.btn_updatepassword);
        this.inpResetCode.setVisibility(View.GONE);

    }


    public void sendResetCode(View view) {
        String cpr = inpCpr.getText().toString();
        if (cpr.length() > 1) {


            DatabaseReference databaseReference = database.getReference("users/" + cpr);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChild("password")) {


                        // A user with that cpr exists!
                        String email = dataSnapshot.child("email").getValue(String.class);
                        resetCode = randomAlphaNumeric(6);
                        new SendMail(getApplicationContext(), email, "Password reset", "<h1>You have requested a password reset</h1><br/>Reset code: " + resetCode).execute();
                        btnNext.setVisibility(View.GONE);
                        inpResetCode.setVisibility(View.VISIBLE);
                        inpNewPassword.setVisibility(View.VISIBLE);
                        inpConfirmPassword.setVisibility(View.VISIBLE);
                        btnUpdate.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            showToast(getApplicationContext(), "Please input a valid cpr!", Toast.LENGTH_LONG);
        }


    }


    public void showToast(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }


    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


    public void updatePassword(View view) {
        String enteredRequestCode = inpResetCode.getText().toString();
        String newPassword = inpNewPassword.getText().toString();
        String confirmPassword = inpConfirmPassword.getText().toString();

        if (enteredRequestCode.equals(resetCode) && newPassword.equals(confirmPassword)) {
            DatabaseReference passwordRef = database.getReference("users/" + inpCpr.getText().toString() + "/password");
            passwordRef.setValue(newPassword);
            showToast(getApplicationContext(), "Your password has been updated", Toast.LENGTH_LONG);

            Intent backToLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(backToLogin);

        } else {
            showToast(getApplicationContext(), "Request code or passwords dont match!", Toast.LENGTH_LONG);
        }


    }
}
