package com.anis.android.medref.user;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.anis.android.medref.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class NoteEditorActivity extends AppCompatActivity {

    TextInputEditText bodyEditText;
    MaterialTextView bodyTv;
    MaterialToolbar toolbar;
    RelativeLayout mainLayout;

    SharedPreferences prefs;
    int noteId = -1;
    String noteTitle = "";
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        mainLayout = findViewById(R.id.main_layout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        bodyEditText = findViewById(R.id.note_body);
        bodyTv = findViewById(R.id.note_body_tv);

        prefs = getSharedPreferences("user_notes", MODE_PRIVATE);

        noteId = getIntent().getIntExtra("noteId", -1);
        if (noteId != -1) {
            noteTitle = prefs.getString("title_" + noteId, "");
            String body = prefs.getString("body_" + noteId, "");
            getSupportActionBar().setTitle(noteTitle);

            bodyEditText.setText(body);
            bodyTv.setText(body);
            setViewMode();
        } else {
            noteTitle = getIntent().getStringExtra("newNoteTitle");
            getSupportActionBar().setTitle(noteTitle);
            toolbar.setTitle(noteTitle);
            isEditMode = true;
            enableEditMode();
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                saveNote();  // Save note before navigating back
                finish();    // Close the activity
            }
        });
    }




    private void saveNote() {
        String title = getSupportActionBar().getTitle().toString();
        String body = bodyEditText.getText().toString().trim();

        if (title.isEmpty() && body.isEmpty() && noteId == -1) {
            return;
        }

        if (title.isEmpty()) {
            Toast.makeText(this, "Note title cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        if (noteId == -1) {
            int count = prefs.getInt("note_count", 0);
            noteId = count;
            editor.putInt("note_count", count + 1);
        }

        noteTitle = title;
        editor.putString("title_" + noteId, noteTitle);
        editor.putString("body_" + noteId, body);
        editor.apply();
        toolbar.setTitle(noteTitle);
        Snackbar.make(mainLayout, "Note saved", Snackbar.LENGTH_SHORT).show();
    }

    private void setViewMode() {
        bodyEditText.setVisibility(MaterialTextView.GONE);
        bodyTv.setVisibility(MaterialTextView.VISIBLE);
    }

    private void enableEditMode() {
        bodyEditText.setVisibility(MaterialTextView.VISIBLE);
        bodyTv.setVisibility(MaterialTextView.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }

        if (id == R.id.action_copy) {
            copyNoteToClipboard();
            return true;
        }
        if (id == R.id.action_change_title) {
            showPopupToChangeTitle();
            return true;
        }
        if (id == R.id.action_edit) {
            enableEditMode();
            item.setVisible(false);
            return true;
        }
        if (id == R.id.action_save) {
            saveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopupToChangeTitle() {
        // Create a dialog with an EditText to change the title
        final EditText titleInput = new EditText(this);
        titleInput.setText(noteTitle);  // Pre-fill the EditText with the current title
        titleInput.setSelection(titleInput.getText().length());  // Move cursor to the end of the text

        // Show the dialog
        new MaterialAlertDialogBuilder(this)
                .setTitle("Change Title")
                .setView(titleInput)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newTitle = titleInput.getText().toString().trim();
                    if (!TextUtils.isEmpty(newTitle)) {
                        noteTitle = newTitle;
                        Objects.requireNonNull(getSupportActionBar()).setTitle(noteTitle);  // Update title in ActionBar
                    } else {
                        Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void copyNoteToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Note", bodyEditText.getText().toString());
        clipboard.setPrimaryClip(clip);
        Snackbar.make(mainLayout, "Note copied to clipboard", Snackbar.LENGTH_SHORT).show();
    }
}