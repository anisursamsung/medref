package com.anis.android.medref.bookviewer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anis.android.medref.R;

import java.util.List;

public class HeadingAdapter extends RecyclerView.Adapter<HeadingAdapter.HeadingViewHolder> {

    public interface OnHeadingClickListener {
        void onClick(int position);
    }

    private final List<HeadingItem> headings;
    private final OnHeadingClickListener listener;

    public HeadingAdapter(List<HeadingItem> headings, OnHeadingClickListener listener) {
        this.headings = headings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HeadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new HeadingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HeadingViewHolder holder, int position) {
        holder.textView.setText(headings.get(position).title);
        holder.itemView.setOnClickListener(v -> listener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return headings.size();
    }

    static class HeadingViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        HeadingViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
