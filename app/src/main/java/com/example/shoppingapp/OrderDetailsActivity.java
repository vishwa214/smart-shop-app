package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView tvOrderId, tvOrderDate, tvOrderTotal, tvPayment;
    ListView lvProducts;

    ShoppingDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        tvOrderId = findViewById(R.id.tv_order_id);
        tvOrderDate = findViewById(R.id.tv_order_date);
        tvOrderTotal = findViewById(R.id.tv_order_total);
        tvPayment = findViewById(R.id.tv_payment);   // 🔥 ADD THIS
        lvProducts = findViewById(R.id.lv_order_products);

        db = new ShoppingDatabase(this);

        int orderId = getIntent().getIntExtra("order_id", 0);
        String date = getIntent().getStringExtra("order_date");
        double total = getIntent().getDoubleExtra("order_total", 0);
        String payment = getIntent().getStringExtra("payment_method");

        tvOrderId.setText("Order ID: #" + orderId);
        tvOrderDate.setText("Date: " + date);
        tvOrderTotal.setText("Total: ₹ " + total);
        tvPayment.setText("Payment: " + payment);

        // 🔥 Get Ordered Products
        ArrayList<Products> orderedProducts =
                db.getOrderItems(orderId);

        // 🔥 Show Products
        PurchasesAdapter adapter =
                new PurchasesAdapter(orderedProducts, this, db);

        lvProducts.setAdapter(adapter);
    }
}
