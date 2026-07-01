package com.labjournal.androidlab;

/*
 * ============================================================================
 * HelloWorldActivity.java - Exercise 6: Hello World
 * ============================================================================
 *
 * PURPOSE:
 *   This activity demonstrates the classic "Hello World" program in Android.
 *   "Hello World" is traditionally the first program written when learning
 *   any new programming language or platform. In Android, this means creating
 *   an Activity that displays "Hello World!" text on screen.
 *
 * WHAT IS AN ACTIVITY?
 *   An Activity is a single, focused screen that the user can interact with.
 *   Think of it as one "page" or "screen" in your app. Every visible screen
 *   in an Android app is backed by an Activity (or Fragment) class.
 *
 *   Key points about Activities:
 *   - Each Activity has a corresponding XML layout file that defines its UI
 *   - Activities must be declared in AndroidManifest.xml
 *   - Activities have a lifecycle (create → start → resume → pause → stop → destroy)
 *   - The system manages Activities on a "back stack" for navigation
 *
 * WHAT THIS ACTIVITY DOES:
 *   1. Sets up the Toolbar as the ActionBar with a back/up navigation arrow
 *   2. Displays the "Hello World!" greeting (defined in the XML layout)
 *   3. Shows a Toast message confirming the activity has loaded
 *   4. Handles back navigation when the user presses the toolbar's back arrow
 *
 * KEY CONCEPTS DEMONSTRATED:
 *   - AppCompatActivity: Base class with backward-compatible features
 *   - Toolbar as ActionBar: Modern replacement for the legacy ActionBar
 *   - setSupportActionBar(): Designates a Toolbar as the activity's action bar
 *   - getSupportActionBar().setDisplayHomeAsUpEnabled(true): Shows the back arrow
 *   - onSupportNavigateUp(): Called when the user presses the back/up arrow
 *   - Toast: A brief popup message that appears at the bottom of the screen
 *
 * XML LAYOUT:
 *   This activity uses res/layout/activity_hello_world.xml which contains:
 *   - A Toolbar at the top for navigation
 *   - A centered "Hello World!" heading with an Android robot emoji
 *   - A subtitle and exercise description footer
 *   - A gradient background for visual appeal
 *
 * @author Lab Journal Project
 * @version 1.0
 * ============================================================================
 */

// === IMPORT SECTION ===

import android.os.Bundle;            // Bundle: A mapping of String keys to various Parcelable values.
                                     // Used to pass data between activities and to save/restore state.

import android.widget.Toast;         // Toast: A small popup message that automatically disappears.
                                     // Useful for brief, non-intrusive notifications to the user.

import androidx.appcompat.app.AppCompatActivity;   // AppCompatActivity: Base class that provides
                                                    // backward-compatible implementations of newer
                                                    // Android features (e.g., ActionBar, Material themes)
                                                    // for older Android versions.

import androidx.appcompat.widget.Toolbar;           // Toolbar: A ViewGroup that can be used as an
                                                    // action bar. More flexible than the legacy ActionBar
                                                    // because it can be placed anywhere in the layout.

/*
 * ============================================================================
 * CLASS DEFINITION
 * ============================================================================
 * HelloWorldActivity extends AppCompatActivity.
 *
 * WHY AppCompatActivity (not plain Activity)?
 *   AppCompatActivity is from the AndroidX support library and provides:
 *   - Material Design themes and widgets on ALL Android versions
 *   - ActionBar/Toolbar support with consistent behavior
 *   - Fragment support with the latest APIs
 *   - Night mode and other modern features
 *
 *   Using AppCompatActivity ensures our app looks and behaves consistently
 *   across Android versions from API 21 (Lollipop) to the latest.
 */
public class HelloWorldActivity extends AppCompatActivity {

    /*
     * ========================================================================
     * onCreate() - The Entry Point of the Activity
     * ========================================================================
     * This method is called when the activity is first created. It is the
     * most important lifecycle callback because it's where you perform
     * all your one-time initialization:
     *
     *   1. Call super.onCreate() — MANDATORY. The parent class performs
     *      essential setup that our activity depends on.
     *
     *   2. setContentView() — Inflates the XML layout file and renders it
     *      on screen. "Inflate" means parsing the XML and creating the
     *      corresponding Java view objects in memory.
     *
     *   3. Set up the Toolbar — Find it in the layout, then tell the system
     *      to use it as our action bar.
     *
     *   4. Show a Toast — A quick confirmation message for the user.
     *
     * @param savedInstanceState A Bundle containing previously saved state.
     *        This is null the first time the activity is created, but contains
     *        data if the activity is being re-created (e.g., after a screen
     *        rotation). We don't use it here since our UI is static.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Step 1: Call the parent class's onCreate method
        // This is ALWAYS required as the first line in onCreate().
        // The superclass performs critical initialization including:
        // - Restoring the activity's previously saved state (if any)
        // - Initializing the window and decor view
        // - Setting up the activity's internal state
        super.onCreate(savedInstanceState);

        // Step 2: Set the content view to our Hello World layout
        // R.layout.activity_hello_world refers to:
        //   res/layout/activity_hello_world.xml
        //
        // After this call, all views defined in the XML become part of
        // the view hierarchy and can be found using findViewById().
        setContentView(R.layout.activity_hello_world);

        // Step 3: Set up the Toolbar as the ActionBar
        // We use a Toolbar widget instead of the default ActionBar because:
        // - It can be positioned anywhere in the layout
        // - It supports custom views and more flexible styling
        // - It works consistently across all Android versions via AppCompat
        setupToolbar();

        // Step 4: Show a welcome Toast message
        // Toast.makeText() creates a Toast object with three parameters:
        //   - Context (this): The current activity context
        //   - Text: The message to display
        //   - Duration: LENGTH_SHORT (~2 seconds) or LENGTH_LONG (~3.5 seconds)
        //
        // .show() is required to actually display the Toast on screen.
        // Without .show(), the Toast is created but never displayed!
        Toast.makeText(this, "Hello World App Loaded! 👋", Toast.LENGTH_SHORT).show();
    }

    /*
     * ========================================================================
     * setupToolbar() - Configuring the App Bar
     * ========================================================================
     * This method finds the Toolbar in the layout and configures it as the
     * activity's ActionBar. It also enables the "Up" (back) navigation arrow.
     *
     * THE ACTION BAR / APP BAR:
     *   The action bar (also called app bar) is the bar at the top of the
     *   screen that typically shows the activity's title and provides
     *   navigation and action items. In Material Design, this is a key
     *   navigation component.
     *
     * BACK/UP NAVIGATION:
     *   setDisplayHomeAsUpEnabled(true) shows a left-pointing arrow (←)
     *   in the toolbar. When pressed, it calls onSupportNavigateUp().
     *   This allows the user to return to the previous screen (MainActivity).
     *
     *   "Up" navigation is different from "Back" navigation:
     *   - Back: Returns to the previous screen in chronological order
     *   - Up: Returns to the logical parent in the app's hierarchy
     *   In our case, both go to MainActivity, so the distinction is minor.
     */
    private void setupToolbar() {
        // Find the Toolbar widget in the layout by its ID
        // This ID must match android:id="@+id/toolbar_hello_world" in the XML
        Toolbar toolbar = findViewById(R.id.toolbar_hello_world);

        // Set this Toolbar as the activity's ActionBar
        // After this call, the Toolbar will:
        // - Display the activity's title (set via app:title in XML or setTitle())
        // - Handle menu items and navigation icons
        // - Respond to standard ActionBar APIs
        setSupportActionBar(toolbar);

        // Enable the "Up" navigation arrow in the toolbar
        // getSupportActionBar() returns the ActionBar object (our Toolbar)
        // setDisplayHomeAsUpEnabled(true) makes the back arrow visible
        //
        // We check for null to avoid NullPointerException, even though
        // we just set the action bar. This is a defensive programming practice.
        if (getSupportActionBar() != null) {
            // Show the back/up arrow (←) in the top-left corner
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Optionally set a custom title (overrides the XML app:title)
            getSupportActionBar().setTitle("Hello World");
        }
    }

    /*
     * ========================================================================
     * onSupportNavigateUp() - Handling Back/Up Navigation
     * ========================================================================
     * This method is called when the user presses the back arrow (←) in
     * the Toolbar/ActionBar. We override it to define what happens when
     * the user wants to navigate "up" in the app hierarchy.
     *
     * In our implementation, we simply call finish() to close this activity
     * and return to the previous activity (MainActivity) on the back stack.
     *
     * finish() does the following:
     *   1. Calls onPause() on this activity
     *   2. Calls onStop() on this activity
     *   3. Calls onDestroy() on this activity
     *   4. Removes this activity from the back stack
     *   5. Resumes the previous activity (MainActivity)
     *
     * @return true to indicate we handled the navigation event ourselves.
     *         Returning true prevents the default behavior from executing.
     */
    @Override
    public boolean onSupportNavigateUp() {
        // Close this activity and return to the parent activity (MainActivity)
        // The finish() method triggers the activity's destruction lifecycle:
        // onPause() → onStop() → onDestroy()
        finish();

        // Return true to indicate the navigation event was handled
        // If we returned false, the system would try default navigation behavior
        return true;
    }

    /*
     * ========================================================================
     * ADDITIONAL NOTES FOR LAB JOURNAL - Exercise 6
     * ========================================================================
     *
     * WHAT WE LEARNED:
     *   1. Every Android screen is represented by an Activity class
     *   2. Each Activity has an XML layout file that defines its visual structure
     *   3. onCreate() is where we initialize the UI (setContentView + findViewById)
     *   4. Toolbar provides a modern, customizable app bar with navigation
     *   5. Toast messages provide brief, non-blocking feedback to users
     *   6. Activities are managed on a back stack for easy navigation
     *
     * THE ANDROID PROJECT STRUCTURE:
     *   app/
     *   ├── src/main/
     *   │   ├── java/com/labjournal/androidlab/  ← Java source files
     *   │   │   ├── MainActivity.java             (Dashboard)
     *   │   │   └── HelloWorldActivity.java       (This file)
     *   │   ├── res/
     *   │   │   ├── layout/                       ← XML layout files
     *   │   │   │   ├── activity_main.xml
     *   │   │   │   └── activity_hello_world.xml
     *   │   │   ├── values/                       ← Resource values
     *   │   │   │   ├── strings.xml
     *   │   │   │   └── colors.xml
     *   │   │   └── drawable/                     ← Graphics and shapes
     *   │   │       └── gradient_header.xml
     *   │   └── AndroidManifest.xml               ← App configuration
     *   └── build.gradle                          ← Build configuration
     *
     * MANIFEST ENTRY REQUIRED:
     *   <activity
     *       android:name=".HelloWorldActivity"
     *       android:parentActivityName=".MainActivity"
     *       android:label="Hello World" />
     *
     *   The parentActivityName attribute tells Android that MainActivity
     *   is the logical parent, enabling proper "Up" navigation behavior.
     */
}
