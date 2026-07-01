package com.labjournal.androidlab;

/*
 * ============================================================================
 * MainActivity.java - Android Lab Journal Dashboard
 * ============================================================================
 *
 * PURPOSE:
 *   This is the main entry point (launcher activity) for the Android Lab Journal
 *   application. It serves as a dashboard that displays 8 lab exercise cards
 *   in a grid layout. Tapping any card navigates the user to the corresponding
 *   exercise activity.
 *
 * KEY CONCEPTS DEMONSTRATED:
 *   1. AppCompatActivity - The base class for activities that use the support
 *      library action bar features.
 *   2. setContentView() - Inflates the XML layout and displays it on screen.
 *   3. findViewById() - Locates views in the inflated layout by their ID.
 *   4. View.OnClickListener - Interface for handling click events on views.
 *   5. Intent - A messaging object used to request an action from another
 *      app component (in this case, launching a new Activity).
 *   6. startActivity() - Launches a new activity using the provided Intent.
 *
 * LAYOUT:
 *   Uses activity_main.xml which contains a gradient header and a scrollable
 *   grid of CardViews (2 columns, 4 rows = 8 cards total).
 *
 * NAVIGATION FLOW:
 *   MainActivity (Dashboard)
 *       ├── Ex 5  → LifecycleDemoActivity
 *       ├── Ex 6  → HelloWorldActivity
 *       ├── Ex 7  → LinearLayoutActivity
 *       ├── Ex 8  → FormInputsActivity
 *       ├── Ex 9  → LayoutTypesActivity
 *       ├── Ex 10 → CalculatorActivity
 *       ├── Ex 11 → ToggleButtonActivity
 *       └── Ex 12 → DatabaseActivity
 *
 * @author Lab Journal Project
 * @version 1.0
 * ============================================================================
 */

// === IMPORT SECTION ===
// Android framework imports required for this activity

import android.content.Intent;       // Used to create navigation intents between activities
import android.os.Bundle;            // Used to pass data during activity lifecycle (e.g., savedInstanceState)
import android.view.View;            // Base class for all UI components; needed for click listeners
import android.widget.Toast;         // Used to show brief popup messages to the user

import androidx.appcompat.app.AppCompatActivity;  // Base class providing backward-compatible features
import androidx.cardview.widget.CardView;          // Material Design card widget for each exercise

public class MainActivity extends AppCompatActivity {

    /*
     * ========================================================================
     * CLASS VARIABLES
     * ========================================================================
     * We declare CardView references for each of the 8 exercise cards.
     * These will be bound to the XML views in onCreate() using findViewById().
     *
     * Naming convention: card_ex<number> matches the XML IDs for consistency.
     */

    // CardView references for each exercise card on the dashboard
    private CardView cardEx5;   // Exercise 5: Lifecycle Demo
    private CardView cardEx6;   // Exercise 6: Hello World
    private CardView cardEx7;   // Exercise 7: Linear Layout
    private CardView cardEx8;   // Exercise 8: Form Inputs
    private CardView cardEx9;   // Exercise 9: Layout Types
    private CardView cardEx10;  // Exercise 10: Calculator
    private CardView cardEx11;  // Exercise 11: Toggle Button
    private CardView cardEx12;  // Exercise 12: Database
    private CardView cardEx13;  // Exercise 13: Udemy Reg
    private CardView cardBasicWidget; // Basic Widget Demo
    private CardView cardFramework; // Internal Framework Demo
    private CardView cardTasks, cardGpa, cardContacts, cardSciCalc; // Final Projects
    private CardView cardFood, cardGrocery; // E-Commerce

    /*
     * ========================================================================
     * onCreate() - Activity Lifecycle: CREATION
     * ========================================================================
     * This is the first callback in the Activity lifecycle. It is called when
     * the activity is first created. This is where we perform all our static
     * setup:
     *   1. Inflate the layout (setContentView)
     *   2. Find and bind all views (findViewById)
     *   3. Set up event listeners (setOnClickListener)
     *
     * @param savedInstanceState - A Bundle containing the activity's previously
     *        saved state. If the activity has never existed before, this is null.
     *        This is used for configuration changes like screen rotation.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super.onCreate() first - this performs essential
        // framework initialization for the activity
        super.onCreate(savedInstanceState);

        /*
         * setContentView() inflates the XML layout file and makes it the
         * visible content of this activity. R.layout.activity_main refers to
         * the file: res/layout/activity_main.xml
         *
         * The 'R' class is auto-generated by the Android build system and
         * contains integer IDs for all resources (layouts, views, strings, etc.)
         */
        setContentView(R.layout.activity_main);

        // Initialize all card views by finding them in the layout
        initializeCards();

        // Set up click listeners for each card to handle navigation
        setupCardClickListeners();

        // Show a welcome toast when the dashboard loads
        Toast.makeText(this, "Welcome to Android Lab Journal! 📓", Toast.LENGTH_SHORT).show();
    }

    /*
     * ========================================================================
     * initializeCards() - View Binding
     * ========================================================================
     * This method uses findViewById() to locate each CardView in the inflated
     * layout and assign it to our class-level variable.
     *
     * findViewById() searches the view hierarchy (the tree of UI elements
     * created by setContentView) for a view with the specified ID.
     *
     * NOTE: This must be called AFTER setContentView(), otherwise the views
     * won't exist yet and findViewById() will return null.
     */
    private void initializeCards() {
        // Find each card by its unique ID defined in activity_main.xml
        // The R.id.card_ex5 corresponds to android:id="@+id/card_ex5" in XML
        cardEx5  = findViewById(R.id.card_ex5);   // Lifecycle Demo card
        cardEx6  = findViewById(R.id.card_ex6);   // Hello World card
        cardEx7  = findViewById(R.id.card_ex7);   // Linear Layout card
        cardEx8  = findViewById(R.id.card_ex8);   // Form Inputs card
        cardEx9  = findViewById(R.id.card_ex9);   // Layout Types card
        cardEx10 = findViewById(R.id.card_ex10);  // Calculator card
        cardEx11 = findViewById(R.id.card_ex11);  // Toggle Button card
        cardEx12 = findViewById(R.id.card_ex12);  // Database card
        cardEx13 = findViewById(R.id.card_ex13);  // Udemy Reg card
        cardBasicWidget = findViewById(R.id.card_basic_widget); // Basic Widget Demo card
        cardFramework = findViewById(R.id.card_framework); // Internal Framework Demo card
        
        // Final Projects
        cardTasks = findViewById(R.id.card_tasks);
        cardGpa = findViewById(R.id.card_gpa);
        cardContacts = findViewById(R.id.card_contacts);
        cardSciCalc = findViewById(R.id.card_scicalc);

        // E-Commerce
        cardFood = findViewById(R.id.card_food);
        cardGrocery = findViewById(R.id.card_grocery);
    }

    /*
     * ========================================================================
     * setupCardClickListeners() - Event Handling
     * ========================================================================
     * Sets up OnClickListener for each card. When a card is tapped, an Intent
     * is created to launch the corresponding exercise activity.
     *
     * HOW INTENTS WORK:
     *   An Intent is a messaging object that describes an operation to perform.
     *   Here we use "explicit intents" — we specify exactly which Activity
     *   class to launch. The two arguments to the Intent constructor are:
     *     1. Context (this) - The current activity, providing app context
     *     2. Target.class   - The Class object of the activity to launch
     *
     * The lambda syntax (v -> { ... }) is a concise way to implement the
     * View.OnClickListener interface, which has a single method: onClick(View v).
     */
    private void setupCardClickListeners() {

        /*
         * Exercise 5: Lifecycle Demo
         * Launches LifecycleDemoActivity to demonstrate Activity lifecycle
         * callbacks (onCreate, onStart, onResume, onPause, onStop, onDestroy)
         */
        cardEx5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an explicit intent targeting LifecycleDemoActivity
                Intent intent = new Intent(MainActivity.this, LifecycleActivity.class);
                // Launch the activity — Android will call its onCreate() method
                startActivity(intent);
            }
        });

        /*
         * Exercise 6: Hello World
         * Launches HelloWorldActivity — the classic first Android app.
         * This is the simplest activity that displays "Hello World!" text.
         */
        cardEx6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelloWorldActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Exercise 7: Linear Layout
         * Launches LinearLayoutActivity to explore LinearLayout properties
         * such as orientation (horizontal/vertical), weight, and gravity.
         */
        cardEx7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LinearLayoutActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Exercise 8: Form Inputs
         * Launches FormInputsActivity to demonstrate various input widgets
         * (EditText, Spinner, CheckBox, RadioButton, DatePicker, etc.)
         */
        cardEx8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Exercise 9: Layout Types
         * Launches LayoutTypesActivity to compare different layout managers
         * (LinearLayout, RelativeLayout, ConstraintLayout, FrameLayout, etc.)
         */
        cardEx9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LayoutDemoActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Exercise 10: Calculator
         * Launches CalculatorActivity — a functional calculator demonstrating
         * event handling, arithmetic operations, and dynamic UI updates.
         */
        cardEx10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Exercise 11: Toggle Button
         * Launches ToggleButtonActivity to demonstrate ToggleButton and Switch
         * widgets with state change listeners and visual feedback.
         */
        cardEx11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ToggleButtonActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Exercise 12: Database
         * Launches DatabaseActivity to introduce SQLite database operations
         * (CRUD: Create, Read, Update, Delete) using SQLiteOpenHelper.
         */
        cardEx12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DatabaseActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Exercise 13: Udemy Registration
         * Launches UdemyRegActivity demonstrating layout matching real-world UI.
         */
        cardEx13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UdemyRegActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Basic Widget Demo
         * Launches BasicWidgetActivity demonstrating fundamental UI components.
         */
        cardBasicWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BasicWidgetActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Internal Framework Demo
         * Launches InternalFrameworkActivity demonstrating Android Architecture.
         */
        cardFramework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InternalFrameworkActivity.class);
                startActivity(intent);
            }
        });

        // ================= FINAL PROJECTS ================= //

        cardTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TaskManagerActivity.class));
            }
        });

        cardGpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GpaTrackerActivity.class));
            }
        });

        cardContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContactBookActivity.class));
            }
        });

        cardSciCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScientificCalculatorActivity.class));
            }
        });

        // ================= E-COMMERCE ================= //

        View.OnClickListener storeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StoreWelcomeActivity.class));
            }
        };

        cardFood.setOnClickListener(storeListener);
        cardGrocery.setOnClickListener(storeListener);
    }

    /*
     * ========================================================================
     * ADDITIONAL NOTES FOR LAB JOURNAL
     * ========================================================================
     *
     * ACTIVITY STACK:
     *   When startActivity() is called, the new activity is pushed onto the
     *   "back stack." The user can press the Back button to return to this
     *   dashboard. Android manages this stack automatically.
     *
     * MANIFEST REQUIREMENT:
     *   Every Activity must be declared in AndroidManifest.xml. The MainActivity
     *   should have the LAUNCHER intent filter so it appears in the app drawer.
     *   Example:
     *     <activity android:name=".MainActivity">
     *         <intent-filter>
     *             <action android:name="android.intent.action.MAIN" />
     *             <category android:name="android.intent.category.LAUNCHER" />
     *         </intent-filter>
     *     </activity>
     *
     * VIEW BINDING ALTERNATIVE:
     *   Modern Android development recommends using ViewBinding or DataBinding
     *   instead of findViewById(). ViewBinding generates a binding class for
     *   each XML layout, providing type-safe access to views without the
     *   risk of null pointer exceptions.
     */
}
