package com.example.shoppingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PurchasesAdapter extends BaseAdapter {

    ArrayList<Products> purchases;
    Context context;
    ShoppingDatabase db;

    public PurchasesAdapter(ArrayList<Products> purchases, Context context, ShoppingDatabase db) {
        this.purchases = purchases;
        this.context = context;
        this.db = db;
    }

    @Override
    public int getCount() {
        return purchases.size();
    }

    @Override
    public Products getItem(int position) {
        return purchases.get(position);
    }

    @Override
    public long getItemId(int position) {
        return purchases.get(position).getId();
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View v = view;
        if (v == null) {
            v = LayoutInflater.from(context)
                    .inflate(R.layout.custome_purchases_products, parent, false);
        }

        ImageView img = v.findViewById(R.id.img_products_purchases);
        TextView tv_name = v.findViewById(R.id.tv_name_purchases);
        TextView tv_price = v.findViewById(R.id.tv_price_purchases);
        TextView tv_brand = v.findViewById(R.id.tv_brand_purchases);
        RatingBar rating = v.findViewById(R.id.rating_purchases);
        TextView tv_quantity = v.findViewById(R.id.tv_quantity);
        Button btnRemove = v.findViewById(R.id.btn_remove);

        Products p = purchases.get(position);

        if (p.getImage() != 0) {
            img.setImageResource(p.getImage());
        } else {
            img.setImageResource(R.drawable.products);
        }

        tv_name.setText(p.getName());
        tv_price.setText(p.getPrice() + "₹");
        tv_brand.setText(p.getBrand());
        rating.setRating(p.getRating());
        tv_quantity.setText(String.valueOf(p.getQuantity()));

        // 🔥 REMOVE BUTTON CLICK
        btnRemove.setOnClickListener(v1 -> {

            if (db.deletePurchaseItem(p.getId())) {

                purchases.remove(position);
                notifyDataSetChanged();

                // 🔥 UPDATE TOTAL AFTER REMOVE
                if (context instanceof PurchasesActivity) {
                    ((PurchasesActivity) context).updateTotal();
                }

                Toast.makeText(context,
                        "Item Removed Successfully",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
