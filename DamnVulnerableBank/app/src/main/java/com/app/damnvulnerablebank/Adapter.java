package com.app.damnvulnerablebank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<TransactionRecords> trecords;

    public Adapter(Context ctx,List<TransactionRecords> trecords){
        this.inflater=LayoutInflater.from(ctx);
        this.trecords=trecords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.custom_list,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fromacc.setText(trecords.get(position).getFromaccnt());
        holder.toacc.setText(trecords.get(position).getToaccnt());
        holder.amount.setText(trecords.get(position).getAmount());

    }

    @Override
    public int getItemCount() {
        return trecords.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView fromacc,toacc,amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fromacc=itemView.findViewById(R.id.fromacc);
            toacc=itemView.findViewById(R.id.toacc);
            amount=itemView.findViewById(R.id.amount);
        }
    }
}
