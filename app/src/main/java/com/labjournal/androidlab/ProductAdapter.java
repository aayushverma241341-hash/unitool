package com.labjournal.androidlab;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private Cursor cursor;
    private DatabaseHelper dbHelper;

    public ProductAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        String icon = cursor.getString(cursor.getColumnIndexOrThrow("icon"));

        holder.tvName.setText(name);
        holder.tvDesc.setText(desc);
        holder.tvPrice.setText(String.format("$%.2f", price));
        holder.tvIcon.setText(icon);

        // Fast Add to Cart
        holder.btnFastAdd.setOnClickListener(v -> {
            dbHelper.addToCart(id, 1);
            Toast.makeText(context, name + " added to cart", Toast.LENGTH_SHORT).show();
        });

        // Click whole item to go to ProductDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvPrice, tvIcon;
        Button btnFastAdd;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvDesc = itemView.findViewById(R.id.tv_item_desc);
            tvPrice = itemView.findViewById(R.id.tv_item_price);
            tvIcon = itemView.findViewById(R.id.tv_item_icon);
            btnFastAdd = itemView.findViewById(R.id.btn_fast_add);
        }
    }
}
