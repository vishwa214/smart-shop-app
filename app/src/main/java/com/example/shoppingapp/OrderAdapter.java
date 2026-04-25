package com.example.shoppingapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    ArrayList<Order> orderList;

    public OrderAdapter(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        Order order = orderList.get(position);

        holder.tvOrderId.setText("Order #" + order.getId());

        // Convert timestamp to readable date
        long millis = Long.parseLong(order.getDate());

        String formattedDate = new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault())
                .format(new Date(millis));

        holder.tvOrderDate.setText(formattedDate);

        holder.tvOrderTotal.setText("₹ " + order.getTotal());

        holder.tvPayment.setText("Payment: " + order.getPaymentMethod());

        // 🔥 Open Order Details Screen
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    OrderDetailsActivity.class);

            intent.putExtra("order_id", order.getId());
            intent.putExtra("order_date", formattedDate);
            intent.putExtra("order_total", order.getTotal());
            intent.putExtra("payment_method", order.getPaymentMethod());

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderId, tvOrderDate, tvOrderTotal, tvPayment;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderTotal = itemView.findViewById(R.id.tv_order_total);
            tvPayment = itemView.findViewById(R.id.tv_payment_method);
        }
    }
}
