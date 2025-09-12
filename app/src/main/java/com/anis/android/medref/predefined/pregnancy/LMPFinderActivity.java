package com.anis.android.medref.predefined.pregnancy;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.anis.android.medref.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class LMPFinderActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lmpfinder);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextInputEditText etGestationalAge = findViewById(R.id.et_gestational_age);
        DatePicker datePicker = findViewById(R.id.date_picker);
        Button btnCalculate = findViewById(R.id.btn_calculate_lmp);
        MaterialTextView tvResult = findViewById(R.id.tv_lmp_result);

        // Handle button click
        btnCalculate.setOnClickListener(v -> {
            String gaInput = etGestationalAge.getText() != null ? etGestationalAge.getText().toString().trim() : "";
            if (gaInput.isEmpty()) {
                Toast.makeText(this, "Enter GA in weeks",Toast.LENGTH_SHORT).show();
                return;
            }

            int gaWeeks = Integer.parseInt(gaInput);

            // Get selected date
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            Calendar examDate = Calendar.getInstance();
            examDate.set(year, month, day);

            // Subtract GA in days
            examDate.add(Calendar.DAY_OF_YEAR, -(gaWeeks * 7));

            // Format result
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String lmpDate = sdf.format(examDate.getTime());

            tvResult.setText(lmpDate);
        });
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
