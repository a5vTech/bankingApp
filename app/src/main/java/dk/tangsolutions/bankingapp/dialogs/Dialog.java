package dk.tangsolutions.bankingapp.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.activities.TransferActivity;

public class Dialog extends DialogFragment {
    private Context context;
    private Button btnOtherAccounts, btnMyAccounts;

    public Dialog() {
    }


    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.transfer_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        // Create the AlertDialog object and return it

        btnMyAccounts = view.findViewById(R.id.btn_myAccounts);
        btnOtherAccounts = view.findViewById(R.id.btn_otherAccounts);

        btnMyAccounts.setOnClickListener(v -> {
            Intent intent = new Intent(context, TransferActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        btnOtherAccounts.setOnClickListener(v -> {
            Intent intent = new Intent(context, TransferActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        return builder.create();


    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
