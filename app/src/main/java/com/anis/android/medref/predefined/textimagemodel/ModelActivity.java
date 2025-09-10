package com.anis.android.medref.predefined.textimagemodel;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout imageContainer;

    private   MaterialTextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        toolbar = findViewById(R.id.toolbar);
        imageContainer = findViewById(R.id.image_container);

        textView = findViewById(R.id.model_text);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        String key = getIntent().getStringExtra("modelKey");
        assert key != null;
        if(!key.isEmpty()){
            if(key.equals("NIS")){
                setActivityTitle("NIS");
                addImages(R.drawable.nis,R.drawable.aefi);
                setTextData(R.raw.nis);
            }
            else if (key.equals("NHB")){
                setActivityTitle("Neonatal Jaundice");
                addImage(R.drawable.jaundice);
                setTextData(R.raw.jaundice);
            } else if (key.equals("GCS")){
                setActivityTitle("Glasgow Coma Scale");
                addImage(R.drawable.gcs);
                setTextData(R.raw.gcs);
            } else if (key.equals("SNB")){
                setActivityTitle("Snake Bite");
                imageContainer.setVisibility(View.GONE);
                setTextData(R.raw.asv_doses);

            } else if (key.equals("DVM")){
                setActivityTitle("Developmental Milestones");
//                setImage(R.drawable.snake_4_svgrepo_com);
                imageContainer.setVisibility(View.GONE);
                setTextData(R.raw.milestones);
            }
            else if (key.equals("DIP")){
                setActivityTitle("Drugs in Pregnancy");
                addImage(R.drawable.pregnancy_safe);
                setTextData(R.raw.drugs_pregnancy);
            }
        }
    }

    private void setTextData(int resId) {
        String textToSet = loadRawResource(resId);
        Markwon markwon = Markwon.create(this);
        markwon.setMarkdown(textView,textToSet);

    }
    private void addImage(int resId) {
        PhotoView photoView = new PhotoView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 8); // spacing between images
        photoView.setLayoutParams(params);
        photoView.setAdjustViewBounds(true);
        photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photoView.setImageResource(resId);
        imageContainer.addView(photoView);
    }

    private void addImages(int... resIds) {
        imageContainer.removeAllViews(); // clear previous
        for (int resId : resIds) {
            addImage(resId);
        }
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
