package com.labjournal.androidlab;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartUpdateListener {

    private DatabaseHelper dbHelper;
    private CartAdapter adapter;
    private Cursor cursor;
    
    private TextView tvSubtotal, tvTax, tvTotal;
    private Button btnCheckout;
    private double currentTotal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DatabaseHelper(this);
        
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvTax = findViewById(R.id.tv_tax);
        tvTotal = findViewById(R.id.tv_total);
        btnCheckout = findViewById(R.id.btn_checkout);
        
        RecyclerView rvCart = findViewById(R.id.rv_cart);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        
        loadCart();
        
        btnCheckout.setOnClickListener(v -> {
            if (currentTotal > 0) {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                intent.putExtra("TOTAL_AMOUNT", currentTotal);
                startActivity(intent);
                finish(); // Close cart since we are moving to checkout
            } else {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCart() {
        if (cursor != null) cursor.close();
        cursor = dbHelper.getCartItems();
        
        if (adapter == null) {
            adapter = new CartAdapter(this, cursor, this);
            ((RecyclerView) findViewById(R.id.rv_cart)).setAdapter(adapter);
        } else {
            adapter.swapCursor(cursor);
        }
        
        calculateReceipt();
    }
    
    private void calculateReceipt() {
        double subtotal = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int qty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                subtotal += (price * qty);
            } while (cursor.moveToNext());
        }
        
        if (subtotal == 0) {
            tvSubtotal.setText("$0.00");
            tvTax.setText("$0.00");
            tvTotal.setText("$0.00");
            currentTotal = 0;
            return;
        }
        
        double tax = subtotal * 0.08; // 8% tax
        double deliveryFee = 3.99;
        currentTotal = subtotal + tax + deliveryFee;
        
        tvSubtotal.setText(String.format("$%.2f", subtotal));
        tvTax.setText(String.format("$%.2f", tax));
        tvTotal.setText(String.format("$%.2f", currentTotal));
    }

    @Override
    public void onCartUpdated() {
        loadCart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) cursor.close();
    }
}
