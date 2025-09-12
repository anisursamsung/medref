package com.anis.android.medref.main.fragments.quickref;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.anis.android.medref.R;
import com.anis.android.medref.user.NoteEditorActivity;
import com.anis.android.medref.main.SearchableFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class QuickTopicsFragment extends Fragment implements SearchableFragment {
    private View view;
    private RecyclerView listRecyclerView;
    private ListAdapter adapter;
    private List<ListItem> list;
    private List<ListItem> filteredList;

    private FloatingActionButton fab;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quick_topics, container, false);

        findViews();
        setupListRecyclerView();

        return view;
    }



    @Override
    public void onSearchQueryChanged(String query) {
        filterList(query);
    }

    private void setupListRecyclerView() {
        listRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        list = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new ListAdapter(requireContext(), filteredList);
        listRecyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            final EditText input = new EditText(requireContext());
            input.setHint("Enter note title");

            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("New Note")
                    .setView(input)
                    .setPositiveButton("OK", (dialog, which) -> {
                        String noteTitle = input.getText().toString().trim();
                        if (noteTitle.isEmpty()) noteTitle = "Untitled";

                        // Pass title to NoteEditorActivity
                        Intent intent = new Intent(requireContext(), NoteEditorActivity.class);
                        intent.putExtra("newNoteTitle", noteTitle);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .show(); // Correct placement
        });


    }

    private void findViews() {
        listRecyclerView =  view.findViewById(R.id.recycler_view_quick_ref);
        fab = view.findViewById(R.id.add_new);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }
    private void loadItems() {
        list.clear();
        filteredList.clear();
        list.add(new ListItem("Drugs in Pregnancy", "Safe vs Drugs of Concern", false, true, false, "DIP", R.drawable.ic_medicine));
        list.add(new ListItem("NIS", "National Immunization Schedule..", false, true, false, "NIS", R.drawable.vaccine));
        list.add(new ListItem("Neonatal Hyperbilirubinemia", "Criteria for phototherapy..", false, true, false, "NHB", R.drawable.baby_svgrepo_com));
        list.add(new ListItem("GCS", "Glasgow Coma Scale", false, true, false, "GCS", R.drawable.accident_svgrepo_com));
        list.add(new ListItem("Snake Bite", "Comprehensive management", false, true, false, "SNB", R.drawable.snake_4_svgrepo_com));
        list.add(new ListItem("Milestones", "Developmental milestones..", false, true, false, "DVM", R.drawable.baby_crawling));

        // Add user-created notes
        loadUserNotes();
        filteredList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void loadUserNotes() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_notes",Context.MODE_PRIVATE);
        int count = prefs.getInt("note_count", 0);
        if (count == 0) {
            return;
        }

        for (int i = 0; i < count; i++) {
            String title = prefs.getString("title_" + i, null);
            if (title != null) {
                String description = "User-created note"; // You can customize this if needed
                list.add(new ListItem(title, description, false, false, true, String.valueOf(i), R.drawable.note));
            }
        }
    }




    private void filterList(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(list);
        } else {
            for (ListItem item : list) {
                if (item.getLabel().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}