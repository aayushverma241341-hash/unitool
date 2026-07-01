package com.labjournal.androidlab;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductDetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int productId;
    private int quantity = 1;
    private double unitPrice = 0.0;

    private TextView tvQty, tvDetailName, tvDetailDesc, tvDetailPrice, tvDetailIcon;
    private Button btnAddToCart;
    private FloatingActionButton fabFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DatabaseHelper(this);
        productId = getIntent().getIntExtra("PRODUCT_ID", -1);

        tvDetailName = findViewById(R.id.tv_detail_name);
        tvDetailDesc = findViewById(R.id.tv_detail_desc);
        tvDetailPrice = findViewById(R.id.tv_detail_price);
        tvDetailIcon = findViewById(R.id.tv_detail_icon);
        tvQty = findViewById(R.id.tv_qty);
        btnAddToCart = findViewById(R.id.btn_add_to_cart_detail);
        fabFav = findViewById(R.id.fab_favorite);
        Button btnMinus = findViewById(R.id.btn_qty_minus);
        Button btnPlus = findViewById(R.id.btn_qty_plus);

        loadProductDetails();
        updateFavoriteIcon();

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityAndTotal();
            }
        });

        btnPlus.setOnClickListener(v -> {
            quantity++;
            updateQuantityAndTotal();
        });

        fabFav.setOnClickListener(v -> {
            boolean isFav = dbHelper.toggleFavorite(productId);
            if (isFav) {
                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
            }
            updateFavoriteIcon();
        });

        btnAddToCart.setOnClickListener(v -> {
            dbHelper.addToCart(productId, quantity);
            Toast.makeText(this, "Added " + quantity + " items to cart", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadProductDetails() {
        if (productId == -1) return;
        Cursor c = dbHelper.getProductById(productId);
        if (c.moveToFirst()) {
            String name = c.getString(c.getColumnIndexOrThrow("name"));
            String desc = c.getString(c.getColumnIndexOrThrow("description"));
            unitPrice = c.getDouble(c.getColumnIndexOrThrow("price"));
            String icon = c.getString(c.getColumnIndexOrThrow("icon"));

            tvDetailName.setText(name);
            tvDetailDesc.setText(desc);
            tvDetailPrice.setText(String.format("$%.2f", unitPrice));
            tvDetailIcon.setText(icon);
            updateQuantityAndTotal();
        }
        c.close();
    }

    private void updateQuantityAndTotal() {
        tvQty.setText(String.valueOf(quantity));
        btnAddToCart.setText(String.format("Add to Cart - $%.2f", unitPrice * quantity));
    }

    private void updateFavoriteIcon() {
        if (dbHelper.isFavorite(productId)) {
            fabFav.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            fabFav.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }
}
