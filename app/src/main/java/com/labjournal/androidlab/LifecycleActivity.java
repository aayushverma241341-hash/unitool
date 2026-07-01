package com.labjournal.androidlab;

/*
 * ============================================================================
 * LifecycleActivity.java
 * ============================================================================
 *
 * PURPOSE:
 * This activity demonstrates the complete Android Activity Lifecycle by
 * overriding every lifecycle callback method and providing visual feedback
 * when each one is triggered.
 *
 * THE ANDROID ACTIVITY LIFECYCLE:
 * Android activities go through a series of states from creation to destruction.
 * Understanding this lifecycle is critical because:
 *   - You must initialise resources at the right time (e.g., in onCreate).
 *   - You must release resources to avoid memory leaks (e.g., in onDestroy).
 *   - You must handle configuration changes (e.g., screen rotation).
 *   - You must save/restore state when the system kills your activity.
 *
 * The full lifecycle flow:
 *   onCreate  →  onStart  →  onResume  →  [Activity Running]
 *                                              ↓
 *                                          onPause  →  onStop  →  onDestroy
 *                                              ↑           ↑
 *                                          onResume    onRestart → onStart
 *
 * HOW THIS DEMO WORKS:
 *   1. Each lifecycle method updates a coloured indicator from grey → green.
 *   2. Each lifecycle method appends a timestamped entry to the on-screen log.
 *   3. Each lifecycle method writes to Logcat with the tag "LifecycleDemo".
 *   4. Two buttons let the student deliberately trigger lifecycle transitions:
 *      - "Finish Activity" calls finish(), which triggers onPause→onStop→onDestroy.
 *      - "Go to Hello World" starts HelloWorldActivity, triggering onPause→onStop.
 *
 * Package: com.labjournal.androidlab
 * ============================================================================
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LifecycleActivity extends AppCompatActivity {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    /** Logcat tag – used to filter lifecycle messages in Android Studio's Logcat. */
    private static final String TAG = "LifecycleDemo";

    /** Green colour applied to indicators when a lifecycle method fires. */
    private static final int COLOR_ACTIVE = Color.parseColor("#4CAF50");

    /** Grey colour for indicators that have NOT yet fired. */
    private static final int COLOR_INACTIVE = Color.parseColor("#BDBDBD");

    // -------------------------------------------------------------------------
    // View References
    // Each of these TextViews acts as a small "status dot" in the lifecycle
    // diagram. They start grey and turn green when the method is called.
    // -------------------------------------------------------------------------

    private TextView tvOnCreate;
    private TextView tvOnStart;
    private TextView tvOnResume;
    private TextView tvOnPause;
    private TextView tvOnStop;
    private TextView tvOnDestroy;

    /** The log area where we append timestamped lifecycle events. */
    private TextView tvLifecycleLog;

    /** The ScrollView wrapping the log, so we can auto-scroll to the bottom. */
    private ScrollView svLog;

    /**
     * A StringBuilder that accumulates all log entries.
     * We use a StringBuilder rather than concatenating strings for performance.
     */
    private StringBuilder logBuilder = new StringBuilder();

    /**
     * SimpleDateFormat for creating human-readable timestamps.
     * Format: [HH:mm:ss] — e.g., [14:30:05]
     */
    private final SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]", Locale.getDefault());

    // =========================================================================
    // LIFECYCLE METHOD: onCreate()
    // =========================================================================
    /**
     * Called when the activity is first created.
     *
     * This is where you perform one-time initialisation:
     *   - Inflate the layout with setContentView()
     *   - Find and store references to views (findViewById)
     *   - Set up click listeners
     *   - Restore saved instance state (if any)
     *
     * After onCreate(), the activity is in the "Created" state but is NOT
     * yet visible to the user. The system will call onStart() next.
     *
     * @param savedInstanceState If the activity was previously destroyed and
     *                           the system saved its state, this Bundle contains
     *                           the saved data. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the XML layout for this activity
        setContentView(R.layout.activity_lifecycle);

        // ---- Toolbar Setup ----
        // We set up the toolbar as the action bar and enable the back arrow
        // so students can navigate back to the main menu.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // Show ← arrow
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Handle the back arrow click
        toolbar.setNavigationOnClickListener(v -> finish());

        // ---- Bind Views ----
        // Obtain references to each lifecycle indicator TextView.
        tvOnCreate  = findViewById(R.id.tv_oncreate);
        tvOnStart   = findViewById(R.id.tv_onstart);
        tvOnResume  = findViewById(R.id.tv_onresume);
        tvOnPause   = findViewById(R.id.tv_onpause);
        tvOnStop    = findViewById(R.id.tv_onstop);
        tvOnDestroy = findViewById(R.id.tv_ondestroy);

        // The log area and its parent ScrollView
        tvLifecycleLog = findViewById(R.id.tv_lifecycle_log);
        svLog          = findViewById(R.id.sv_log);

        // ---- Button: Finish Activity ----
        // Calling finish() will cause Android to run onPause → onStop → onDestroy.
        // This lets students see the "destruction" side of the lifecycle.
        findViewById(R.id.btn_finish).setOnClickListener(v -> {
            appendLog("User pressed 'Finish Activity'");
            finish();   // Triggers onPause → onStop → onDestroy
        });

        // ---- Button: Go to Hello World ----
        // Starting a new activity causes THIS activity to go through
        // onPause → onStop (but NOT onDestroy, because it's still in the
        // back stack). When the user presses Back, onRestart → onStart → onResume
        // will fire.
        findViewById(R.id.btn_go_hello).setOnClickListener(v -> {
            appendLog("User pressed 'Go to Hello World' — starting HelloWorldActivity");
            Intent intent = new Intent(LifecycleActivity.this, HelloWorldActivity.class);
            startActivity(intent);
        });

        // ---- Mark this lifecycle stage ----
        activateIndicator(tvOnCreate);
        appendLog("onCreate() called — Activity is being created");
        Log.d(TAG, "onCreate() called");
    }

    // =========================================================================
    // LIFECYCLE METHOD: onStart()
    // =========================================================================
    /**
     * Called after onCreate() (or after onRestart() if the activity was stopped).
     *
     * At this point the activity is about to become visible to the user.
     * This is a good place to:
     *   - Register BroadcastReceivers that affect the UI
     *   - Start animations
     *   - Begin monitoring sensors, GPS, etc.
     *
     * After onStart(), the system calls onResume().
     */
    @Override
    protected void onStart() {
        super.onStart();

        activateIndicator(tvOnStart);
        appendLog("onStart() called — Activity is becoming visible");
        Log.d(TAG, "onStart() called");
    }

    // =========================================================================
    // LIFECYCLE METHOD: onResume()
    // =========================================================================
    /**
     * Called after onStart() when the activity is about to start interacting
     * with the user.
     *
     * At this point the activity is in the foreground and has input focus.
     * This is the best place to:
     *   - Start camera preview
     *   - Resume paused animations or video playback
     *   - Acquire exclusive resources (e.g., camera, microphone)
     *
     * The activity stays in the "Resumed" (running) state until something
     * causes it to pause (e.g., a dialog appears, or the user switches apps).
     */
    @Override
    protected void onResume() {
        super.onResume();

        activateIndicator(tvOnResume);
        appendLog("onResume() called — Activity is in the foreground and interactive");
        Log.d(TAG, "onResume() called");
    }

    // =========================================================================
    // LIFECYCLE METHOD: onPause()
    // =========================================================================
    /**
     * Called when the activity is losing foreground focus (but may still be
     * partially visible, e.g., behind a translucent dialog).
     *
     * This is the first indication that the user is leaving the activity.
     * You should:
     *   - Pause animations, video, or audio playback
     *   - Save lightweight unsaved data (e.g., draft text)
     *   - Release exclusive resources like the camera
     *
     * IMPORTANT: onPause() must be fast. Do NOT do heavy work here (like
     * writing large files or making network calls) because it blocks the
     * transition to the next activity.
     */
    @Override
    protected void onPause() {
        super.onPause();

        activateIndicator(tvOnPause);
        appendLog("onPause() called — Activity is partially obscured / losing focus");
        Log.d(TAG, "onPause() called");
    }

    // =========================================================================
    // LIFECYCLE METHOD: onStop()
    // =========================================================================
    /**
     * Called when the activity is no longer visible to the user.
     *
     * This happens when:
     *   - Another activity covers this one completely
     *   - The user presses Home
     *   - The user navigates to another activity
     *
     * In onStop() you should:
     *   - Release resources that aren't needed while invisible
     *   - Save persistent data (e.g., write to a database)
     *   - Unregister BroadcastReceivers
     *
     * After onStop(), the activity is either restarted (onRestart → onStart)
     * or destroyed (onDestroy).
     */
    @Override
    protected void onStop() {
        super.onStop();

        activateIndicator(tvOnStop);
        appendLog("onStop() called — Activity is no longer visible");
        Log.d(TAG, "onStop() called");
    }

    // =========================================================================
    // LIFECYCLE METHOD: onDestroy()
    // =========================================================================
    /**
     * Called before the activity is destroyed. This is the final callback.
     *
     * This can happen because:
     *   1. The user pressed Back or called finish().
     *   2. The system is destroying the activity due to a configuration change
     *      (e.g., screen rotation) — in this case a new instance is created.
     *   3. The system is reclaiming memory.
     *
     * In onDestroy() you should:
     *   - Release ALL remaining resources
     *   - Cancel background threads
     *   - Unregister all listeners
     *
     * After onDestroy(), the Activity object is garbage-collected.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        activateIndicator(tvOnDestroy);
        appendLog("onDestroy() called — Activity is being destroyed");
        Log.d(TAG, "onDestroy() called");
    }

    // =========================================================================
    // LIFECYCLE METHOD: onRestart()
    // =========================================================================
    /**
     * Called when the activity is being re-displayed to the user after having
     * been stopped (i.e., it was invisible but not destroyed).
     *
     * This happens when:
     *   - The user presses Back from another activity to return here
     *   - The user switches back from the Recent Apps screen
     *
     * onRestart() is always followed by onStart().
     *
     * This is a good place to:
     *   - Refresh data that may have changed while the activity was stopped
     *   - Re-query a database or re-fetch network data
     */
    @Override
    protected void onRestart() {
        super.onRestart();

        // Note: There is no separate indicator for onRestart in the diagram,
        // but we still log it so students can see when it fires.
        appendLog("onRestart() called — Activity is being restarted after being stopped");
        Log.d(TAG, "onRestart() called");
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /**
     * Activates a lifecycle indicator by changing its background tint to green.
     *
     * We use {@link android.view.View#getBackground()} and mutate + setTint
     * to colour the indicator. The drawable must support tinting (our custom
     * drawable 'lifecycle_indicator_bg' is a simple shape that supports it).
     *
     * @param indicator The TextView acting as the status dot.
     */
    private void activateIndicator(TextView indicator) {
        if (indicator != null && indicator.getBackground() != null) {
            // mutate() ensures we don't affect shared drawable state
            indicator.getBackground().mutate().setTint(COLOR_ACTIVE);
        }
    }

    /**
     * Appends a timestamped log entry to the on-screen lifecycle log.
     *
     * Format: [HH:mm:ss] message
     * Example: [14:30:05] onCreate() called — Activity is being created
     *
     * After appending, we auto-scroll the ScrollView to the bottom so the
     * latest entry is always visible.
     *
     * @param message The message to log (without timestamp — we add it).
     */
    private void appendLog(String message) {
        // Create a timestamp string like "[14:30:05]"
        String timestamp = sdf.format(new Date());

        // Append to our StringBuilder buffer
        logBuilder.append(timestamp).append(" ").append(message).append("\n");

        // Update the TextView with the full log
        tvLifecycleLog.setText(logBuilder.toString());

        // Auto-scroll the log ScrollView to the bottom.
        // We use post() to ensure the scroll happens after the TextView
        // has been re-measured with the new text.
        svLog.post(() -> svLog.fullScroll(ScrollView.FOCUS_DOWN));
    }
}
