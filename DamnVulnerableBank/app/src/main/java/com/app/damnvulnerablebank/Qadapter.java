package com.app.damnvulnerablebank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Qadapter extends RecyclerView.Adapter {
    LayoutInflater inflater;
    List<BoardListRecords> boardRecords;
    private OnItemClickListener mListener;

    public Qadapter(Context ctx, List<BoardListRecords> boardRecords) {
        this.inflater = LayoutInflater.from(ctx);
        this.boardRecords = boardRecords;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setOnItemClickListener(BoardView boardView) {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    public int getItemCount() {
        return boardRecords.size();
    }
}
