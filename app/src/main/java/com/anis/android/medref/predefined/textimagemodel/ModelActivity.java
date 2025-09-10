package com.anis.android.medref.predefined.textimagemodel;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anis.android.medref.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;

import io.noties.markwon.Markwon;
import io.noties.markwon.image.picasso.PicassoImagesPlugin;

public class ModelActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;

    private   MaterialTextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        toolbar = findViewById(R.id.toolbar);

        textView = findViewById(R.id.model_text);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        String key = getIntent().getStringExtra("modelKey");
        assert key != null;
        if(!key.isEmpty()){
            if(key.equals("NIS")){
                setActivityTitle("NIS");
                setTextData("quickrefs/files/nis.md");
            }
            else if (key.equals("NHB")){
                setActivityTitle("Neonatal Jaundice");
                setTextData("quickrefs/files/jaundice.md");
            }
            else if (key.equals("GCS")){
                setActivityTitle("Glasgow Coma Scale");
                setTextData("quickrefs/files/gcs.md");
            }
            else if (key.equals("SNB")){
                setActivityTitle("Snake Bite");
                setTextData("quickrefs/files/asv_doses.md");

            }
            else if (key.equals("DVM")){
                setActivityTitle("Developmental Milestones");
                setTextData("quickrefs/files/milestones.md");
            }
            else if (key.equals("DIP")){
                setActivityTitle("Drugs in Pregnancy");
                setTextData("quickrefs/files/drugs_pregnancy.md");
            }
        }
    }


    private void setTextData(String filename) {


        String textToSet = loadMarkdownFromAssets(filename);
        Markwon markwon = Markwon.builder(this)
                .usePlugin(PicassoImagesPlugin.create(this))
                .build();
        markwon.setMarkdown(textView, textToSet);
    }
//        String textToSet = loadRawResource(resId);
//
//        // Create Markwon instance with Picasso plugin
//        Markwon markwon = Markwon.builder(this)
//                .usePlugin(PicassoImagesPlugin.create(this))
//                .build();
//
//        markwon.setMarkdown(textView, textToSet);
//    }

//    private void setTextData(int resId) {
//        String textToSet = loadRawResource(resId);
//
//        // Create Markwon instance with Picasso plugin
//        Markwon markwon = Markwon.builder(this)
//                .usePlugin(PicassoImagesPlugin.create(this))
//                .build();
//
//        markwon.setMarkdown(textView, textToSet);
//    }
    private String loadMarkdownFromAssets(String filename) {
        try {
            InputStream inputStream = getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            reader.close();
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error loading markdown file: " + filename;
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
