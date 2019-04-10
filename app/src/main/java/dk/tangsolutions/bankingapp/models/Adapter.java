package dk.tangsolutions.bankingapp.models;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import dk.tangsolutions.bankingapp.R;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {
    Context mContext;
    List<BankAccount> mData;


    public Adapter(Context mContext, List<BankAccount> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item, viewGroup, false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
//        myViewHolder.courseSubject.setText(mData.get(i).getSubject());
        myViewHolder.bankAccount.setOnClickListener((View) -> {
//            Intent rateCourseIntent = new Intent(mContext, RateCourseActivity.class);
//            rateCourseIntent.putExtra("Course", mData.get(i));
//            mContext.startActivity(rateCourseIntent);

        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        Button bankAccount;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            bankAccount = itemView.findViewById(R.id.bankAccount);

        }
    }
}
