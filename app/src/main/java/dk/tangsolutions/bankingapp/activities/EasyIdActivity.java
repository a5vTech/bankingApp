package dk.tangsolutions.bankingapp.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dk.tangsolutions.bankingapp.R;

public class EasyIdActivity extends AppCompatActivity {
    private EditText inpEasyIdCode;
    private Button btnConfirmPayment;
    private String EasyIdCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_id);
        init();


    }

    private void init() {
        this.inpEasyIdCode = findViewById(R.id.inp_EasyIdCode);
        this.btnConfirmPayment = findViewById(R.id.btn_confirmPayment);
        Intent intent = getIntent();
        EasyIdCode = intent.getStringExtra("EasyIdCode");
    }


    public void confirmPayment(View view) {
        String EasyIdCode = inpEasyIdCode.getText().toString();
        if (EasyIdCode.equals(this.EasyIdCode)) {
            Intent success = new Intent();
            setResult(RESULT_OK, success);
            finish();
        } else {
            Toast.makeText(this, "Wrong EasyID code. Please try again", Toast.LENGTH_LONG).show();
        }

    }


}
