package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {

    RecyclerView rvOrders;
    ShoppingDatabase db;
    SharedPreferences shp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        rvOrders = findViewById(R.id.rv_orders);

        db = new ShoppingDatabase(this);

        shp_id = getSharedPreferences("Preferences_id", MODE_PRIVATE);
        int userId = shp_id.getInt("user_id", 0);

        if(userId == 0){
            Toast.makeText(this,
                    "User not found",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Order> orders = db.getUserOrders(userId);

        OrderAdapter adapter = new OrderAdapter(orders);

        rvOrders.setLayoutManager(
                new LinearLayoutManager(this));

        rvOrders.setAdapter(adapter);
    }
}
