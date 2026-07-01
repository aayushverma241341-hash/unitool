package com.labjournal.androidlab;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.text.DecimalFormat;

public class ScientificCalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvExpression, tvResult;
    private String currentInput = "";
    private String operator = "";
    private double firstOperand = 0;
    private boolean isOperatorClicked = false;
    private final DecimalFormat format = new DecimalFormat("0.######");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scientific_calculator);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvExpression = findViewById(R.id.tv_expression);
        tvResult = findViewById(R.id.tv_result);

        int[] buttonIds = {
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
            R.id.btn_decimal, R.id.btn_add, R.id.btn_subtract, R.id.btn_multiply, R.id.btn_divide,
            R.id.btn_clear, R.id.btn_equals, R.id.btn_sin, R.id.btn_cos, R.id.btn_tan,
            R.id.btn_log, R.id.btn_ln, R.id.btn_sqrt, R.id.btn_power, R.id.btn_pi
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        
        if (id == R.id.btn_clear) {
            currentInput = "";
            operator = "";
            firstOperand = 0;
            tvResult.setText("0");
            tvExpression.setText("");
            isOperatorClicked = false;
        } else if (id == R.id.btn_decimal) {
            if (!currentInput.contains(".")) {
                currentInput += ".";
                tvResult.setText(currentInput);
            }
        } else if (id == R.id.btn_pi) {
            currentInput = String.valueOf(Math.PI);
            tvResult.setText(format.format(Math.PI));
        } else if (id == R.id.btn_add || id == R.id.btn_subtract || id == R.id.btn_multiply || id == R.id.btn_divide || id == R.id.btn_power) {
            handleOperator(((Button) v).getText().toString());
        } else if (id == R.id.btn_equals) {
            calculateResult();
        } else if (id == R.id.btn_sin || id == R.id.btn_cos || id == R.id.btn_tan || id == R.id.btn_log || id == R.id.btn_ln || id == R.id.btn_sqrt) {
            handleScientificFunction(((Button) v).getText().toString());
        } else {
            // Numbers
            if (isOperatorClicked) {
                currentInput = "";
                isOperatorClicked = false;
            }
            currentInput += ((Button) v).getText().toString();
            tvResult.setText(currentInput);
        }
    }

    private void handleOperator(String op) {
        if (!currentInput.isEmpty()) {
            firstOperand = Double.parseDouble(currentInput);
        } else {
            firstOperand = Double.parseDouble(tvResult.getText().toString());
        }
        operator = op;
        tvExpression.setText(format.format(firstOperand) + " " + operator);
        isOperatorClicked = true;
    }

    private void handleScientificFunction(String func) {
        double value = 0;
        try {
            value = Double.parseDouble(tvResult.getText().toString());
        } catch (NumberFormatException ignored) {}

        double result = 0;
        switch (func) {
            case "sin": result = Math.sin(Math.toRadians(value)); break;
            case "cos": result = Math.cos(Math.toRadians(value)); break;
            case "tan": result = Math.tan(Math.toRadians(value)); break;
            case "log": result = Math.log10(value); break;
            case "ln": result = Math.log(value); break;
            case "√": result = Math.sqrt(value); break;
        }
        
        tvExpression.setText(func + "(" + format.format(value) + ")");
        currentInput = String.valueOf(result);
        tvResult.setText(format.format(result));
    }

    private void calculateResult() {
        if (operator.isEmpty()) return;

        double secondOperand = 0;
        if (!currentInput.isEmpty()) {
            secondOperand = Double.parseDouble(currentInput);
        }
        
        double result = 0;
        boolean error = false;

        switch (operator) {
            case "+": result = firstOperand + secondOperand; break;
            case "−": result = firstOperand - secondOperand; break;
            case "×": result = firstOperand * secondOperand; break;
            case "÷": 
                if (secondOperand == 0) {
                    error = true;
                } else {
                    result = firstOperand / secondOperand;
                }
                break;
            case "x^y": result = Math.pow(firstOperand, secondOperand); break;
        }

        if (error) {
            tvResult.setText("Error");
            currentInput = "";
        } else {
            tvExpression.setText(format.format(firstOperand) + " " + operator + " " + format.format(secondOperand) + " =");
            tvResult.setText(format.format(result));
            currentInput = String.valueOf(result);
        }
        
        operator = "";
        isOperatorClicked = true;
    }
}
