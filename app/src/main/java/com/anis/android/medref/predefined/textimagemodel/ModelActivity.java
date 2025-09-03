package com.anis.android.medref.predefined.textimagemodel;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anis.android.medref.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.textview.MaterialTextView;

import java.io.InputStream;
import java.util.Scanner;

import io.noties.markwon.Markwon;

public class ModelActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);

        toolbar = findViewById(R.id.toolbar);
        PhotoView imageView = findViewById(R.id.model_image);
        MaterialTextView textView = findViewById(R.id.model_text);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // Get key from intent
        int  imageResId = getIntent().getIntExtra("imageResId",-1);
        int mdFileResId  = getIntent().getIntExtra("mdFileResId",-1);
        if(imageResId==-1){
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageResource(imageResId);
        }
        if(mdFileResId==-1){
            textView.setVisibility(View.GONE);
        } else {
            String textToSet = loadRawResource(mdFileResId);
            Markwon markwon = Markwon.create(this);
            markwon.setMarkdown(textView,textToSet);
        }

        String toolbarTitle = getIntent().getStringExtra("toolbarTitle");
        getSupportActionBar().setTitle(toolbarTitle);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private String loadRawResource(int resId) {
        InputStream inputStream = getResources().openRawResource(resId);
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

}
