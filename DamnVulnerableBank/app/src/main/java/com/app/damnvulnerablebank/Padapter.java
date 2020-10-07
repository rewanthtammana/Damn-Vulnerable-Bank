package com.app.damnvulnerablebank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Padapter extends RecyclerView.Adapter<Padapter.ViewHolder>{
    LayoutInflater inflater;
    List<PendingBeneficiaryRecords> precords;
    private Padapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(Padapter.OnItemClickListener listener){
        mListener =listener;
    }

    public Padapter(Context ctx,List<PendingBeneficiaryRecords> precords){
        this.inflater=LayoutInflater.from(ctx);
        this.precords=precords;
    }

    @NonNull
    @Override
    public Padapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.custom_pend,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Padapter.ViewHolder holder, int position) {
        holder.account_number.setText(precords.get(position).getAccountNumber());
        holder.beneficiary_account_number.setText(precords.get(position).getBeneficiaryAccountNumber());
        holder.idd.setText(precords.get(position).getId());

    }

    @Override
    public int getItemCount() {
        return precords.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView account_number,beneficiary_account_number,idd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            account_number=itemView.findViewById(R.id.accno);
            beneficiary_account_number=itemView.findViewById(R.id.beacc);
            idd=itemView.findViewById(R.id.idn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position =getAdapterPosition();
                        if(position !=RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}

