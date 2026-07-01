package com.labjournal.androidlab;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private Cursor cursor;

    public OrderHistoryAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        long dateMillis = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("order_date")));
        double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total_amount"));
        String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

        holder.tvOrderId.setText("Order #" + (1000 + id));
        holder.tvTotal.setText(String.format("$%.2f", total));
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault());
        holder.tvDate.setText("Placed: " + sdf.format(new Date(dateMillis)));
        
        holder.tvStatus.setText("Status: " + status);
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate, tvTotal, tvStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvDate = itemView.findViewById(R.id.tv_order_date);
            tvTotal = itemView.findViewById(R.id.tv_order_total);
            tvStatus = itemView.findViewById(R.id.tv_order_status);
        }
    }
}
