package com.anis.android.medref.main.fragments.tools;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.anis.android.medref.R;
import com.anis.android.medref.predefined.rabies.RabiesActivity;
import com.anis.android.medref.predefined.pregnancy.PregnancyActivity;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ToolViewHolder> {

    private List<ToolItem> toolItems;
    private Context context;

    public ToolsAdapter(Context context, List<ToolItem> toolItems) {
        this.context = context;
        this.toolItems = toolItems;
    }

    @NonNull
    @Override
    public ToolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quick_tools_item, parent, false);
        return new ToolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToolViewHolder holder, int position) {
        ToolItem item = toolItems.get(position);
        holder.toolIcon.setImageResource(item.iconResId);
        holder.toolLabel.setText(item.label);
        holder.itemView.setOnClickListener(v -> {
            if ("ANC".equals(item.getId())) {
                context.startActivity(new Intent(context, PregnancyActivity.class));
            } else if ("RAB".equals(item.getId())) {
                context.startActivity(new Intent(context, RabiesActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return toolItems.size();
    }

    static class ToolViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView toolIcon;
        MaterialTextView toolLabel;

        public ToolViewHolder(@NonNull View itemView) {
            super(itemView);
            toolIcon = itemView.findViewById(R.id.toolIcon);
            toolLabel = itemView.findViewById(R.id.toolLabel);
        }
    }
}
