package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
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
    private static final String PASSWORD_RESET_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#";

    private EditText inpCpr, inpNewPassword, inpConfirmPassword, inpResetCode;
    private Button btnNext, btnUpdate;

    private FirebaseDatabase database;
    private String resetCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();

        if (savedInstanceState != null) {
            inpCpr.setText(savedInstanceState.getString(getString(R.string.cpr)));
            inpNewPassword.setText(savedInstanceState.getString(getString(R.string.new_password)));
            inpConfirmPassword.setText(savedInstanceState.getString(getString(R.string.confirm_password)));
            inpResetCode.setText(savedInstanceState.getString(getString(R.string.reset_code)));
        }

    }

    /**
     * This method initializes xml to java
     */
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


    /**
     * This method sends a password reset code to the users mail and prepares the view
     * to accept resetCode and new password
     */

    public void sendResetCode(View view) {
        String cpr = inpCpr.getText().toString();
        // Check if Cpr is valid (Length)
        if (cpr.length() == 10) {


            DatabaseReference databaseReference = database.getReference("users/" + cpr);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Check if user exists and if user has a password
                    if (dataSnapshot.getValue() != null && dataSnapshot.hasChild("password")) {

                        // A user with that cpr exists!
                        String email = dataSnapshot.child("email").getValue(String.class);
                        // Generate resetCode
                        resetCode = randomResetCode(6);
                        // Send mail to user with reset code
                        new SendMail(getApplicationContext(), email, "Password reset", "<h1>You have requested a password reset</h1><br/>Reset code: " + resetCode).execute();
                        // Prepare view for reset code and new password
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
            Toast.makeText(getApplicationContext(), "Please input a valid cpr!", Toast.LENGTH_LONG).show();
        }


    }

    /**
     * This method generates a random reset code
     */
    public static String randomResetCode(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * PASSWORD_RESET_STRING.length());
            builder.append(PASSWORD_RESET_STRING.charAt(character));
        }
        return builder.toString();
    }

    /**
     * This method updates the users password if the resetCode and new passwords match
     */
    public void updatePassword(View view) {
        String enteredRequestCode = inpResetCode.getText().toString();
        String newPassword = inpNewPassword.getText().toString();
        String confirmPassword = inpConfirmPassword.getText().toString();

        if (enteredRequestCode.equals(resetCode) && newPassword.equals(confirmPassword)) {
            DatabaseReference passwordRef = database.getReference("users/" + inpCpr.getText().toString() + "/password");
            passwordRef.setValue(newPassword);
            Toast.makeText(getApplicationContext(), "Your password has been updated", Toast.LENGTH_LONG).show();

            Intent backToLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(backToLogin);

        } else {
            Toast.makeText(getApplicationContext(), "Request code or passwords don't match!", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.cpr), this.inpCpr.getText().toString());
        outState.putString(getString(R.string.new_password), this.inpNewPassword.getText().toString());
        outState.putString(getString(R.string.confirm_password), this.inpConfirmPassword.getText().toString());
        outState.putString(getString(R.string.reset_code), this.inpResetCode.getText().toString());

    }
}
