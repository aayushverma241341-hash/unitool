package com.labjournal.androidlab;

/*
 * ============================================================================
 * LinearLayoutActivity.java
 * ============================================================================
 *
 * PURPOSE:
 * This activity accompanies the Linear Layout Demo screen (Exercise 7).
 * It demonstrates how to manipulate Views programmatically using button
 * click listeners inside a LinearLayout-based UI.
 *
 * LINEAR LAYOUT KEY CONCEPTS:
 *   1. LinearLayout arranges its children in a single direction — either
 *      VERTICAL (top-to-bottom) or HORIZONTAL (left-to-right).
 *   2. layout_weight lets you distribute remaining space proportionally
 *      among children. Combined with layout_width="0dp" (horizontal) or
 *      layout_height="0dp" (vertical), it creates flexible, responsive UIs.
 *   3. gravity vs layout_gravity:
 *        - gravity = how a view aligns its OWN content
 *        - layout_gravity = how the PARENT positions this view
 *   4. Nesting LinearLayouts (e.g., horizontal inside vertical) creates
 *      multi-row button bars without needing more complex layouts.
 *
 * WHAT THIS ACTIVITY DOES:
 *   - Change Color: cycles the ImageView's background tint through 5 colours
 *   - Rotate Image: rotates the ImageView by 45° on each press
 *   - Scale Image:  toggles between 1.0× (normal) and 1.5× (enlarged)
 *   - Reset:        restores rotation, scale, and colour to defaults
 *   - Info:         shows an AlertDialog explaining what LinearLayout is
 *   - Each action updates the status TextView at the bottom of the screen
 *
 * Package: com.labjournal.androidlab
 * ============================================================================
 */

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LinearLayoutActivity extends AppCompatActivity {

    // -------------------------------------------------------------------------
    // View References
    // -------------------------------------------------------------------------

    /** The demo image that the user can manipulate. */
    private ImageView ivDemoImage;

    /** Status bar at the bottom — shows feedback about the last action. */
    private TextView tvStatus;

    // -------------------------------------------------------------------------
    // State Variables
    // -------------------------------------------------------------------------

    /**
     * Index into the COLORS array, tracking which colour is currently applied.
     * We cycle through colours using modular arithmetic (colorIndex % COLORS.length).
     */
    private int colorIndex = 0;

    /**
     * The current rotation angle of the image in degrees.
     * Incremented by 45° on each "Rotate" press.
     */
    private float currentRotation = 0f;

    /**
     * Whether the image is currently scaled up (1.5×).
     * Toggled on each "Scale" press.
     */
    private boolean isScaled = false;

    /**
     * Array of colours to cycle through for the "Change Color" button.
     * Colours are parsed from hex strings using Color.parseColor().
     *
     * Colours:
     *   0 = Red        (#F44336)
     *   1 = Green      (#4CAF50)
     *   2 = Blue       (#2196F3)
     *   3 = Yellow     (#FFEB3B)
     *   4 = Purple     (#9C27B0)
     */
    private static final int[] COLORS = {
        Color.parseColor("#F44336"),   // Red
        Color.parseColor("#4CAF50"),   // Green
        Color.parseColor("#2196F3"),   // Blue
        Color.parseColor("#FFEB3B"),   // Yellow
        Color.parseColor("#9C27B0")    // Purple
    };

    /**
     * Human-readable names for the colours (used in the status message).
     */
    private static final String[] COLOR_NAMES = {
        "Red", "Green", "Blue", "Yellow", "Purple"
    };

    // =========================================================================
    // onCreate
    // =========================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout);

        // ---- Toolbar Setup ----
        // Configure the toolbar as the ActionBar and enable the back arrow.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // ---- Bind Views ----
        ivDemoImage = findViewById(R.id.iv_demo_image);
        tvStatus    = findViewById(R.id.tv_status);

        // ---- Set Up Button Click Listeners ----
        setupChangeColorButton();
        setupRotateButton();
        setupScaleButton();
        setupResetButton();
        setupInfoButton();
    }

    // =========================================================================
    // Button Setup Methods
    // Each method is separated for clarity and follows Single Responsibility.
    // =========================================================================

    /**
     * CHANGE COLOR BUTTON
     *
     * Cycles the ImageView's background tint through the COLORS array.
     *
     * How it works:
     *   1. We use the colorIndex to pick the current colour.
     *   2. We apply it using setBackgroundTintList() with a ColorStateList.
     *   3. We increment the index using modular arithmetic so it wraps around
     *      (0 → 1 → 2 → 3 → 4 → 0 → 1 → ...).
     *
     * LinearLayout relevance:
     *   This button sits in a horizontal LinearLayout with layout_weight="1",
     *   meaning it shares the row equally with the Rotate and Scale buttons.
     */
    private void setupChangeColorButton() {
        findViewById(R.id.btn_change_color).setOnClickListener(v -> {
            // Get the current colour from the array
            int color = COLORS[colorIndex];
            String colorName = COLOR_NAMES[colorIndex];

            // Apply the colour as a background tint to the ImageView.
            // ColorStateList.valueOf() creates a simple single-state list.
            ivDemoImage.setBackgroundTintList(ColorStateList.valueOf(color));

            // Update the status bar
            updateStatus("Background colour changed to " + colorName);

            // Advance to the next colour (with wrap-around)
            colorIndex = (colorIndex + 1) % COLORS.length;
        });
    }

    /**
     * ROTATE BUTTON
     *
     * Rotates the ImageView by 45 degrees on each click.
     *
     * How it works:
     *   - setRotation() rotates the view around its centre point.
     *   - We accumulate the angle (0 → 45 → 90 → 135 → ... → 360 → 405 → ...).
     *   - Android normalises large angles internally, so values > 360 are fine.
     *
     * This demonstrates programmatic view transformation, which is commonly
     * done in Android UI code alongside layout management.
     */
    private void setupRotateButton() {
        findViewById(R.id.btn_rotate).setOnClickListener(v -> {
            // Increment rotation by 45 degrees
            currentRotation += 45f;

            // Apply the new rotation angle
            ivDemoImage.setRotation(currentRotation);

            // Update status with the current angle (mod 360 for readability)
            updateStatus("Image rotated to " + ((int) currentRotation % 360) + "°");
        });
    }

    /**
     * SCALE BUTTON
     *
     * Toggles the ImageView between normal size (1.0×) and enlarged (1.5×).
     *
     * How it works:
     *   - setScaleX() and setScaleY() scale the view around its pivot point.
     *   - The default pivot is the centre of the view.
     *   - We toggle a boolean flag to alternate between the two states.
     *
     * Note: Scaling does NOT change the view's layout bounds — surrounding
     * views stay in place. The scaled image simply draws larger or smaller
     * within its allocated space.
     */
    private void setupScaleButton() {
        findViewById(R.id.btn_scale).setOnClickListener(v -> {
            // Toggle the scaled state
            isScaled = !isScaled;

            // Determine the target scale factor
            float scale = isScaled ? 1.5f : 1.0f;

            // Apply uniform scaling on both axes
            ivDemoImage.setScaleX(scale);
            ivDemoImage.setScaleY(scale);

            // Update status
            updateStatus("Image scale: " + scale + "×" + (isScaled ? " (enlarged)" : " (normal)"));
        });
    }

    /**
     * RESET BUTTON
     *
     * Restores all image transformations to their defaults:
     *   - Rotation → 0°
     *   - Scale → 1.0×
     *   - Background tint → removed (null)
     *   - Colour index → 0
     *
     * This provides a clean slate so the student can re-run experiments.
     */
    private void setupResetButton() {
        findViewById(R.id.btn_reset).setOnClickListener(v -> {
            // Reset rotation
            currentRotation = 0f;
            ivDemoImage.setRotation(0f);

            // Reset scale
            isScaled = false;
            ivDemoImage.setScaleX(1.0f);
            ivDemoImage.setScaleY(1.0f);

            // Remove background tint (restore original appearance)
            ivDemoImage.setBackgroundTintList(null);

            // Reset colour index to start from the first colour again
            colorIndex = 0;

            // Update status
            updateStatus("All transformations reset to default");
        });
    }

    /**
     * INFO BUTTON
     *
     * Shows an AlertDialog explaining what LinearLayout is, its key properties,
     * and how it differs from other layout types.
     *
     * AlertDialog is a standard Android component for displaying modal messages.
     * Using AlertDialog.Builder follows the Builder design pattern.
     */
    private void setupInfoButton() {
        findViewById(R.id.btn_info).setOnClickListener(v -> {
            // Build the dialog content — a concise summary of LinearLayout concepts
            String infoMessage =
                "LinearLayout is a ViewGroup that arranges its child views " +
                "in a single direction — either vertically or horizontally.\n\n" +

                "Key Properties:\n" +
                "• android:orientation — \"vertical\" or \"horizontal\"\n" +
                "• android:layout_weight — distributes extra space proportionally\n" +
                "• android:gravity — aligns content within the view\n" +
                "• android:layout_gravity — positions the view within its parent\n\n" +

                "When to use LinearLayout:\n" +
                "✓ Simple row or column arrangements\n" +
                "✓ Equally-spaced button bars (using weight)\n" +
                "✓ Quick prototyping of simple UIs\n\n" +

                "When to prefer other layouts:\n" +
                "✗ Complex nested layouts (use ConstraintLayout)\n" +
                "✗ Overlapping views (use FrameLayout)\n" +
                "✗ Grid arrangements (use GridLayout or RecyclerView)\n\n" +

                "This demo screen uses nested LinearLayouts: a vertical parent " +
                "with horizontal children for the button rows.";

            // Create and show the AlertDialog
            new AlertDialog.Builder(LinearLayoutActivity.this)
                .setTitle("About LinearLayout")
                .setMessage(infoMessage)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Got it!", (dialog, which) -> dialog.dismiss())
                .show();

            // Update status
            updateStatus("Info dialog shown");
        });
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /**
     * Updates the status TextView at the bottom of the screen with the given message.
     *
     * This provides immediate visual feedback to the student so they can see
     * what each button does without looking at Logcat.
     *
     * @param message The status message to display.
     */
    private void updateStatus(String message) {
        tvStatus.setText(message);
    }
}
