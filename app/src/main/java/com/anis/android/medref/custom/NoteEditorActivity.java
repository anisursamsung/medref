package com.anis.android.medref.custom;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.anis.android.medref.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.textfield.TextInputEditText;

public class NoteEditorActivity extends AppCompatActivity {

    TextInputEditText bodyEditText;
    MaterialToolbar toolbar;
    SharedPreferences prefs;
    int noteId = -1;
    String noteTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        bodyEditText = findViewById(R.id.note_body);

        prefs = getSharedPreferences("user_notes", MODE_PRIVATE);

        noteId = getIntent().getIntExtra("noteId", -1);
        if (noteId != -1) {
            // Existing note
            noteTitle = prefs.getString("title_" + noteId, "");
            String body = prefs.getString("body_" + noteId, "");
            toolbar.setTitle(noteTitle);
            bodyEditText.setText(body);
//            bodyEditText.setFocusable(false);
//            bodyEditText.setFocusableInTouchMode(false);
//            bodyEditText.setCursorVisible(false);
//            bodyEditText.setKeyListener(null);
        } else {
            // New note (title passed from MainActivity)
            noteTitle = getIntent().getStringExtra("newNoteTitle");
            toolbar.setTitle(noteTitle);
        }
//        bodyEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bodyEditText.setFocusable(true);
//                bodyEditText.setFocusableInTouchMode(true);
//                bodyEditText.setCursorVisible(true);
//                bodyEditText.setKeyListener(new EditText(NoteEditorActivity.this).getKeyListener());
//            }
//        });

    }



    @Override
    protected void onPause() {
        super.onPause();
        saveNote(); // Auto-save whenever activity goes to background
    }

    private void saveNote() {
        SharedPreferences.Editor editor = prefs.edit();

        if (noteId == -1) {
            int count = prefs.getInt("note_count", 0);
            noteId = count;
            prefs.edit().putInt("note_count", count + 1).apply();
        }

        editor.putString("title_" + noteId, noteTitle);
        editor.putString("body_" + noteId, bodyEditText.getText().toString());
        editor.apply();
    }

    private void showChangeTitleDialog() {
        final EditText input = new EditText(this);
        input.setText(noteTitle);
        input.setSelection(noteTitle.length());

        new AlertDialog.Builder(this)
                .setTitle("Change Note Title")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newTitle = input.getText().toString().trim();
                    if (!newTitle.isEmpty()) {
                        noteTitle = newTitle;
                        toolbar.setTitle(noteTitle);
                        saveNote(); // immediately update title
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            saveNote();
            return true;
        } else if (id == R.id.action_change_title) {
            showChangeTitleDialog();
            return true;
        } else if (id == R.id.action_copy) {
            copyToClipboard();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void copyToClipboard() {
        String text =bodyEditText.getText().toString().trim();

        if (!text.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No text to copy", Toast.LENGTH_SHORT).show();
        }
    }

}
