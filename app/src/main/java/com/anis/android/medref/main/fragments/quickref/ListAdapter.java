package com.anis.android.medref.main.fragments.quickref;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.anis.android.medref.R;
import com.anis.android.medref.predefined.pregnancy.PregnancyActivity;
import com.anis.android.medref.predefined.rabies.RabiesActivity;
import com.anis.android.medref.predefined.model.ModelActivity;
import com.anis.android.medref.user.NoteEditorActivity;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private Context context;
    private List<ListItem> itemList;

    public ListAdapter(Context context, List<ListItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quick_topics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem item = itemList.get(position);
        holder.label.setText(item.getLabel());
        holder.description.setText(item.getDescription());
        holder.icon.setImageResource(item.getIconResId());

        holder.itemView.setOnClickListener(v -> {
            if (item.isPredef()) {
                // Launch predefined activity based on the item type
                if ("ANC".equals(item.getKey())) {
                    context.startActivity(new Intent(context, PregnancyActivity.class));
                } else if ("RAB".equals(item.getKey())) {
                    context.startActivity(new Intent(context, RabiesActivity.class));
                }
            } else if (item.isModel()) {
                String key = item.getKey();
                Intent intent = new Intent(context, ModelActivity.class);
                intent.putExtra("modelKey", key);
                context.startActivity(intent);
            } else if (item.isUser()) {
                // Handle user-created notes
                int noteId = Integer.parseInt(item.getKey());
                Intent intent = new Intent(context, NoteEditorActivity.class);
                intent.putExtra("noteId", noteId);  // Pass noteId for editing
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (item.isUser()) {
                showDeleteDialog(item, position);
                return true; // Indicates the event is consumed
            }
            return false;
        });
    }

    private void showDeleteDialog(ListItem item, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    int noteId = Integer.parseInt(item.getKey());
                    deleteNoteFromSharedPreferences(noteId);  // Delete the note from SharedPreferences
                    itemList.remove(position);  // Remove from the list
                    notifyItemRemoved(position);  // Notify the adapter

                    // Handle the case when no notes are left
                    if (itemList.isEmpty()) {
                        // Optionally, you can add a placeholder or empty state
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteNoteFromSharedPreferences(int noteId) {
        SharedPreferences prefs = context.getSharedPreferences("user_notes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Remove the note's title and body
        editor.remove("title_" + noteId);
        editor.remove("body_" + noteId);

        // Adjust the note_count and remove the note from SharedPreferences
        int count = prefs.getInt("note_count", 0);
        if (count > 0) {
            editor.putInt("note_count", count - 1);
        }

        editor.apply();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView label;
        MaterialTextView description;
        AppCompatImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.reference_label);
            icon = itemView.findViewById(R.id.icon);
            description = itemView.findViewById(R.id.reference_description);
        }
    }
}
