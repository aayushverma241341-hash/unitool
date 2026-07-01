package com.labjournal.androidlab;

/*
 * ============================================================================
 * LayoutDemoActivity.java — Layout Types Demo Screen (Exercise 9)
 * ============================================================================
 * This Activity demonstrates the four fundamental Android layout managers by
 * inflating different demo layouts into a FrameLayout container at runtime.
 *
 * Layout Types Covered:
 *   1. LinearLayout     — arranges children in a single row or column
 *   2. RelativeLayout   — positions children relative to each other / parent
 *   3. TableLayout      — arranges children in rows and columns
 *   4. FrameLayout      — stacks children on top of each other (z-ordering)
 *
 * Key Android Concepts:
 *   • LayoutInflater — creates View objects from XML layout resources
 *   • FrameLayout as a container — host for dynamically-swapped content
 *   • Button highlighting — visual indication of the active selection
 *
 * Package: com.labjournal.androidlab
 * ============================================================================
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class LayoutDemoActivity extends AppCompatActivity {

    // ── UI References ──────────────────────────────────────────────────────
    private MaterialButton btnLinear, btnRelative, btnTable, btnFrame;
    private FrameLayout layoutContainer;
    private TextView tvLayoutInfo;

    // ── Currently active button (used for highlight logic) ─────────────────
    private MaterialButton activeButton = null;

    /*
     * ======================================================================
     * onCreate — Entry point
     * ======================================================================
     * Sets up the toolbar, binds buttons, and loads the default demo
     * (LinearLayout) when the Activity first opens.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_demo);

        // ── Step 1: Set up the Toolbar ─────────────────────────────────
        setupToolbar();

        // ── Step 2: Bind views ─────────────────────────────────────────
        initViews();

        // ── Step 3: Wire up button click listeners ─────────────────────
        setupButtons();

        // ── Step 4: Show the LinearLayout demo by default ──────────────
        showDemo(DemoType.LINEAR);
    }

    /*
     * ------------------------------------------------------------------
     * setupToolbar()
     * ------------------------------------------------------------------
     * Configures the toolbar with a back-navigation arrow.
     */
    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar_layout_demo);
        setSupportActionBar(toolbar);

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
     * Finds all views by ID and stores references in member fields.
     */
    private void initViews() {
        btnLinear     = findViewById(R.id.btn_linear_demo);
        btnRelative   = findViewById(R.id.btn_relative_demo);
        btnTable      = findViewById(R.id.btn_table_demo);
        btnFrame      = findViewById(R.id.btn_frame_demo);
        layoutContainer = findViewById(R.id.layout_container);
        tvLayoutInfo    = findViewById(R.id.tv_layout_info);
    }

    /*
     * ------------------------------------------------------------------
     * setupButtons()
     * ------------------------------------------------------------------
     * Each button switches the demo content and updates the info text.
     */
    private void setupButtons() {
        btnLinear.setOnClickListener(v -> showDemo(DemoType.LINEAR));
        btnRelative.setOnClickListener(v -> showDemo(DemoType.RELATIVE));
        btnTable.setOnClickListener(v -> showDemo(DemoType.TABLE));
        btnFrame.setOnClickListener(v -> showDemo(DemoType.FRAME));
    }

    // ==================================================================
    //  DEMO SWITCHING LOGIC
    // ==================================================================

    /**
     * Enum representing the four layout types we demonstrate.
     * Each value maps to a layout resource and a descriptive info string.
     */
    private enum DemoType {
        LINEAR, RELATIVE, TABLE, FRAME
    }

    /*
     * ------------------------------------------------------------------
     * showDemo(DemoType type)
     * ------------------------------------------------------------------
     * Central method that:
     *   1. Clears the container
     *   2. Inflates the appropriate demo layout
     *   3. Adds it to the container
     *   4. Updates the info text
     *   5. Highlights the active button
     *
     * KEY CONCEPT — LayoutInflater:
     *   LayoutInflater reads an XML layout file and creates the
     *   corresponding View hierarchy.  We call inflate(layoutRes,
     *   container, false) — passing 'false' means "don't attach to
     *   the container yet" so we can do it manually after clearing.
     */
    private void showDemo(DemoType type) {
        // ── 1. Remove any previously inflated demo layout ──────────────
        layoutContainer.removeAllViews();

        // ── 2. Determine which layout to inflate and which info to show ─
        int layoutRes;
        String info;
        MaterialButton targetButton;

        switch (type) {
            case LINEAR:
                layoutRes = R.layout.demo_linear;
                targetButton = btnLinear;
                info = "📐 LinearLayout\n\n"
                     + "• Arranges children in a single direction: vertical (column) or horizontal (row).\n"
                     + "• Use android:orientation to set the direction.\n"
                     + "• android:layout_weight distributes remaining space proportionally.\n"
                     + "• Simple and intuitive, but nesting deeply can hurt performance.\n"
                     + "• Best for: simple lists, toolbars, button rows, form fields.";
                break;

            case RELATIVE:
                layoutRes = R.layout.demo_relative;
                targetButton = btnRelative;
                info = "🔗 RelativeLayout\n\n"
                     + "• Positions children relative to each other or to the parent.\n"
                     + "• Attributes: layout_above, layout_below, layout_toStartOf, layout_toEndOf, etc.\n"
                     + "• layout_centerInParent, layout_alignParentTop/Bottom/Start/End for parent-relative positioning.\n"
                     + "• Eliminates the need for nested LinearLayouts.\n"
                     + "• Best for: forms, overlapping elements, complex positioning without nesting.";
                break;

            case TABLE:
                layoutRes = R.layout.demo_table;
                targetButton = btnTable;
                info = "📊 TableLayout\n\n"
                     + "• Arranges children into rows and columns.\n"
                     + "• You use <TableRow> elements to define a row.\n"
                     + "• Columns can be stretched using android:stretchColumns.\n"
                     + "• Views can span multiple columns using android:layout_span.\n"
                     + "• Best for: structured data, spreadsheet-like views, forms.";
                break;

            case FRAME:
            default:
                layoutRes = R.layout.demo_frame;
                targetButton = btnFrame;
                info = "📚 FrameLayout\n\n"
                     + "• Designed to hold a single child, but can stack multiple children.\n"
                     + "• Children are drawn in XML order — last child is on top (z-ordering).\n"
                     + "• layout_gravity positions children within the frame (center, top|start, etc.).\n"
                     + "• android:alpha controls transparency for overlapping effects.\n"
                     + "• Best for: Fragment containers, image overlays, simple stacking, badges.";
                break;
        }

        // ── 3. Inflate the demo layout into the container ──────────────
        /*
         * LayoutInflater.from(context) obtains the system LayoutInflater.
         * inflate(resource, root, attachToRoot):
         *   - resource     : the XML layout to inflate
         *   - root         : the parent ViewGroup (used for LayoutParams)
         *   - attachToRoot : false = don't add to root yet; we do it next
         */
        View demoView = LayoutInflater.from(this)
                .inflate(layoutRes, layoutContainer, false);

        // ── 4. Add the inflated view to the container ──────────────────
        layoutContainer.addView(demoView);

        // ── 5. Update the info text below the container ────────────────
        tvLayoutInfo.setText(info);

        // ── 6. Highlight the active button, un-highlight others ────────
        highlightButton(targetButton);
    }

    // ==================================================================
    //  BUTTON HIGHLIGHTING
    // ==================================================================

    /*
     * ------------------------------------------------------------------
     * highlightButton(MaterialButton button)
     * ------------------------------------------------------------------
     * Visually indicates which layout demo is currently active by
     * giving the selected button a filled background and white text,
     * while reverting all other buttons to the default text-only style.
     *
     * This provides clear visual feedback to the user about which
     * layout type they are currently viewing.
     */
    private void highlightButton(MaterialButton button) {
        // Reset ALL buttons to the default (un-highlighted) style
        resetButtonStyle(btnLinear);
        resetButtonStyle(btnRelative);
        resetButtonStyle(btnTable);
        resetButtonStyle(btnFrame);

        // Apply the highlighted (active) style to the selected button
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        button.setTextColor(ContextCompat.getColor(this, android.R.color.white));

        // Track the currently active button
        activeButton = button;
    }

    /*
     * ------------------------------------------------------------------
     * resetButtonStyle(MaterialButton button)
     * ------------------------------------------------------------------
     * Reverts a button to its default un-highlighted appearance:
     * transparent background with the primary color text.
     */
    private void resetButtonStyle(MaterialButton button) {
        button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        button.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }
}
