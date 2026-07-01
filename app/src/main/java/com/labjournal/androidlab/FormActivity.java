package com.labjournal.androidlab;

/*
 * ============================================================================
 * FormActivity.java — Registration Form Screen (Exercise 8)
 * ============================================================================
 * This Activity demonstrates how to work with a wide variety of Android form
 * input widgets.  It covers:
 *
 *   1. TextInputEditText  — text fields with Material floating labels
 *   2. DatePickerDialog    — date selection via a popup calendar
 *   3. RadioGroup          — single-choice selection (Gender)
 *   4. CheckBox            — multi-choice selection (Hobbies)
 *   5. Spinner             — dropdown / combo-box (Course)
 *   6. SeekBar             — slider for numeric rating
 *   7. Multiline EditText  — free-form text area (Address)
 *   8. Validation          — checking user input before submission
 *   9. AlertDialog         — displaying collected data in a dialog
 *  10. Form Reset          — programmatically clearing all widgets
 *
 * Package: com.labjournal.androidlab
 * ============================================================================
 */

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FormActivity extends AppCompatActivity {

    // ── TextInputEditText fields (wrapped by TextInputLayout) ──────────────
    private TextInputEditText etName, etEmail, etPhone, etPassword, etDob, etAddress;

    // ── TextInputLayout wrappers (used to show inline errors) ──────────────
    private TextInputLayout tilName, tilEmail, tilPhone, tilPassword, tilDob, tilAddress;

    // ── Radio group for single-choice gender selection ─────────────────────
    private RadioGroup rgGender;

    // ── CheckBoxes for multi-choice hobby selection ────────────────────────
    private CheckBox cbReading, cbSports, cbMusic, cbCoding;

    // ── Spinner (dropdown) for course selection ────────────────────────────
    private Spinner spinnerCourse;

    // ── SeekBar for numeric rating + its value label ───────────────────────
    private SeekBar seekbarRating;
    private TextView tvSeekbarValue;

    // ── Action buttons ─────────────────────────────────────────────────────
    private MaterialButton btnSubmit, btnReset;

    /*
     * ======================================================================
     * onCreate — Entry point of the Activity
     * ======================================================================
     * Called when the Activity is first created.  We perform all
     * initialisation here: binding views, setting up listeners, and
     * populating the Spinner with data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // ── Step 1: Set up the Toolbar with back navigation ────────────────
        setupToolbar();

        // ── Step 2: Bind all views to their XML counterparts ───────────────
        initViews();

        // ── Step 3: Set up the DatePicker for the Date of Birth field ──────
        setupDatePicker();

        // ── Step 4: Populate the Course spinner with items ─────────────────
        setupSpinner();

        // ── Step 5: Attach a listener to the SeekBar ──────────────────────
        setupSeekBar();

        // ── Step 6: Wire up the Submit and Reset buttons ──────────────────
        setupButtons();
    }

    /*
     * ------------------------------------------------------------------
     * setupToolbar()
     * ------------------------------------------------------------------
     * Configures the MaterialToolbar as the Activity's action bar and
     * enables the back (←) navigation button.
     */
    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar_form);
        setSupportActionBar(toolbar);

        // Show the back arrow and handle clicks to finish the activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /*
     * ------------------------------------------------------------------
     * initViews()
     * ------------------------------------------------------------------
     * Finds every form widget by its ID and stores it in a member field
     * so we can reference it elsewhere without repeated findViewById().
     */
    private void initViews() {
        // TextInputLayout wrappers — used for showing inline error messages
        tilName     = findViewById(R.id.til_name);
        tilEmail    = findViewById(R.id.til_email);
        tilPhone    = findViewById(R.id.til_phone);
        tilPassword = findViewById(R.id.til_password);
        tilDob      = findViewById(R.id.til_dob);
        tilAddress  = findViewById(R.id.til_address);

        // TextInputEditText fields — the actual editable inputs
        etName     = findViewById(R.id.et_name);
        etEmail    = findViewById(R.id.et_email);
        etPhone    = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etDob      = findViewById(R.id.et_dob);
        etAddress  = findViewById(R.id.et_address);

        // RadioGroup — only one RadioButton can be checked at a time
        rgGender = findViewById(R.id.rg_gender);

        // CheckBoxes — independent; any combination can be checked
        cbReading = findViewById(R.id.cb_reading);
        cbSports  = findViewById(R.id.cb_sports);
        cbMusic   = findViewById(R.id.cb_music);
        cbCoding  = findViewById(R.id.cb_coding);

        // Spinner — dropdown list for course selection
        spinnerCourse = findViewById(R.id.spinner_course);

        // SeekBar + its label
        seekbarRating  = findViewById(R.id.seekbar_rating);
        tvSeekbarValue = findViewById(R.id.tv_seekbar_value);

        // Action buttons
        btnSubmit = findViewById(R.id.btn_submit);
        btnReset  = findViewById(R.id.btn_reset);
    }

    /*
     * ------------------------------------------------------------------
     * setupDatePicker()
     * ------------------------------------------------------------------
     * The Date of Birth field (et_dob) is non-focusable.  Instead, when
     * the user taps it, we open a DatePickerDialog.  The selected date
     * is formatted as "dd/MM/yyyy" and placed into the EditText.
     *
     * KEY CONCEPT:
     *   DatePickerDialog takes year, month (0-indexed!), and day.
     *   We initialise it with the current date so the user has a
     *   sensible starting point.
     */
    private void setupDatePicker() {
        etDob.setOnClickListener(v -> {
            // Get the current date to use as the default selection
            final Calendar calendar = Calendar.getInstance();
            int year  = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);     // 0-indexed
            int day   = calendar.get(Calendar.DAY_OF_MONTH);

            // Create and show the DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    FormActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Format the date as dd/MM/yyyy (month is 0-indexed, so +1)
                        String formattedDate = String.format(Locale.getDefault(),
                                "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        etDob.setText(formattedDate);

                        // Clear any previous error on the DOB field
                        tilDob.setError(null);
                    },
                    year, month, day
            );

            // Prevent selecting a future date (user must have been born already!)
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    /*
     * ------------------------------------------------------------------
     * setupSpinner()
     * ------------------------------------------------------------------
     * Populates the Course Spinner with a list of items using an
     * ArrayAdapter.  The first item ("Select Course") acts as a
     * placeholder / prompt.
     *
     * KEY CONCEPT:
     *   ArrayAdapter bridges a data source (String array / List) to
     *   a Spinner widget.  We use the built-in
     *   android.R.layout.simple_spinner_item and
     *   android.R.layout.simple_spinner_dropdown_item layouts.
     */
    private void setupSpinner() {
        // Define the list of courses (first item is the placeholder)
        String[] courses = {
                "Select Course",
                "Computer Science",
                "Information Technology",
                "Electronics",
                "Mechanical",
                "Civil"
        };

        // Create an ArrayAdapter with the default spinner item layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                courses
        );

        // Specify the layout for the dropdown popup
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach the adapter to the Spinner
        spinnerCourse.setAdapter(adapter);
    }

    /*
     * ------------------------------------------------------------------
     * setupSeekBar()
     * ------------------------------------------------------------------
     * Attaches an OnSeekBarChangeListener that updates the label
     * (tv_seekbar_value) every time the user drags the thumb.
     *
     * KEY CONCEPT:
     *   SeekBar.OnSeekBarChangeListener has three callbacks:
     *     • onProgressChanged — fired continuously as the thumb moves
     *     • onStartTrackingTouch — thumb pressed down
     *     • onStopTrackingTouch  — thumb released
     *   We only need onProgressChanged here to update the label.
     */
    private void setupSeekBar() {
        seekbarRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the label to show the current value out of max
                tvSeekbarValue.setText(String.format(Locale.getDefault(),
                        "%d/%d", progress, seekBar.getMax()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not used — could be used for haptic feedback, etc.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not used — could be used to save the final value
            }
        });
    }

    /*
     * ------------------------------------------------------------------
     * setupButtons()
     * ------------------------------------------------------------------
     * Wires up the Submit and Reset buttons.
     *   • Submit → validates inputs, then shows an AlertDialog summary.
     *   • Reset  → clears every field back to its default state.
     */
    private void setupButtons() {
        btnSubmit.setOnClickListener(v -> submitForm());
        btnReset.setOnClickListener(v -> resetForm());
    }

    // ==================================================================
    //  FORM SUBMISSION & VALIDATION
    // ==================================================================

    /*
     * ------------------------------------------------------------------
     * submitForm()
     * ------------------------------------------------------------------
     * Validates every field.  If all validations pass, collects the data
     * and displays it in an AlertDialog.
     */
    private void submitForm() {
        // Clear all previous errors first
        clearErrors();

        // Run all validations; stop at the first failure
        if (!validateForm()) {
            return;   // A Toast / error has already been shown
        }

        // ── Collect all form data ─────────────────────────────────────
        String name     = getText(etName);
        String email    = getText(etEmail);
        String phone    = getText(etPhone);
        String password = getText(etPassword);
        String dob      = getText(etDob);
        String gender   = getSelectedGender();
        String hobbies  = getSelectedHobbies();
        String course   = spinnerCourse.getSelectedItem().toString();
        int    rating   = seekbarRating.getProgress();
        String address  = getText(etAddress);

        // ── Build a summary string ────────────────────────────────────
        StringBuilder summary = new StringBuilder();
        summary.append("Name: ").append(name).append("\n");
        summary.append("Email: ").append(email).append("\n");
        summary.append("Phone: ").append(phone).append("\n");
        summary.append("Password: ").append(maskPassword(password)).append("\n");
        summary.append("Date of Birth: ").append(dob).append("\n");
        summary.append("Gender: ").append(gender).append("\n");
        summary.append("Hobbies: ").append(hobbies.isEmpty() ? "None" : hobbies).append("\n");
        summary.append("Course: ").append(course).append("\n");
        summary.append("Rating: ").append(rating).append("/10").append("\n");
        summary.append("Address: ").append(address);

        // ── Show the summary in an AlertDialog ────────────────────────
        new AlertDialog.Builder(this)
                .setTitle("Registration Summary")
                .setMessage(summary.toString())
                .setIcon(R.drawable.ic_check_circle)
                .setPositiveButton("OK", (dialog, which) -> {
                    Toast.makeText(this, "Registration submitted!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Edit", null)   // Dismiss and let user edit
                .show();
    }

    /*
     * ------------------------------------------------------------------
     * validateForm()
     * ------------------------------------------------------------------
     * Validates each field in order.  Returns true if everything is OK.
     * Uses TextUtils.isEmpty() for null-safe empty checks and
     * Patterns.EMAIL_ADDRESS for email validation.
     */
    private boolean validateForm() {
        // ── 1. Name must not be empty ─────────────────────────────────
        if (TextUtils.isEmpty(getText(etName))) {
            tilName.setError("Name is required");
            etName.requestFocus();
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }

        // ── 2. Email must match a valid pattern ───────────────────────
        String email = getText(etEmail);
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email is required");
            etEmail.requestFocus();
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }

        // ── 3. Phone must be at least 10 digits ──────────────────────
        String phone = getText(etPhone);
        if (TextUtils.isEmpty(phone)) {
            tilPhone.setError("Phone number is required");
            etPhone.requestFocus();
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.length() < 10) {
            tilPhone.setError("Phone must be at least 10 digits");
            etPhone.requestFocus();
            Toast.makeText(this, "Phone number too short", Toast.LENGTH_SHORT).show();
            return false;
        }

        // ── 4. Password must be at least 6 characters ────────────────
        String password = getText(etPassword);
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password is required");
            etPassword.requestFocus();
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            tilPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
            return false;
        }

        // ── 5. Date of Birth must not be empty ───────────────────────
        if (TextUtils.isEmpty(getText(etDob))) {
            tilDob.setError("Date of Birth is required");
            Toast.makeText(this, "Please select your date of birth", Toast.LENGTH_SHORT).show();
            return false;
        }

        // ── 6. Gender must be selected ───────────────────────────────
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        // ── 7. A course must be selected (not the placeholder) ───────
        if (spinnerCourse.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a course", Toast.LENGTH_SHORT).show();
            return false;
        }

        // ── 8. Address must not be empty ─────────────────────────────
        if (TextUtils.isEmpty(getText(etAddress))) {
            tilAddress.setError("Address is required");
            etAddress.requestFocus();
            Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
            return false;
        }

        // All validations passed!
        return true;
    }

    // ==================================================================
    //  FORM RESET
    // ==================================================================

    /*
     * ------------------------------------------------------------------
     * resetForm()
     * ------------------------------------------------------------------
     * Restores every widget to its initial / default state:
     *   • Clears all EditText fields
     *   • Unchecks all RadioButtons and CheckBoxes
     *   • Resets the Spinner to the placeholder item
     *   • Resets the SeekBar to mid-point (5)
     *   • Clears all error messages
     */
    private void resetForm() {
        // Clear all text fields
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etPassword.setText("");
        etDob.setText("");
        etAddress.setText("");

        // Clear the RadioGroup selection (no gender selected)
        rgGender.clearCheck();

        // Uncheck all CheckBoxes
        cbReading.setChecked(false);
        cbSports.setChecked(false);
        cbMusic.setChecked(false);
        cbCoding.setChecked(false);

        // Reset Spinner to the first item (placeholder "Select Course")
        spinnerCourse.setSelection(0);

        // Reset SeekBar to default value of 5
        seekbarRating.setProgress(5);
        tvSeekbarValue.setText("5/10");

        // Remove all inline error messages
        clearErrors();

        // Inform the user
        Toast.makeText(this, "Form has been reset", Toast.LENGTH_SHORT).show();
    }

    // ==================================================================
    //  HELPER / UTILITY METHODS
    // ==================================================================

    /**
     * Safely extracts the trimmed text from a TextInputEditText.
     * Returns an empty string if the field is null.
     */
    private String getText(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    /**
     * Returns the text of the currently selected RadioButton in the
     * gender RadioGroup, or "Not selected" if none is checked.
     */
    private String getSelectedGender() {
        int selectedId = rgGender.getCheckedRadioButtonId();
        if (selectedId == -1) {
            return "Not selected";
        }
        RadioButton selectedButton = findViewById(selectedId);
        return selectedButton.getText().toString();
    }

    /**
     * Builds a comma-separated string of all checked hobbies.
     * Example output: "Reading, Music, Coding"
     */
    private String getSelectedHobbies() {
        List<String> hobbies = new ArrayList<>();
        if (cbReading.isChecked()) hobbies.add("Reading");
        if (cbSports.isChecked())  hobbies.add("Sports");
        if (cbMusic.isChecked())   hobbies.add("Music");
        if (cbCoding.isChecked())  hobbies.add("Coding");
        return TextUtils.join(", ", hobbies);
    }

    /**
     * Masks a password string for display, showing only the first and
     * last characters.  Example: "secret" → "s****t"
     */
    private String maskPassword(String password) {
        if (password.length() <= 2) {
            return "**";
        }
        StringBuilder masked = new StringBuilder();
        masked.append(password.charAt(0));
        for (int i = 1; i < password.length() - 1; i++) {
            masked.append('*');
        }
        masked.append(password.charAt(password.length() - 1));
        return masked.toString();
    }

    /**
     * Removes all error messages from every TextInputLayout.
     */
    private void clearErrors() {
        tilName.setError(null);
        tilEmail.setError(null);
        tilPhone.setError(null);
        tilPassword.setError(null);
        tilDob.setError(null);
        tilAddress.setError(null);
    }
}
