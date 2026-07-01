package com.labjournal.androidlab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class OrderTrackingActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int orderId;
    private int currentStage = 0;
    
    private TextView tvStatus, tvDesc, tvIcon;
    private ProgressBar progressBar;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DatabaseHelper(this);
        orderId = getIntent().getIntExtra("ORDER_ID", -1);

        tvStatus = findViewById(R.id.tv_tracking_status);
        tvDesc = findViewById(R.id.tv_tracking_desc);
        tvIcon = findViewById(R.id.tv_tracking_icon);
        progressBar = findViewById(R.id.progress_tracking);
        
        Button btnBack = findViewById(R.id.btn_back_to_hub);
        btnBack.setOnClickListener(v -> finish());

        simulateLiveTracking();
    }

    private void simulateLiveTracking() {
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                currentStage++;
                progressBar.setProgress(currentStage);
                
                String statusStr = "Received";
                
                if (currentStage == 1) {
                    statusStr = "Preparing";
                    tvStatus.setText("Order is being prepared");
                    tvDesc.setText("The store is getting your items ready.");
                    tvIcon.setText("👨‍🍳");
                } else if (currentStage == 2) {
                    statusStr = "Out for Delivery";
                    tvStatus.setText("Out for Delivery");
                    tvDesc.setText("Your order is on the way!");
                    tvIcon.setText("🛵");
                } else if (currentStage == 3) {
                    statusStr = "Delivered";
                    tvStatus.setText("Delivered!");
                    tvDesc.setText("Enjoy your items.");
                    tvIcon.setText("🎉");
                    dbHelper.updateOrderStatus(orderId, statusStr);
                    return; // Stop simulating
                }
                
                dbHelper.updateOrderStatus(orderId, statusStr);
                
                // Advance every 4 seconds for simulation
                handler.postDelayed(this, 4000);
            }
        };
        
        // Start simulation after 2 seconds
        handler.postDelayed(runnable, 2000);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
