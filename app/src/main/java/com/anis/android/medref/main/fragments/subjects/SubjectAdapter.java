package com.anis.android.medref.main.fragments.subjects;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.anis.android.medref.R;
import com.anis.android.medref.bookviewer.BookViewerActivity;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private final Context context;
    private final List<SubjectItem> subjectList;

    public SubjectAdapter(Context context, List<SubjectItem> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        SubjectItem item = subjectList.get(position);
        holder.label.setText(item.getLabel());
        holder.icon.setImageResource(item.getIconResId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookViewerActivity.class);
                intent.putExtra("fileName", item.getFileName());      // file should be in assets/
                intent.putExtra("title", item.getLabel());  // shown in toolbar
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        AppCompatImageView icon;

        SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.subject_label);
            icon = itemView.findViewById(R.id.subject_icon);
        }
    }
}
