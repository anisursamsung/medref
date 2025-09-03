package com.anis.android.medref.predefined;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.anis.android.medref.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.textview.MaterialTextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.noties.markwon.Markwon;

public class RabiesActivity extends AppCompatActivity {

    AppCompatSpinner vaccinationTypeSpinner;
    DatePicker initialDoseDatePicker;
    MaterialTextView outputTv;
    MaterialToolbar toolbar;
    MaterialButton fastAidBtn,antibdyBtn,otehrMedBtn,vaccineButton;
    MaterialButton generateScheduleButton;
    private  Markwon markwon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rabies);
        markwon = Markwon.create(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        outputTv = findViewById(R.id.showOutput);
        vaccinationTypeSpinner = findViewById(R.id.vaccination_type_spinner);
        initialDoseDatePicker = findViewById(R.id.initial_dose_date_picker);
        generateScheduleButton = findViewById(R.id.generate_schedule_button);
        fastAidBtn = findViewById(R.id.fast_aid_button);
        antibdyBtn = findViewById(R.id.antibodies_button);
        otehrMedBtn = findViewById(R.id.other_medicine_button);
        vaccineButton = findViewById(R.id.vaccine_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.vaccination_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccinationTypeSpinner.setAdapter(adapter);

        generateScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateSchedule();
            }
        });

        fastAidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markwon.setMarkdown(outputTv, getString(R.string.fast_aid_rabies));
            }
        });
        vaccineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markwon.setMarkdown(outputTv, getString(R.string.vaccine_rabies));

            }
        });
        antibdyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markwon.setMarkdown(outputTv, getString(R.string.antibody_rabies));
            }
        });
        otehrMedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markwon.setMarkdown(outputTv, getString(R.string.other_med_rabies));
            }
        });

    }

    private void showInformation(String title, String content) {
        String mdText = "### " + title + "\n\n" + content;
        markwon.setMarkdown(outputTv, mdText);
    }


    private void generateSchedule() {
        String vaccinationType = vaccinationTypeSpinner.getSelectedItem().toString();
        int year = initialDoseDatePicker.getYear();
        int month = initialDoseDatePicker.getMonth();
        int day = initialDoseDatePicker.getDayOfMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date initialDoseDate = calendar.getTime();

        String schedule = "";
        String header = "";
        if (vaccinationType.equalsIgnoreCase("intramuscular")) {
            schedule = generateScheduleText(initialDoseDate, new int[]{0, 3, 7, 14, 28});
            header = "Intramuscular schedule (Day 0, 3, 7, 14 & 28)";
        } else if (vaccinationType.equalsIgnoreCase("intradermal")) {
            schedule = generateScheduleText(initialDoseDate, new int[]{0, 3, 7, 28});
            header = "Intradermal schedule (Day 0, 3, 7 & 28)";
        }


        showInformation(header, schedule);
    }

    private String generateScheduleText(Date initialDate, int[] daysArray) {
        StringBuilder scheduleText = new StringBuilder();
        scheduleText.append("### Vaccination Schedule\n\n");

        for (int i = 0; i < daysArray.length; i++) {
            Calendar doseCalendar = Calendar.getInstance();
            doseCalendar.setTime(initialDate);
            doseCalendar.add(Calendar.DAY_OF_YEAR, daysArray[i]);
            Date doseDate = doseCalendar.getTime();

            scheduleText.append("- **Dose ").append(i + 1).append(":** ")
                    .append(formatDate(doseDate))
                    .append("\n");
        }

        return scheduleText.toString();
    }


    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(date);
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
