package com.anis.android.medref.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anis.android.medref.SettingsActivity;
import com.anis.android.medref.custom.NoteEditorActivity;
import com.anis.android.medref.predefined.pregnancy.PregnancyActivity;
import com.anis.android.medref.R;
import com.anis.android.medref.predefined.RabiesActivity;
import com.anis.android.medref.predefined.textimagemodel.ModelActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    RecyclerView recyclerView;
    ReferenceAdapter adapter;
    List<ReferenceItem> referenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        referenceList = new ArrayList<>();
        adapter = new ReferenceAdapter(this, referenceList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            final EditText input = new EditText(this);
            input.setHint("Enter note title");

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("New Note")
                    .setView(input)
                    .setPositiveButton("OK", (dialog, which) -> {
                        String noteTitle = input.getText().toString().trim();
                        if (noteTitle.isEmpty()) noteTitle = "Untitled";

                        // Pass title to NoteEditorActivity
                        Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);
                        intent.putExtra("newNoteTitle", noteTitle);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                    .show();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        referenceList.clear();

        // Predefined references
        referenceList.add(new ReferenceItem("ANC", PregnancyActivity.class, R.drawable.fetus));
        referenceList.add(new ReferenceItem("Rabies", RabiesActivity.class, R.drawable.dog_svgrepo_com));
        referenceList.add(new ReferenceItem(
                "N Jaundice",
                ModelActivity.class,
                R.drawable.baby_svgrepo_com,
                true,
                R.drawable.jaundice,
                R.raw.jaundice,
                "Neonatal Jaundice"
        ));
        referenceList.add(new ReferenceItem(
                "GCS",
                ModelActivity.class,
                R.drawable.accident_svgrepo_com,
                true,
                R.drawable.gcs,
                R.raw.gcs,
                "Glasgow Coma Scale"
        ));

        referenceList.add(new ReferenceItem(
                "Snakebite",
                ModelActivity.class,
                R.drawable.snake_4_svgrepo_com,
                true,
                -1,
                R.raw.asv_doses,
                "Snake Bite"
        ));
        referenceList.add(new ReferenceItem(
                "Milestones",
                ModelActivity.class,
                R.drawable.baby_crawling,
                true,
                -1,
                R.raw.milestones,
                "Baby milestones"
        ));
        referenceList.add(new ReferenceItem("NIS",ModelActivity.class,R.drawable.vaccine,true,R.drawable.aefi,R.raw.nis,"NIS"));
//        referenceList.add(new ReferenceItem("APGAR",ModelActivity.class,R.drawable.newborn,true,R.drawable.apgar,R.raw.apgar,"APGAR Score"));


        // Load user notes
        SharedPreferences prefs = getSharedPreferences("user_notes", MODE_PRIVATE);
        int count = prefs.getInt("note_count", 0);
        for (int i = 0; i < count; i++) {
            String title = prefs.getString("title_" + i, null);
            if (title != null) {
                referenceList.add(new ReferenceItem(title, NoteEditorActivity.class, R.drawable.note, i));
            }
        }


        referenceList.add(new ReferenceItem("Settings", SettingsActivity.class, R.drawable.settings));

        adapter.notifyDataSetChanged();
    }

}
