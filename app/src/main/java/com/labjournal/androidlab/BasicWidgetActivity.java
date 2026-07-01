package com.labjournal.androidlab;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BasicWidgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_widget);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Find Widgets
        EditText etInput = findViewById(R.id.et_basic_input);
        Button btnAction = findViewById(R.id.btn_basic_action);
        CheckBox cbCheck = findViewById(R.id.cb_basic_check);
        TextView tvOutput = findViewById(R.id.tv_basic_output);

        // Set Button Click Listener
        btnAction.setOnClickListener(v -> {
            String input = etInput.getText().toString();
            boolean isChecked = cbCheck.isChecked();
            
            String outputText = "Button Clicked!\n" +
                                "Input Text: " + (input.isEmpty() ? "None" : input) + "\n" +
                                "Checkbox is: " + (isChecked ? "Checked" : "Unchecked");
            
            tvOutput.setText(outputText);
            Toast.makeText(this, "Widgets interacted!", Toast.LENGTH_SHORT).show();
        });
    }
}
