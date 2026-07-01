package com.labjournal.androidlab;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * ============================================================================
 * DatabaseActivity.java — Exercise 12: Student Database (SQLite) CRUD
 * ============================================================================
 *
 * PURPOSE:
 *   Provides a complete user interface for performing CRUD operations on
 *   the students SQLite database via the DatabaseHelper class.
 *
 * CRUD OPERATIONS DEMONSTRATED:
 *
 *   ┌──────────┬─────────────────────────────────────────────────────────────┐
 *   │ Operation│ Description                                               │
 *   ├──────────┼─────────────────────────────────────────────────────────────┤
 *   │ CREATE   │ btn_add → Validates inputs, calls addStudent(),           │
 *   │          │ shows Toast "Record Added!", clears fields                │
 *   ├──────────┼─────────────────────────────────────────────────────────────┤
 *   │ READ     │ btn_view_all → Calls getAllStudents(), iterates Cursor,   │
 *   │          │ formats and displays in tv_records                        │
 *   ├──────────┼─────────────────────────────────────────────────────────────┤
 *   │ UPDATE   │ btn_update → Finds student by Roll No, updates record     │
 *   │          │ with new values from input fields                         │
 *   ├──────────┼─────────────────────────────────────────────────────────────┤
 *   │ DELETE   │ btn_delete → Finds student by Roll No, deletes record     │
 *   │          │ btn_delete_all → Confirmation dialog, then deletes all    │
 *   └──────────┴─────────────────────────────────────────────────────────────┘
 *
 * DATA FLOW:
 *   User Input → Validation → DatabaseHelper method → Toast feedback → Refresh display
 *
 * IMPORTANT PATTERNS:
 *   • Always validate user input before database operations
 *   • Always close Cursors after reading to prevent memory leaks
 *   • Show confirmation dialogs before destructive operations
 *   • Refresh the display after any modification
 *
 * ============================================================================
 */
public class DatabaseActivity extends AppCompatActivity {

    // ── View References ──────────────────────────────────────────────────
    private TextInputEditText etStudentName;  // Student name input field
    private TextInputEditText etRollNo;       // Roll number input field
    private TextInputEditText etMarks;        // Marks input field

    private MaterialButton btnAdd;            // Add Record button
    private MaterialButton btnUpdate;         // Update Record button
    private MaterialButton btnDelete;         // Delete Record button
    private MaterialButton btnViewAll;        // View All Records button
    private MaterialButton btnDeleteAll;      // Delete All Records button

    private TextView tvRecords;               // Display area for records

    // ── Database Helper ──────────────────────────────────────────────────
    // Single instance of DatabaseHelper used for all operations
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        // ─────────────────────────────────────────────────────────────
        // Step 1: Set up the Toolbar with back navigation
        // ─────────────────────────────────────────────────────────────
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Close this activity and return to previous
            }
        });

        // ─────────────────────────────────────────────────────────────
        // Step 2: Initialize the DatabaseHelper
        // ─────────────────────────────────────────────────────────────
        // Creating the helper does NOT create the database file yet.
        // The database is created lazily on first read/write operation.
        dbHelper = new DatabaseHelper(this);

        // ─────────────────────────────────────────────────────────────
        // Step 3: Find all views by their IDs
        // ─────────────────────────────────────────────────────────────
        etStudentName = findViewById(R.id.et_student_name);
        etRollNo      = findViewById(R.id.et_roll_no);
        etMarks       = findViewById(R.id.et_marks);
        btnAdd        = findViewById(R.id.btn_add);
        btnUpdate     = findViewById(R.id.btn_update);
        btnDelete     = findViewById(R.id.btn_delete);
        btnViewAll    = findViewById(R.id.btn_view_all);
        btnDeleteAll  = findViewById(R.id.btn_delete_all);
        tvRecords     = findViewById(R.id.tv_records);

        // ─────────────────────────────────────────────────────────────
        // Step 4: Set up button click listeners
        // ─────────────────────────────────────────────────────────────

        /*
         * ADD RECORD (CREATE operation)
         * ─────────────────────────────
         * Flow:
         *   1. Read values from input fields
         *   2. Validate that all fields are filled
         *   3. Call dbHelper.addStudent() to INSERT the record
         *   4. Show success/failure Toast
         *   5. Clear input fields
         *   6. Refresh the records display
         */
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read input values, trimming whitespace
                String name   = etStudentName.getText().toString().trim();
                String rollNo = etRollNo.getText().toString().trim();
                String marks  = etMarks.getText().toString().trim();

                // Validate: All fields must be non-empty
                if (name.isEmpty() || rollNo.isEmpty() || marks.isEmpty()) {
                    Toast.makeText(DatabaseActivity.this,
                            "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;  // Stop here — don't proceed with the insert
                }

                // Parse marks from String to double
                double marksValue;
                try {
                    marksValue = Double.parseDouble(marks);
                } catch (NumberFormatException e) {
                    // User entered invalid number (shouldn't happen with numberDecimal keyboard)
                    Toast.makeText(DatabaseActivity.this,
                            "Invalid marks value", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert into database
                // addStudent() returns the row ID (positive) on success, -1 on failure
                long result = dbHelper.addStudent(name, rollNo, marksValue);

                if (result != -1) {
                    // Insertion successful
                    Toast.makeText(DatabaseActivity.this,
                            "Record Added! (ID: " + result + ")", Toast.LENGTH_SHORT).show();
                    clearFields();       // Reset input fields for next entry
                    displayAllRecords(); // Refresh the records display
                } else {
                    // Insertion failed (rare, usually due to disk issues)
                    Toast.makeText(DatabaseActivity.this,
                            "Failed to add record", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
         * VIEW ALL RECORDS (READ operation)
         * ──────────────────────────────────
         * Queries all records and displays them in a formatted text view.
         */
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllRecords();
            }
        });

        /*
         * UPDATE RECORD (UPDATE operation)
         * ─────────────────────────────────
         * Flow:
         *   1. Read Roll Number from input (used to FIND the record)
         *   2. Query the database for that roll number
         *   3. If found, update the record with new Name and Marks values
         *   4. Show success/failure Toast
         *   5. Refresh the display
         *
         * Note: Roll Number is used as the search key because it's a
         * natural identifier. The actual UPDATE uses the internal row ID.
         */
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name   = etStudentName.getText().toString().trim();
                String rollNo = etRollNo.getText().toString().trim();
                String marks  = etMarks.getText().toString().trim();

                // Roll number is required to find the target record
                if (rollNo.isEmpty()) {
                    Toast.makeText(DatabaseActivity.this,
                            "Enter Roll Number to update", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Name and marks are required for the update
                if (name.isEmpty() || marks.isEmpty()) {
                    Toast.makeText(DatabaseActivity.this,
                            "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double marksValue;
                try {
                    marksValue = Double.parseDouble(marks);
                } catch (NumberFormatException e) {
                    Toast.makeText(DatabaseActivity.this,
                            "Invalid marks value", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Search for the student by Roll Number
                Cursor cursor = dbHelper.getStudentByRollNo(rollNo);

                if (cursor != null && cursor.moveToFirst()) {
                    // Student found — get their internal ID for the UPDATE query
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    cursor.close();  // Close cursor ASAP to free resources

                    // Perform the update using the internal ID
                    int rowsAffected = dbHelper.updateStudent(id, name, rollNo, marksValue);

                    if (rowsAffected > 0) {
                        Toast.makeText(DatabaseActivity.this,
                                "Record Updated!", Toast.LENGTH_SHORT).show();
                        clearFields();
                        displayAllRecords();
                    } else {
                        Toast.makeText(DatabaseActivity.this,
                                "Update failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // No student found with that roll number
                    if (cursor != null) cursor.close();
                    Toast.makeText(DatabaseActivity.this,
                            "No record found with Roll No: " + rollNo,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
         * DELETE RECORD (DELETE operation)
         * ─────────────────────────────────
         * Flow:
         *   1. Read Roll Number from input
         *   2. Find the student by roll number
         *   3. If found, delete using their internal ID
         *   4. Show feedback Toast
         *   5. Refresh display
         */
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rollNo = etRollNo.getText().toString().trim();

                if (rollNo.isEmpty()) {
                    Toast.makeText(DatabaseActivity.this,
                            "Enter Roll Number to delete", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Search for the student by Roll Number
                Cursor cursor = dbHelper.getStudentByRollNo(rollNo);

                if (cursor != null && cursor.moveToFirst()) {
                    // Student found — get their ID
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String studentName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    cursor.close();  // Always close the cursor!

                    // Delete the record
                    dbHelper.deleteStudent(id);

                    Toast.makeText(DatabaseActivity.this,
                            "Deleted: " + studentName + " (Roll: " + rollNo + ")",
                            Toast.LENGTH_SHORT).show();
                    clearFields();
                    displayAllRecords();
                } else {
                    if (cursor != null) cursor.close();
                    Toast.makeText(DatabaseActivity.this,
                            "No record found with Roll No: " + rollNo,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
         * DELETE ALL RECORDS (Bulk DELETE operation)
         * ──────────────────────────────────────────
         * Shows a confirmation AlertDialog before deleting.
         *
         * AlertDialog is essential for destructive operations — it
         * prevents accidental data loss by requiring explicit user
         * confirmation.
         *
         * AlertDialog.Builder pattern:
         *   1. Create a Builder with context
         *   2. Set title and message
         *   3. Set positive button (confirm) with action
         *   4. Set negative button (cancel) with dismiss
         *   5. Call show() to display
         */
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build and show a confirmation dialog
                new AlertDialog.Builder(DatabaseActivity.this)
                        .setTitle("Delete All Records")
                        .setMessage("Are you sure you want to delete ALL student records? "
                                + "This action cannot be undone.")
                        .setIcon(android.R.drawable.ic_dialog_alert)

                        // Positive button — user confirms deletion
                        .setPositiveButton("Delete All", (dialog, which) -> {
                            dbHelper.deleteAllStudents();
                            Toast.makeText(DatabaseActivity.this,
                                    "All records deleted!", Toast.LENGTH_SHORT).show();
                            clearFields();
                            displayAllRecords();  // Will show "No records found"
                        })

                        // Negative button — user cancels, dialog is dismissed
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();  // Simply close the dialog
                        })

                        .show();  // Display the dialog
            }
        });

        // ─────────────────────────────────────────────────────────────
        // Step 5: Display existing records on activity launch
        // ─────────────────────────────────────────────────────────────
        displayAllRecords();
    }

    // =====================================================================
    // displayAllRecords() — Reads and displays all database records
    // =====================================================================

    /**
     * Queries all student records from the database and displays them
     * in the tv_records TextView in a formatted layout.
     *
     * Format per record:
     *   ID: 1 | Name: John | Roll: 101 | Marks: 95.5
     *   ──────────────────────────────────────────────
     *
     * CURSOR USAGE PATTERN:
     *   1. Get cursor from query
     *   2. Check if cursor has data (moveToFirst())
     *   3. Loop through rows (do-while with moveToNext())
     *   4. Extract data using getColumnIndexOrThrow() + getXxx()
     *   5. CLOSE the cursor when done
     *
     * getColumnIndexOrThrow() is preferred over getColumnIndex() because
     * it throws an exception if the column doesn't exist, making bugs
     * easier to find. getColumnIndex() silently returns -1.
     */
    private void displayAllRecords() {
        // Execute SELECT * FROM students
        Cursor cursor = dbHelper.getAllStudents();

        // Check if we got any results
        if (cursor != null && cursor.getCount() > 0) {
            // StringBuilder is more efficient than String concatenation in loops
            StringBuilder sb = new StringBuilder();

            // Add a header line
            sb.append("Total Records: ").append(cursor.getCount()).append("\n");
            sb.append("══════════════════════════════\n\n");

            // Move cursor to the first row
            cursor.moveToFirst();

            // Iterate through all rows
            do {
                /*
                 * Extract data from the current row.
                 *
                 * getColumnIndexOrThrow("name") — Returns the 0-based column
                 *   index for the column named "name". Throws an exception
                 *   if the column doesn't exist in the result set.
                 *
                 * cursor.getInt(index)    — Read integer from column
                 * cursor.getString(index) — Read string from column
                 * cursor.getDouble(index) — Read double from column
                 */
                int id        = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name   = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String rollNo = cursor.getString(cursor.getColumnIndexOrThrow("roll_no"));
                double marks  = cursor.getDouble(cursor.getColumnIndexOrThrow("marks"));

                // Format and append this record
                sb.append("ID: ").append(id)
                  .append(" | Name: ").append(name)
                  .append("\nRoll: ").append(rollNo)
                  .append(" | Marks: ").append(marks)
                  .append("\n──────────────────────────────\n");

            } while (cursor.moveToNext());  // Move to next row; returns false when done

            // Display the formatted records
            tvRecords.setText(sb.toString());

        } else {
            // No records found — show a friendly message
            tvRecords.setText("No records found.\nTap 'Add' to create your first student record.");
        }

        // ── IMPORTANT: Always close the cursor! ──
        // Cursors hold references to database resources. Failing to close
        // them causes memory leaks and can exhaust the cursor limit.
        if (cursor != null) {
            cursor.close();
        }
    }

    // =====================================================================
    // clearFields() — Resets all input fields to empty
    // =====================================================================

    /**
     * Clears all three input fields after a successful operation.
     *
     * setText("") sets the field to an empty string.
     * This provides a clean slate for the next data entry.
     */
    private void clearFields() {
        etStudentName.setText("");
        etRollNo.setText("");
        etMarks.setText("");

        // Optional: Move focus back to the first field for convenience
        etStudentName.requestFocus();
    }
}
