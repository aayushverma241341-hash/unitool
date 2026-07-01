package com.labjournal.androidlab;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TaskManagerActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etTaskInput;
    private Button btnAddTask;
    private LinearLayout layoutTasksContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DatabaseHelper(this);
        etTaskInput = findViewById(R.id.et_task_input);
        btnAddTask = findViewById(R.id.btn_add_task);
        layoutTasksContainer = findViewById(R.id.layout_tasks_container);

        btnAddTask.setOnClickListener(v -> {
            String task = etTaskInput.getText().toString().trim();
            if (TextUtils.isEmpty(task)) {
                Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show();
            } else {
                long id = dbHelper.addTask(task);
                if (id != -1) {
                    etTaskInput.setText("");
                    loadTasks();
                    Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error adding task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadTasks();
    }

    private void loadTasks() {
        layoutTasksContainer.removeAllViews();
        Cursor cursor = dbHelper.getAllTasks();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow("is_completed"));

                CheckBox cb = new CheckBox(this);
                cb.setText(title);
                cb.setChecked(isCompleted == 1);
                cb.setTextSize(18f);
                cb.setPadding(0, 16, 0, 16);

                cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    dbHelper.toggleTaskStatus(id, isChecked);
                });

                cb.setOnLongClickListener(v -> {
                    dbHelper.deleteTask(id);
                    loadTasks();
                    Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
                    return true;
                });

                layoutTasksContainer.addView(cb);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
