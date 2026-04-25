package com.example.shoppingapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PurchasesActivity extends AppCompatActivity {
    private static final String MERCHANT_UPI = "8185984986@fam";

    ListView lv;
    TextView tvTotal;
    Button btnCheckout;
    RadioGroup rgPayment;

    PurchasesAdapter pa;
    ShoppingDatabase db;
    ArrayList<Products> p;

    double total;
    int userId;
    String selectedPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);

        lv = findViewById(R.id.lv_purchases);
        tvTotal = findViewById(R.id.tv_total_price);
        btnCheckout = findViewById(R.id.btn_checkout);
        rgPayment = findViewById(R.id.rg_payment);

        db = new ShoppingDatabase(this);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Checkout");
        }

        loadData();

        btnCheckout.setOnClickListener(v -> {

            if(p == null || p.isEmpty()){
                Toast.makeText(this,
                        "Cart is Empty!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedId = rgPayment.getCheckedRadioButtonId();

            if(selectedId == -1){
                Toast.makeText(this,
                        "Please select a payment method",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedButton = findViewById(selectedId);
            selectedPayment = selectedButton.getText().toString();

            total = db.getTotalPurchasesPrice();

            SharedPreferences shp_id =
                    getSharedPreferences("Preferences_id", MODE_PRIVATE);

            userId = shp_id.getInt("user_id", 0);

            if(userId == 0){
                Toast.makeText(this,
                        "User not found!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔥 If UPI → Open UPI App
            if(selectedPayment.equalsIgnoreCase("UPI")){
                startUpiPayment(total);
            } else {
                // Cash / Card
                completeOrder(selectedPayment);
            }

        });
    }

    // 🔥 REAL UPI INTENT
    private void startUpiPayment(double amount) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", MERCHANT_UPI)
                .appendQueryParameter("pn", "Smart Shop")
                .appendQueryParameter("tn", "Order Payment")
                .appendQueryParameter("am", String.valueOf(amount))
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiIntent = new Intent(Intent.ACTION_VIEW);
        upiIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiIntent, "Pay with");

        if (chooser.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooser, 101);
        } else {
            Toast.makeText(this,
                    "No UPI app found!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // 🔥 Handle UPI Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {

            if (data != null) {

                String response = data.getStringExtra("response");

                if (response != null &&
                        response.toLowerCase().contains("success")) {

                    Toast.makeText(this,
                            "Payment Successful",
                            Toast.LENGTH_SHORT).show();

                    completeOrder("UPI");

                } else {
                    Toast.makeText(this,
                            "Payment Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 🔥 Save Order in Database
    private void completeOrder(String paymentMethod){

        int orderId = db.insertOrder(userId, total, paymentMethod);

        if(orderId > 0){

            for(Products product : p){
                db.insertOrderItem(orderId, product);
            }

            db.clearCart();

            Intent intent = new Intent(
                    PurchasesActivity.this,
                    OrderSuccessActivity.class);

            intent.putExtra("order_id", orderId);
            intent.putExtra("payment_method", paymentMethod);

            startActivity(intent);
            finish();

        } else {

            Toast.makeText(this,
                    "Order Failed!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData(){
        p = db.getAllProductsInPurchases();
        pa = new PurchasesAdapter(p, this, db);
        lv.setAdapter(pa);
        updateTotal();
    }

    public void updateTotal(){
        double total = db.getTotalPurchasesPrice();
        tvTotal.setText("Total: ₹ " + total);
    }
}
