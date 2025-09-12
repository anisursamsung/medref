package com.anis.android.medref.main.fragments.tools;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anis.android.medref.R;

import java.util.ArrayList;
import java.util.List;

public class ToolsFragment extends Fragment {
    private View view;
    private RecyclerView toolsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tools, container, false);

        findViews();
        setupToolsRecyclerView();
        return view;
    }

    private void setupToolsRecyclerView() {
        toolsRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        List<ToolItem> toolList = new ArrayList<>();
        toolList.add(new ToolItem(R.drawable.pregnant_woman_svgrepo_com, "ANC", "ANC"));
        toolList.add(new ToolItem(R.drawable.dog_svgrepo_com, "Dog bite", "RAB"));
        // Add more items...

        ToolsAdapter adapter = new ToolsAdapter(requireContext(), toolList);
        toolsRecyclerView.setAdapter(adapter);
    }




    private void findViews() {
        toolsRecyclerView = view.findViewById(R.id.recycler_view_tools);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }
    private void loadItems() {
    //
    }







}