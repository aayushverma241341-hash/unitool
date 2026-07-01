package com.labjournal.androidlab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CheckoutActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DatabaseHelper(this);
        totalAmount = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0.0);

        TextView tvAddress = findViewById(R.id.tv_checkout_address);
        tvAddress.setText(dbHelper.getUserAddress());

        TextView tvTotal = findViewById(R.id.tv_checkout_total);
        tvTotal.setText(String.format("Total: $%.2f", totalAmount));

        Button btnPlaceOrder = findViewById(R.id.btn_place_order);
        btnPlaceOrder.setOnClickListener(v -> {
            // Execute SQL Transaction
            long orderId = dbHelper.placeOrder(totalAmount);
            if (orderId != -1) {
                Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CheckoutActivity.this, OrderTrackingActivity.class);
                intent.putExtra("ORDER_ID", (int) orderId);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Order Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
