package com.anis.android.medref;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.color.DynamicColors;

public class MedRefApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Load saved theme mode (Light / Dark / Auto)
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int mode = prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(mode);

        // Apply dynamic colors if available (Android 12+)
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
