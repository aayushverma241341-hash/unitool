package com.labjournal.androidlab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class StoreHubActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_hub);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DatabaseHelper(this);

        TextView tvDeliveryAddress = findViewById(R.id.tv_delivery_address);
        tvDeliveryAddress.setText("Deliver to: " + dbHelper.getUserAddress());

        CardView cardFood = findViewById(R.id.card_order_food);
        CardView cardGrocery = findViewById(R.id.card_order_grocery);
        Button btnCart = findViewById(R.id.btn_view_cart);
        Button btnHistory = findViewById(R.id.btn_order_history);
        Button btnFavs = findViewById(R.id.btn_favorites);

        cardFood.setOnClickListener(v -> {
            Intent intent = new Intent(StoreHubActivity.this, CatalogActivity.class);
            intent.putExtra("CATEGORY", "food");
            startActivity(intent);
        });

        cardGrocery.setOnClickListener(v -> {
            Intent intent = new Intent(StoreHubActivity.this, CatalogActivity.class);
            intent.putExtra("CATEGORY", "grocery");
            startActivity(intent);
        });

        btnCart.setOnClickListener(v -> startActivity(new Intent(StoreHubActivity.this, CartActivity.class)));
        btnHistory.setOnClickListener(v -> startActivity(new Intent(StoreHubActivity.this, OrderHistoryActivity.class)));
        btnFavs.setOnClickListener(v -> startActivity(new Intent(StoreHubActivity.this, FavoritesActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update cart button count if needed
        Button btnCart = findViewById(R.id.btn_view_cart);
        int cartCount = dbHelper.getCartItemCount();
        btnCart.setText("View Active Cart (" + cartCount + " items)");
    }
}
