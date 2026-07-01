package com.labjournal.androidlab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class StoreWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide ActionBar for Splash Screen effect
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        
        setContentView(R.layout.activity_store_welcome);

        Button btnEnterStore = findViewById(R.id.btn_enter_store);
        btnEnterStore.setOnClickListener(v -> {
            startActivity(new Intent(StoreWelcomeActivity.this, StoreHubActivity.class));
            finish(); // Close welcome screen so user doesn't back into it
        });
    }
}
