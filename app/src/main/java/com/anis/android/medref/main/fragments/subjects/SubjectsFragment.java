package com.anis.android.medref.main.fragments.subjects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anis.android.medref.R;
import com.anis.android.medref.main.SearchableFragment;

import java.util.ArrayList;
import java.util.List;

public class SubjectsFragment extends Fragment implements SearchableFragment {

    private View view;
    private RecyclerView recyclerView;
    private SubjectAdapter adapter;
    private List<SubjectItem> subjectList;
    private List<SubjectItem> filteredList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_subjects, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_subjects);
        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        subjectList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new SubjectAdapter(requireContext(), filteredList);
        recyclerView.setAdapter(adapter);

        loadSubjects();
    }

    private void loadSubjects() {
        subjectList.clear();
        filteredList.clear();

        // Sample predefined subjects (label + icon)
        subjectList.add(new SubjectItem("Sample", R.drawable.ic_books, "mdfiles/sample.md"));
        subjectList.add(new SubjectItem("Paediatrics", R.drawable.ic_books, "mdfiles/paediatrics.md"));
        filteredList.addAll(subjectList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchQueryChanged(String query) {
        filterList(query);
    }

    private void filterList(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(subjectList);
        } else {
            for (SubjectItem item : subjectList) {
                if (item.getLabel().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
