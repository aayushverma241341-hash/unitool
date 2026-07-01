package com.labjournal.androidlab;

/*
 * ============================================================================
 *  CalculatorActivity.java
 *  Exercise 10 – Fully-Functional Calculator
 * ============================================================================
 *
 *  This Activity drives the calculator UI defined in activity_calculator.xml.
 *  It implements a simple "immediate-execution" calculator (like the stock
 *  iOS / Android calculator) that supports:
 *
 *      • Four basic arithmetic operations: +  −  ×  ÷
 *      • Clear (C) – resets all state
 *      • Plus / Minus (±) – toggles the sign of the current number
 *      • Percent (%) – divides the current number by 100
 *      • Decimal point (.) – only one per number is allowed
 *      • Equals (=) – evaluates the pending operation
 *
 *  Internally the calculator keeps:
 *      - firstOperand   : the left-hand number (stored when an operator is tapped)
 *      - currentOperator : the pending operator symbol (+, −, ×, ÷)
 *      - currentInput    : the string the user is currently typing
 *      - isNewInput      : flag that says the next digit should start fresh
 *
 *  All arithmetic uses Java doubles.  Results are formatted with
 *  DecimalFormat to strip unnecessary trailing zeros (e.g. "8.0" → "8").
 *
 *  A single View.OnClickListener handles every button via a switch on
 *  the view's resource ID, keeping the code compact and easy to follow.
 * ============================================================================
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;

/**
 * CalculatorActivity – Exercise 10
 *
 * Implements a fully working calculator with a modern dark UI.
 * All button clicks are routed through a single {@link View.OnClickListener}.
 */
public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    // ---------------------------------------------------------------
    //  UI REFERENCES
    // ---------------------------------------------------------------

    /** Small text above the main display – shows the running expression
     *  (e.g. "42 +" or "42 + 8 ="). */
    private TextView tvExpression;

    /** Large text – shows the number the user is entering or the
     *  result of the last calculation. */
    private TextView tvResult;

    // ---------------------------------------------------------------
    //  CALCULATOR STATE
    // ---------------------------------------------------------------

    /** The left-hand operand, stored when the user presses an operator. */
    private double firstOperand = 0;

    /** The pending operator symbol: "+", "−", "×", "÷", or empty. */
    private String currentOperator = "";

    /** The digit string currently being built by the user (e.g. "3.14"). */
    private String currentInput = "0";

    /**
     * When true the next digit press will clear {@code currentInput} and
     * start a fresh number.  This is set after an operator or equals is
     * tapped so the user doesn't accidentally append to the previous value.
     */
    private boolean isNewInput = true;

    /**
     * Formatter that strips trailing zeros.
     * "8.0" → "8", "3.14000" → "3.14", very large/small numbers keep
     * up to 10 decimal places.
     */
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##########");

    // ===================================================================
    //  LIFECYCLE
    // ===================================================================

    /**
     * Called when the Activity is first created.
     * Sets up the toolbar, grabs view references, and wires every button
     * to the single {@link #onClick(View)} handler.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // ---------- Toolbar setup ----------
        // Use the Toolbar as the ActionBar and enable the back arrow.
        Toolbar toolbar = findViewById(R.id.toolbar_calculator);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // ← arrow
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Pressing the toolbar back arrow finishes the Activity.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // ---------- Display TextViews ----------
        tvExpression = findViewById(R.id.tv_expression);
        tvResult     = findViewById(R.id.tv_result);

        // ---------- Wire ALL buttons to this Activity's onClick ----------
        // We collect every button ID in an array and loop through it,
        // which is much tidier than 19 separate findViewById + setOnClickListener calls.
        int[] buttonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
                R.id.btn_add, R.id.btn_subtract, R.id.btn_multiply, R.id.btn_divide,
                R.id.btn_equals, R.id.btn_clear, R.id.btn_decimal,
                R.id.btn_plusminus, R.id.btn_percent
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(this);
        }
    }

    // ===================================================================
    //  CLICK HANDLER  –  single entry-point for every calculator button
    // ===================================================================

    /**
     * Dispatches button taps to the appropriate helper method based on
     * the view's resource ID.  Using a switch/case keeps all logic in
     * one place and makes it easy to add new buttons later.
     *
     * @param view The button that was tapped.
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();

        // ---- DIGIT BUTTONS (0 – 9) ----
        // Each digit is appended to the current input string.
        if (id == R.id.btn_0) {
            appendDigit("0");
        } else if (id == R.id.btn_1) {
            appendDigit("1");
        } else if (id == R.id.btn_2) {
            appendDigit("2");
        } else if (id == R.id.btn_3) {
            appendDigit("3");
        } else if (id == R.id.btn_4) {
            appendDigit("4");
        } else if (id == R.id.btn_5) {
            appendDigit("5");
        } else if (id == R.id.btn_6) {
            appendDigit("6");
        } else if (id == R.id.btn_7) {
            appendDigit("7");
        } else if (id == R.id.btn_8) {
            appendDigit("8");
        } else if (id == R.id.btn_9) {
            appendDigit("9");
        }

        // ---- OPERATOR BUTTONS ----
        // Store the first operand and the chosen operator, then
        // flag the display so the next digit starts a new number.
        else if (id == R.id.btn_add) {
            handleOperator("+");
        } else if (id == R.id.btn_subtract) {
            handleOperator("−");
        } else if (id == R.id.btn_multiply) {
            handleOperator("×");
        } else if (id == R.id.btn_divide) {
            handleOperator("÷");
        }

        // ---- EQUALS ----
        else if (id == R.id.btn_equals) {
            handleEquals();
        }

        // ---- CLEAR ----
        else if (id == R.id.btn_clear) {
            handleClear();
        }

        // ---- DECIMAL POINT ----
        else if (id == R.id.btn_decimal) {
            handleDecimal();
        }

        // ---- PLUS / MINUS (toggle sign) ----
        else if (id == R.id.btn_plusminus) {
            handlePlusMinus();
        }

        // ---- PERCENT ----
        else if (id == R.id.btn_percent) {
            handlePercent();
        }
    }

    // ===================================================================
    //  DIGIT INPUT
    // ===================================================================

    /**
     * Appends a single digit character to {@link #currentInput}.
     *
     * If {@link #isNewInput} is true we start a fresh string instead of
     * appending, which happens right after an operator or equals press.
     *
     * Leading-zero suppression: if the current string is just "0" and
     * the incoming digit is not "0", we replace rather than append
     * (so we get "7" instead of "07").
     *
     * @param digit A single character "0" – "9".
     */
    private void appendDigit(String digit) {
        // Start a fresh number when flagged (after operator / equals).
        if (isNewInput) {
            currentInput = digit;
            isNewInput = false;
        } else {
            // Replace a lone "0" with the new digit (no leading zeros).
            if (currentInput.equals("0") && digit.equals("0")) {
                // Already "0", ignore extra zeros.
                return;
            } else if (currentInput.equals("0")) {
                currentInput = digit;
            } else {
                currentInput += digit;
            }
        }
        // Update the main display immediately.
        tvResult.setText(currentInput);
    }

    // ===================================================================
    //  OPERATOR HANDLING
    // ===================================================================

    /**
     * Called when the user taps +, −, ×, or ÷.
     *
     * If there is already a pending operation (the user pressed an
     * operator earlier), we evaluate it first (chaining), then store
     * the new operator.
     *
     * The expression line is updated to show e.g. "42 +".
     *
     * @param operator The operator symbol: "+", "−", "×", or "÷".
     */
    private void handleOperator(String operator) {
        // If the user is chaining operations (e.g. 5 + 3 × …),
        // evaluate the previous operation first so intermediate results
        // appear on screen.
        if (!currentOperator.isEmpty() && !isNewInput) {
            computeResult();
        }

        // Store the current display value as the first operand.
        firstOperand = parseCurrentInput();

        // Remember which operator was chosen.
        currentOperator = operator;

        // Show the expression so far (e.g. "42 +").
        tvExpression.setText(formatNumber(firstOperand) + " " + currentOperator);

        // Flag so the next digit starts a new number.
        isNewInput = true;
    }

    // ===================================================================
    //  EQUALS
    // ===================================================================

    /**
     * Evaluates the pending operation:
     *     result = firstOperand  (operator)  secondOperand
     *
     * The full expression is shown in the expression line
     * (e.g. "42 + 8 =") and the result in the main display.
     *
     * Division by zero produces the string "Error".
     */
    private void handleEquals() {
        // Nothing to do if no operator has been chosen yet.
        if (currentOperator.isEmpty()) {
            return;
        }

        // Capture the second operand from the current display.
        double secondOperand = parseCurrentInput();

        // Show the full expression: "firstOperand operator secondOperand ="
        tvExpression.setText(
                formatNumber(firstOperand) + " " +
                currentOperator + " " +
                formatNumber(secondOperand) + " ="
        );

        // Perform the actual arithmetic.
        computeResult();

        // After equals we clear the pending operator so tapping "="
        // again won't repeat the operation unexpectedly.
        currentOperator = "";
    }

    // ===================================================================
    //  ARITHMETIC ENGINE
    // ===================================================================

    /**
     * Performs the arithmetic dictated by {@link #currentOperator}:
     *
     *     firstOperand  (op)  secondOperand  →  result
     *
     * The result is written back into {@link #firstOperand} (so chained
     * operations work) and displayed on screen.
     *
     * Special case: division by zero sets the display to "Error" and
     * resets state so the calculator doesn't get stuck.
     */
    private void computeResult() {
        // The second operand is whatever is currently on screen.
        double secondOperand = parseCurrentInput();
        double result = 0;

        // Choose the right arithmetic operation.
        switch (currentOperator) {
            case "+":
                result = firstOperand + secondOperand;
                break;
            case "−":
                result = firstOperand - secondOperand;
                break;
            case "×":
                result = firstOperand * secondOperand;
                break;
            case "÷":
                // Guard against division by zero.
                if (secondOperand == 0) {
                    tvResult.setText("Error");
                    // Reset everything so the user can start fresh.
                    firstOperand    = 0;
                    currentOperator = "";
                    currentInput    = "0";
                    isNewInput      = true;
                    return;                       // ← bail out early
                }
                result = firstOperand / secondOperand;
                break;
        }

        // Store the result as the new first operand (for chaining).
        firstOperand = result;

        // Update the display and the internal input string.
        currentInput = formatNumber(result);
        tvResult.setText(currentInput);

        // Next digit tap should start a brand-new number.
        isNewInput = true;
    }

    // ===================================================================
    //  CLEAR (C)
    // ===================================================================

    /**
     * Resets the entire calculator back to its initial state:
     *   - First operand → 0
     *   - Operator → none
     *   - Current input → "0"
     *   - Display → "0"
     *   - Expression → blank
     */
    private void handleClear() {
        firstOperand    = 0;
        currentOperator = "";
        currentInput    = "0";
        isNewInput      = true;

        tvResult.setText("0");
        tvExpression.setText("");
    }

    // ===================================================================
    //  DECIMAL POINT
    // ===================================================================

    /**
     * Appends a decimal point to the current input.
     *
     * Rules:
     *   1. Only ONE decimal point per number is allowed.
     *   2. If we're starting fresh (isNewInput) begin with "0.".
     */
    private void handleDecimal() {
        if (isNewInput) {
            // Starting a new number with a decimal → "0."
            currentInput = "0.";
            isNewInput = false;
        } else if (!currentInput.contains(".")) {
            // Only add a dot if there isn't one already.
            currentInput += ".";
        }
        tvResult.setText(currentInput);
    }

    // ===================================================================
    //  PLUS / MINUS (±)
    // ===================================================================

    /**
     * Toggles the sign of the currently displayed number.
     *
     * If the value is positive it becomes negative (prepend "-") and
     * vice-versa (strip leading "-").  Zero is left unchanged.
     */
    private void handlePlusMinus() {
        if (currentInput.isEmpty() || currentInput.equals("0")) {
            return;     // No point toggling zero.
        }

        if (currentInput.startsWith("-")) {
            // Already negative → make positive by removing the minus.
            currentInput = currentInput.substring(1);
        } else {
            // Positive → make negative by prepending a minus.
            currentInput = "-" + currentInput;
        }

        tvResult.setText(currentInput);
    }

    // ===================================================================
    //  PERCENT (%)
    // ===================================================================

    /**
     * Divides the currently displayed number by 100.
     *
     * Example: if the display shows "42", tapping % changes it to "0.42".
     * This is how the standard iOS calculator percent button works.
     */
    private void handlePercent() {
        double value = parseCurrentInput();
        value /= 100.0;

        currentInput = formatNumber(value);
        tvResult.setText(currentInput);

        // Keep isNewInput as-is so the user can still chain operations.
    }

    // ===================================================================
    //  UTILITY METHODS
    // ===================================================================

    /**
     * Parses the {@link #currentInput} string into a double.
     *
     * If parsing fails for any reason (shouldn't happen with our guarded
     * input, but just in case) it returns 0.
     *
     * @return The numeric value of the current input.
     */
    private double parseCurrentInput() {
        try {
            return Double.parseDouble(currentInput);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Formats a double value into a human-friendly string:
     *   - No trailing zeros  (8.0 → "8")
     *   - Up to 10 decimal places for precision
     *
     * @param value The number to format.
     * @return A clean string representation.
     */
    private String formatNumber(double value) {
        return decimalFormat.format(value);
    }
}
