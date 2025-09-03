package com.anis.android.medref.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.anis.android.medref.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ReferenceAdapter extends RecyclerView.Adapter<ReferenceAdapter.ViewHolder> {

    private Context context;
    private List<ReferenceItem> itemList;

    public ReferenceAdapter(Context context, List<ReferenceItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ReferenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reference, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferenceAdapter.ViewHolder holder, int position) {
        ReferenceItem item = itemList.get(position);
        holder.buttonItem.setText(item.getTitle());
        holder.buttonItem.setIcon(AppCompatResources.getDrawable(context, item.getIconResId()));



        holder.buttonItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, item.getTargetActivity());
            if (item.getNoteId() != -1) {
                intent.putExtra("noteId", item.getNoteId());
            }
            if (item.isModel()) { // here it's actually the key
                intent.putExtra("imageResId",item.getImageResId());
                intent.putExtra("mdFileResId",item.getMdFileResId());
                intent.putExtra("toolbarTitle",item.getToolbarTitle());
            }
            context.startActivity(intent);
        });


        holder.buttonItem.setOnLongClickListener(v -> {
            if (item.getNoteId() != -1) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            deleteNote(item.getNoteId());
                            itemList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, itemList.size());
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
            return false;
        });
    }

    private void deleteNote(int noteId) {
        SharedPreferences prefs = context.getSharedPreferences("user_notes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("title_" + noteId);
        editor.remove("body_" + noteId);
        editor.apply();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
     MaterialButton buttonItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonItem = itemView.findViewById(R.id.button_item);

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
