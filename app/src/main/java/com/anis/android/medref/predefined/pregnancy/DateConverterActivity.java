package com.anis.android.medref.predefined.pregnancy;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.anis.android.medref.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.textview.MaterialTextView;

import java.util.Calendar;

public class DateConverterActivity extends AppCompatActivity {

    private MaterialTextView textViewResult;
    private MaterialButton  buttonConvert;
    private NumberPicker dayPicker, monthPicker, yearPicker;
    private AppCompatSpinner conversionTypeSpinner;

    private final String[] conversionTypes = {
            "Bangla to Gregorian",
            "Gregorian to Bangla"
    };

    private boolean isBanglaToGregorian = true;
    private final GregorianToBanglaAndGregorianConverter converter = new GregorianToBanglaAndGregorianConverter();
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_converter);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Bind Views
        conversionTypeSpinner = findViewById(R.id.conversionTypeSpinner);
        dayPicker = findViewById(R.id.numberPickerDay);
        monthPicker = findViewById(R.id.numberPickerMonth);
        yearPicker = findViewById(R.id.numberPickerYear);
        buttonConvert = findViewById(R.id.buttonConvertToEnglish);
        textViewResult = findViewById(R.id.textViewEnglishDate);

        // Setup Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, conversionTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conversionTypeSpinner.setAdapter(adapter);

        conversionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isBanglaToGregorian = (position == 0);
                updatePickersBasedOnMode();
                textViewResult.setText(""); // clear previous result
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Default picker ranges
        updatePickersBasedOnMode();

        buttonConvert.setOnClickListener(v -> {
            int day = dayPicker.getValue();
            int month = monthPicker.getValue();
            int year = yearPicker.getValue();

            if (isBanglaToGregorian) {
                String result = banglaToGregorian(day, month, year);
                textViewResult.setText(result);
            } else {
                String result = converter.convertToBangla(day, month, year);
                textViewResult.setText(result);
            }
        });
    }

    private void updatePickersBasedOnMode() {
        if (isBanglaToGregorian) {
            dayPicker.setMinValue(1);
            dayPicker.setMaxValue(31);
            monthPicker.setMinValue(1);
            monthPicker.setMaxValue(12);
            yearPicker.setMinValue(1400);
            yearPicker.setMaxValue(1500);
            yearPicker.setValue(1432);
        } else {
            dayPicker.setMinValue(1);
            dayPicker.setMaxValue(31);
            monthPicker.setMinValue(1);
            monthPicker.setMaxValue(12);
            yearPicker.setMinValue(1950);
            yearPicker.setMaxValue(2100);
            yearPicker.setValue(Calendar.getInstance().get(Calendar.YEAR));
        }
    }

    private String banglaToGregorian(int banglaDay, int banglaMonth, int banglaYear) {
        int[] banglaMonthDays = {31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 29, 30};

        int gregorianYear = banglaYear + 593;

        if (isLeapYear(gregorianYear)) {
            banglaMonthDays[10] = 30; // Falgun adjustment
        }

        int daysPassed = banglaDay;
        for (int i = 0; i < banglaMonth - 1; i++) {
            daysPassed += banglaMonthDays[i];
        }

        int gregorianDay = daysPassed + 13; // Offset from 14 April
        int gregorianMonth = 4; // Start from April
        int[] gregorianMonthDays = {31,28,31,30,31,30,31,31,30,31,30,31};

        if (isLeapYear(gregorianYear)) {
            gregorianMonthDays[1] = 29;
        }

        while (gregorianDay > gregorianMonthDays[gregorianMonth - 1]) {
            gregorianDay -= gregorianMonthDays[gregorianMonth - 1];
            gregorianMonth++;
            if (gregorianMonth > 12) {
                gregorianMonth = 1;
                gregorianYear++;
            }
        }

        return gregorianDay + "/" + gregorianMonth + "/" + gregorianYear;
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
