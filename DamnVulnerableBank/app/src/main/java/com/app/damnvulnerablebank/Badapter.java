package com.app.damnvulnerablebank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Badapter extends RecyclerView.Adapter<Badapter.ViewHolder> {

    LayoutInflater inflater;
    List<Brecords> brecords;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener =listener;
    }

    public Badapter(Context ctx,List<Brecords> brecords){
        this.inflater=LayoutInflater.from(ctx);
        this.brecords=brecords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.custom_benif,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.benificiaryaccnt.setText(brecords.get(position).getBenificiaryaccnt());
        holder.isapproved.setText(brecords.get(position).getIsapproved());


    }

    @Override
    public int getItemCount() {
        return brecords.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView benificiaryaccnt,isapproved;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            benificiaryaccnt=itemView.findViewById(R.id.benif);
            isapproved=itemView.findViewById(R.id.isapproved);

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
