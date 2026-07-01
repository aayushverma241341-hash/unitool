package com.labjournal.androidlab;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

/**
 * ============================================================================
 * ToggleButtonActivity.java — Exercise 11: Toggle Button Demo
 * ============================================================================
 *
 * PURPOSE:
 *   Demonstrates two-state toggle controls in Android and how to respond
 *   to their state changes.
 *
 * KEY CONCEPTS:
 *
 *   1. ToggleButton vs Switch
 *      ─────────────────────────────────────────────────────────────────
 *      • ToggleButton: The classic Android on/off button. It displays
 *        different text depending on its state (textOn / textOff).
 *        Looks like a regular button that stays pressed.
 *
 *      • Switch / SwitchCompat: A modern sliding toggle introduced in
 *        API 14. Preferred for settings-style interfaces. SwitchCompat
 *        is the backwards-compatible version from the AppCompat library.
 *
 *      Both classes extend CompoundButton, which itself extends Button.
 *
 *   2. CompoundButton.OnCheckedChangeListener
 *      ─────────────────────────────────────────────────────────────────
 *      This is the common listener interface for all two-state buttons.
 *      It has a single method:
 *
 *          void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
 *
 *      • buttonView — the toggle/switch that changed
 *      • isChecked  — the new state (true = ON, false = OFF)
 *
 *      You can use the same listener for multiple toggles and switch on
 *      buttonView.getId() to determine which one changed.
 *
 *   3. Dynamic UI Changes
 *      ─────────────────────────────────────────────────────────────────
 *      The Dark Mode switch demonstrates how a toggle can drive major
 *      UI changes (background color, text color) at runtime.
 *
 * LIFECYCLE:
 *   onCreate → Find all views → Attach listeners → Ready
 *
 * ============================================================================
 */
public class ToggleButtonActivity extends AppCompatActivity {

    // ── View References ──────────────────────────────────────────────────
    // Root layout — background color changes for dark mode
    private LinearLayout rootLayout;

    // Basic ToggleButton and its status label
    private ToggleButton toggleBasic;
    private TextView tvToggleStatus;

    // Wi-Fi switch and status
    private SwitchCompat switchWifi;
    private TextView tvWifiStatus;

    // Bluetooth switch and status
    private SwitchCompat switchBluetooth;
    private TextView tvBluetoothStatus;

    // Notification switch and status
    private SwitchCompat switchNotification;
    private TextView tvNotificationStatus;

    // Dark Mode switch (using standard Switch for variety) and status
    private Switch switchDarkMode;
    private TextView tvDarkmodeStatus;

    // Visual indicator — changes color based on basic toggle
    private View viewIndicator;
    private TextView tvIndicatorLabel;

    // Summary text — shows all current states
    private TextView tvSummary;

    // Content layout — for dark mode text color changes
    private LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle_button);

        // ─────────────────────────────────────────────────────────────
        // Step 1: Set up the Toolbar with back navigation
        // ─────────────────────────────────────────────────────────────
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Enable the "up" (back) arrow in the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Handle toolbar back button press
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish() closes this activity and returns to the previous one
                finish();
            }
        });

        // ─────────────────────────────────────────────────────────────
        // Step 2: Find all views by their IDs
        // ─────────────────────────────────────────────────────────────
        rootLayout          = findViewById(R.id.root_layout);
        contentLayout       = findViewById(R.id.content_layout);
        toggleBasic         = findViewById(R.id.toggle_basic);
        tvToggleStatus      = findViewById(R.id.tv_toggle_status);
        switchWifi          = findViewById(R.id.switch_wifi);
        tvWifiStatus        = findViewById(R.id.tv_wifi_status);
        switchBluetooth     = findViewById(R.id.switch_bluetooth);
        tvBluetoothStatus   = findViewById(R.id.tv_bluetooth_status);
        switchNotification  = findViewById(R.id.switch_notification);
        tvNotificationStatus = findViewById(R.id.tv_notification_status);
        switchDarkMode      = findViewById(R.id.switch_dark_mode);
        tvDarkmodeStatus    = findViewById(R.id.tv_darkmode_status);
        viewIndicator       = findViewById(R.id.view_indicator);
        tvIndicatorLabel    = findViewById(R.id.tv_indicator_label);
        tvSummary           = findViewById(R.id.tv_summary);

        // ─────────────────────────────────────────────────────────────
        // Step 3: Set up listeners for each toggle/switch
        // ─────────────────────────────────────────────────────────────

        /*
         * LISTENER FOR BASIC TOGGLE BUTTON
         * ─────────────────────────────────
         * When the ToggleButton is toggled:
         *   • Update the status text to "Toggle is ON" or "Toggle is OFF"
         *   • Change the visual indicator color:
         *       Green (#4CAF50) when ON
         *       Red   (#F44336) when OFF
         */
        toggleBasic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle turned ON
                    tvToggleStatus.setText("Toggle is ON");
                    viewIndicator.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
                    tvIndicatorLabel.setText("Status: ON");
                } else {
                    // Toggle turned OFF
                    tvToggleStatus.setText("Toggle is OFF");
                    viewIndicator.setBackgroundColor(Color.parseColor("#F44336")); // Red
                    tvIndicatorLabel.setText("Status: OFF");
                }
                // Refresh the summary whenever any toggle changes
                updateSummary();
            }
        });

        /*
         * LISTENER FOR WI-FI SWITCH
         * ──────────────────────────
         * Simulates toggling Wi-Fi. In a real app, you'd use
         * WifiManager to actually control the hardware.
         * Here we just update status text and show a Toast.
         */
        switchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvWifiStatus.setText("Wi-Fi is ON");
                    Toast.makeText(ToggleButtonActivity.this,
                            "Wi-Fi Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    tvWifiStatus.setText("Wi-Fi is OFF");
                    Toast.makeText(ToggleButtonActivity.this,
                            "Wi-Fi Disabled", Toast.LENGTH_SHORT).show();
                }
                updateSummary();
            }
        });

        /*
         * LISTENER FOR BLUETOOTH SWITCH
         * ──────────────────────────────
         * Simulates toggling Bluetooth connectivity.
         */
        switchBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvBluetoothStatus.setText("Bluetooth is ON");
                    Toast.makeText(ToggleButtonActivity.this,
                            "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    tvBluetoothStatus.setText("Bluetooth is OFF");
                    Toast.makeText(ToggleButtonActivity.this,
                            "Bluetooth Disabled", Toast.LENGTH_SHORT).show();
                }
                updateSummary();
            }
        });

        /*
         * LISTENER FOR NOTIFICATION SWITCH
         * ─────────────────────────────────
         * Simulates enabling/disabling notifications.
         */
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvNotificationStatus.setText("Notifications are ON");
                    Toast.makeText(ToggleButtonActivity.this,
                            "Notifications Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    tvNotificationStatus.setText("Notifications are OFF");
                    Toast.makeText(ToggleButtonActivity.this,
                            "Notifications Disabled", Toast.LENGTH_SHORT).show();
                }
                updateSummary();
            }
        });

        /*
         * LISTENER FOR DARK MODE SWITCH
         * ──────────────────────────────
         * Demonstrates dynamic UI theming:
         *   • When ON:  Root layout → dark grey (#303030), text → white
         *   • When OFF: Root layout → white (#FFFFFF), text → dark
         *
         * In production apps, you'd typically use AppCompatDelegate
         * .setDefaultNightMode() instead. This manual approach is
         * for educational purposes.
         */
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // ── Enable Dark Mode ──
                    tvDarkmodeStatus.setText("Dark Mode is ON");
                    rootLayout.setBackgroundColor(Color.parseColor("#303030"));  // Dark grey
                    // Note: In a full implementation, you'd iterate all TextViews
                    // and change their colors, or use themes. This is simplified.
                } else {
                    // ── Disable Dark Mode ──
                    tvDarkmodeStatus.setText("Dark Mode is OFF");
                    rootLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));  // White
                }
                updateSummary();
            }
        });

        // ─────────────────────────────────────────────────────────────
        // Step 4: Initialize the summary with default states
        // ─────────────────────────────────────────────────────────────
        updateSummary();
    }

    /**
     * ────────────────────────────────────────────────────────────────
     * updateSummary()
     * ────────────────────────────────────────────────────────────────
     * Builds a formatted string showing the current state of every
     * toggle and switch, then sets it on the summary TextView.
     *
     * Called every time any toggle/switch changes state.
     *
     * Uses CompoundButton.isChecked() to read the current state of
     * each widget. This method returns true if the toggle is ON.
     * ────────────────────────────────────────────────────────────────
     */
    private void updateSummary() {
        // Build the summary string using a StringBuilder for efficiency
        StringBuilder sb = new StringBuilder();

        // Read each toggle's current state using isChecked()
        sb.append("Toggle:        ").append(toggleBasic.isChecked()        ? "ON" : "OFF").append("\n");
        sb.append("Wi-Fi:         ").append(switchWifi.isChecked()         ? "ON" : "OFF").append("\n");
        sb.append("Bluetooth:     ").append(switchBluetooth.isChecked()    ? "ON" : "OFF").append("\n");
        sb.append("Notifications: ").append(switchNotification.isChecked() ? "ON" : "OFF").append("\n");
        sb.append("Dark Mode:     ").append(switchDarkMode.isChecked()     ? "ON" : "OFF");

        // Set the formatted summary text
        tvSummary.setText(sb.toString());
    }
}
