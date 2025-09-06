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
import java.util.Objects;
import java.util.Scanner;

import io.noties.markwon.Markwon;

public class ModelActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private PhotoView imageView;
    private   MaterialTextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.model_image);
        textView = findViewById(R.id.model_text);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        String key = getIntent().getStringExtra("modelKey");
        assert key != null;
        if(!key.isEmpty()){
            if(key.equals("NIS")){
                setActivityTitle("NIS");
                setImage(R.drawable.nis);
                setTextData(R.raw.nis);
            }
            else if (key.equals("NHB")){
                setActivityTitle("Neonatal Jaundice");
                setImage(R.drawable.jaundice);
                setTextData(R.raw.jaundice);
            } else if (key.equals("GCS")){
                setActivityTitle("Glasgow Coma Scale");
                setImage(R.drawable.gcs);
                setTextData(R.raw.gcs);
            } else if (key.equals("SNB")){
                setActivityTitle("Snake Bite");
                imageView.setVisibility(View.GONE);
                setTextData(R.raw.asv_doses);

            } else if (key.equals("DVM")){
                setActivityTitle("Developmental Milestones");
//                setImage(R.drawable.snake_4_svgrepo_com);
                imageView.setVisibility(View.GONE);
                setTextData(R.raw.milestones);
            }
            else if (key.equals("DIP")){
                setActivityTitle("Drugs in Pregnancy");
                setImage(R.drawable.pregnancy_safe);
                setTextData(R.raw.drugs_pregnancy);
            }
        }
    }

    private void setTextData(int resId) {
        String textToSet = loadRawResource(resId);
        Markwon markwon = Markwon.create(this);
        markwon.setMarkdown(textView,textToSet);

    }

    private void setImage(int resId){
        imageView.setImageResource(resId);
    }


    private void setActivityTitle(String title){
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
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
