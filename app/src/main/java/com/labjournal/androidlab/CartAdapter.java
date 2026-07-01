package com.labjournal.androidlab;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private Cursor cursor;
    private DatabaseHelper dbHelper;
    private CartUpdateListener listener;

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public CartAdapter(Context context, Cursor cursor, CartUpdateListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        int cartId = cursor.getInt(cursor.getColumnIndexOrThrow("cart_id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        int qty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        String icon = cursor.getString(cursor.getColumnIndexOrThrow("icon"));

        holder.tvName.setText(name);
        holder.tvIcon.setText(icon);
        holder.tvPriceQty.setText(String.format("$%.2f x %d", price, qty));
        
        double itemTotal = price * qty;
        holder.tvItemTotal.setText(String.format("$%.2f", itemTotal));

        holder.btnRemove.setOnClickListener(v -> {
            dbHelper.removeCartItem(cartId);
            listener.onCartUpdated(); // Notify Activity to refresh totals and list
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

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPriceQty, tvItemTotal, tvIcon;
        Button btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_cart_name);
            tvPriceQty = itemView.findViewById(R.id.tv_cart_price_qty);
            tvItemTotal = itemView.findViewById(R.id.tv_cart_item_total);
            tvIcon = itemView.findViewById(R.id.tv_cart_icon);
            btnRemove = itemView.findViewById(R.id.btn_cart_remove);
        }
    }
}
