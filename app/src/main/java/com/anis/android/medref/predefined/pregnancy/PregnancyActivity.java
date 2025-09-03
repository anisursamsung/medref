package com.anis.android.medref.predefined.pregnancy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anis.android.medref.R;
import com.anis.android.medref.predefined.textimagemodel.ModelActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.noties.markwon.Markwon;

public class PregnancyActivity extends AppCompatActivity {

    DatePicker datePicker; // Assuming a pregnancy of 280 days
    MaterialTextView outputTv;
    MaterialButton calculateButton;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnancy);
        datePicker = findViewById(R.id.date_picker);
        calculateButton = findViewById(R.id.calculate_button);
        outputTv = findViewById(R.id.showOutput);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDates();
            }
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureButtons();

    }



    private void calculateDates() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date lastMenstrualDate = calendar.getTime();

        // LMP string
        String lmpString = sdf.format(lastMenstrualDate);

        Calendar currentDate = Calendar.getInstance();
        long gestationalAgeInMillis = currentDate.getTimeInMillis() - lastMenstrualDate.getTime();
        int gestationalAgeInWeeks = (int) (gestationalAgeInMillis / (1000 * 60 * 60 * 24 * 7));
        int gestationalAgeInDays = (int) ((gestationalAgeInMillis % (1000 * 60 * 60 * 24 * 7)) / (1000 * 60 * 60 * 24));

        // Gestational Age string
        String gaString = gestationalAgeInWeeks + " weeks " + gestationalAgeInDays + " days";

        // Estimated Due Date
        Calendar eddCalendar = Calendar.getInstance();
        eddCalendar.setTime(lastMenstrualDate);
        eddCalendar.add(Calendar.DAY_OF_YEAR, 280);
        Date eddDate = eddCalendar.getTime();
        String eddString = sdf.format(eddDate);

        // Trimester calculation
        String trimester;
        if (gestationalAgeInWeeks < 14) {
            trimester = "1st Trimester";
        } else if (gestationalAgeInWeeks < 28) {
            trimester = "2nd Trimester";
        } else {
            trimester = "3rd Trimester";
        }

        // Milestone calculations
        String quickening = addWeeks(lastMenstrualDate, 18, sdf) + " - " + addWeeks(lastMenstrualDate, 22, sdf);
        String anomalyScan = addWeeks(lastMenstrualDate, 18, sdf) + " - " + addWeeks(lastMenstrualDate, 22, sdf);
        String viability = addWeeks(lastMenstrualDate, 24, sdf);
        String term = addWeeks(lastMenstrualDate, 37, sdf) + " - " + addWeeks(lastMenstrualDate, 42, sdf);

        // Risk alerts
        String risk = "";
        if (gestationalAgeInWeeks < 6) {
            risk = "⚠️ Early pregnancy – confirm viability with ultrasound.";
        } else if (gestationalAgeInWeeks < 37) {
            risk = "Normal – ongoing pregnancy.";
        } else if (gestationalAgeInWeeks >= 37 && gestationalAgeInWeeks <= 42) {
            risk = "✅ Term pregnancy.";
        } else {
            risk = "⚠️ Post-term pregnancy – consider induction.";
        }

        // Final result string
        String resultString =
                "### Pregnancy Report\n\n" +
                        "**LMP:** " + lmpString + "\n\n" +
                        "**Gestational Age:** " + gaString + "\n\n" +
                        "**Trimester:** " + trimester + "\n\n" +
                        "**Estimated Due Date:** " + eddString + "\n\n" +
                        "### Key Milestones\n" +
                        "- **Quickening:** " + quickening + "\n" +
                        "- **Anomaly Scan:** " + anomalyScan + "\n" +
                        "- **Viability:** " + viability + "\n" +
                        "- **Term:** " + term + "\n\n" +
                        "### Clinical Note\n" +
                        risk;

        Markwon markwon = Markwon.create(this);
        markwon.setMarkdown(outputTv, resultString);

    }

    private String addWeeks(Date date, int weeks, SimpleDateFormat sdf) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.WEEK_OF_YEAR, weeks);
        return sdf.format(c.getTime());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void configureButtons() {
        MaterialButton findLmp,fundalH,safeDrug,concernDrug,calendrConv;
        findLmp = findViewById(R.id.find_lmp_button);
        fundalH = findViewById(R.id.fundal_height_button);
        safeDrug = findViewById(R.id.safe_drugs_button);
        concernDrug = findViewById(R.id.contraindicated_drugs_button);
        calendrConv = findViewById(R.id.bengali_english_calendar_button);

        findLmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PregnancyActivity.this, LMPFinderActivity.class);
                startActivity(intent);
            }
        });

        fundalH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogOfFundalHeight();
            }
        });
        safeDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchModelActivity(R.raw.safe_drus_pregnancy,-1, "Pregnancy Safe Drugs");
            }
        });
         concernDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchModelActivity(R.raw.drugs_of_concern,-1, "Pregnancy Safe Drugs");
            }
        });
        calendrConv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PregnancyActivity.this, DateConverterActivity.class);
                startActivity(intent);
            }
        });

    }
    private void launchModelActivity(int mdFile, int image,String title) {
        Intent intent = new Intent(this, ModelActivity.class);
        intent.putExtra("imageResId",image );
        intent.putExtra("mdFileResId",mdFile);
        intent.putExtra("toolbarTitle",title);
        startActivity(intent);
    }


    private void showDialogOfFundalHeight() {
        // Inflate the custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_fundal_height, null);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Fundal Height Reference")
                .setView(dialogView)
                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                .show();
    }


}
