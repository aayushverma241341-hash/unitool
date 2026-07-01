package com.labjournal.androidlab;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GpaTrackerActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etCourseName, etCredits, etGrade;
    private Button btnAddGrade;
    private TextView tvOverallGpa;
    private LinearLayout layoutGradesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa_tracker);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DatabaseHelper(this);
        etCourseName = findViewById(R.id.et_course_name);
        etCredits = findViewById(R.id.et_credits);
        etGrade = findViewById(R.id.et_grade);
        tvOverallGpa = findViewById(R.id.tv_overall_gpa);
        btnAddGrade = findViewById(R.id.btn_add_grade);
        layoutGradesContainer = findViewById(R.id.layout_grades_container);

        btnAddGrade.setOnClickListener(v -> addGrade());

        loadGrades();
    }

    private void addGrade() {
        String course = etCourseName.getText().toString().trim();
        String creditsStr = etCredits.getText().toString().trim();
        String gradeStr = etGrade.getText().toString().trim();

        if (TextUtils.isEmpty(course) || TextUtils.isEmpty(creditsStr) || TextUtils.isEmpty(gradeStr)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int credits = Integer.parseInt(creditsStr);
            double grade = Double.parseDouble(gradeStr);

            long id = dbHelper.addGrade(course, credits, grade);
            if (id != -1) {
                etCourseName.setText("");
                etCredits.setText("");
                etGrade.setText("");
                Toast.makeText(this, "Course Added", Toast.LENGTH_SHORT).show();
                loadGrades();
            } else {
                Toast.makeText(this, "Error adding course", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadGrades() {
        layoutGradesContainer.removeAllViews();
        Cursor cursor = dbHelper.getAllGrades();

        double totalGradePoints = 0;
        int totalCredits = 0;

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String course = cursor.getString(cursor.getColumnIndexOrThrow("course_name"));
                int credits = cursor.getInt(cursor.getColumnIndexOrThrow("credit_hours"));
                double grade = cursor.getDouble(cursor.getColumnIndexOrThrow("grade_score"));

                totalGradePoints += (grade * credits);
                totalCredits += credits;

                TextView tv = new TextView(this);
                tv.setText(course + " - " + credits + " Credits - Grade: " + grade);
                tv.setTextSize(16f);
                tv.setPadding(0, 16, 0, 16);
                
                tv.setOnLongClickListener(v -> {
                    dbHelper.deleteGrade(id);
                    Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show();
                    loadGrades();
                    return true;
                });

                layoutGradesContainer.addView(tv);
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (totalCredits > 0) {
            double gpa = totalGradePoints / totalCredits;
            tvOverallGpa.setText(String.format("%.2f", gpa));
        } else {
            tvOverallGpa.setText("0.00");
        }
    }
}
