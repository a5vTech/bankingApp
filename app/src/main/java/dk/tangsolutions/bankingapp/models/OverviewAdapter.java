package dk.tangsolutions.bankingapp.models;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dk.tangsolutions.bankingapp.R;
import dk.tangsolutions.bankingapp.activities.TransactionsActivity;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.myViewHolder> {
    Context mContext;
    List<BankAccount> mData;


    public OverviewAdapter(Context mContext, List<BankAccount> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.overview_card_item, viewGroup, false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
        myViewHolder.accName.setText(mData.get(i).getName());
        myViewHolder.accNo.setText(""+ mData.get(i).getAccountNumber());
        myViewHolder.accBal.setText("" + mData.get(i).getBalance());
        myViewHolder.cv_cardItem.setOnClickListener((view) -> {
            Intent intent = new Intent(mContext, TransactionsActivity.class);
            intent.putExtra(mContext.getString(R.string.CURRENT_BANK_ACC), mData.get(i));
            mContext.startActivity(intent);
        });



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private CardView cv_cardItem;
        private TextView accName, accNo, accBal;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            accName = itemView.findViewById(R.id.tv_acc_name);
            accNo = itemView.findViewById(R.id.tv_acc_no);
            accBal = itemView.findViewById(R.id.tv_acc_balance);
            cv_cardItem = itemView.findViewById(R.id.cv_cardItem);

        }
    }
}
