package com.anis.android.medref;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "settings";
    private static final String KEY_THEME_MODE = "theme_mode";

    private MaterialTextView tvThemeSummary;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        MaterialCardView itemTheme = findViewById(R.id.itemTheme);
        tvThemeSummary = findViewById(R.id.tvThemeSummary);

        // Load current theme choice
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedMode = prefs.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        updateThemeSummary(savedMode);

        itemTheme.setOnClickListener(v -> showThemeDialog(prefs));


    }

    private void showThemeDialog(SharedPreferences prefs) {
        String[] options;
        int[] modes;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            options = new String[]{"Light", "Dark", "Follow System"};
            modes = new int[]{
                    AppCompatDelegate.MODE_NIGHT_NO,
                    AppCompatDelegate.MODE_NIGHT_YES,
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            };
        } else {
            options = new String[]{"Light", "Dark"};
            modes = new int[]{
                    AppCompatDelegate.MODE_NIGHT_NO,
                    AppCompatDelegate.MODE_NIGHT_YES
            };
        }

        int savedMode = prefs.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        int checkedItem = 0;
        for (int i = 0; i < modes.length; i++) {
            if (modes[i] == savedMode) {
                checkedItem = i;
                break;
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Choose Theme")
                .setSingleChoiceItems(options, checkedItem, (dialog, which) -> {
                    int mode = modes[which];
                    prefs.edit().putInt(KEY_THEME_MODE, mode).apply();
                    AppCompatDelegate.setDefaultNightMode(mode);
                    updateThemeSummary(mode);
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateThemeSummary(int mode) {
        switch (mode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                tvThemeSummary.setText("Light");
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                tvThemeSummary.setText("Dark");
                break;
            default:
                tvThemeSummary.setText("Follow System");
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
