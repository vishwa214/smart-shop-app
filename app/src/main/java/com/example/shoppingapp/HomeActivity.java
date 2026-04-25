package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bnv;
    RecyclerView rv;
    RecyclerView rvCategories;
    Button btnTodayOffer;
    TextView tvWelcome;

    public static final String PRODUCT_KEY = "product_key";
    public static final String TABLE_NAME_KEY = "table_key";

    SharedPreferences shp, shp_id;
    SharedPreferences.Editor shpEditor, shpEditor_id;

    public static boolean flag;
    ShoppingDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        flag = false;
        db = new ShoppingDatabase(this);

        tvWelcome = findViewById(R.id.tv_welcome);
        btnTodayOffer = findViewById(R.id.btn_today_offer);
        rv = findViewById(R.id.rv_home);
        rvCategories = findViewById(R.id.rv_categories);
        bnv = findViewById(R.id.BottomNavigationView);

        shp = getSharedPreferences("myPreferences", MODE_PRIVATE);
        shp_id = getSharedPreferences("Preferences_id", MODE_PRIVATE);

        // 🔥 Welcome Username
        int userId = shp_id.getInt("user_id", 0);
        if(userId != 0){
            Users user = db.getUser(userId);
            if(user != null){
                tvWelcome.setText("Welcome, " + user.getFullName() + " 👋");
            }
        }

        // 🔥 Today Offer Button
        btnTodayOffer.setOnClickListener(v -> {

            ArrayList<Products> mobileList =
                    db.getAllProducts(ShoppingDatabase.TB_MOBILE);

            if (mobileList != null && mobileList.size() > 0) {

                Products iphone = mobileList.get(0);

                Intent i = new Intent(HomeActivity.this,
                        DisplayProductsActivity.class);

                i.putExtra(PRODUCT_KEY, iphone.getId());
                i.putExtra(TABLE_NAME_KEY,
                        ShoppingDatabase.TB_MOBILE);

                flag = true;
                startActivity(i);

            } else {
                Toast.makeText(HomeActivity.this,
                        "No Mobile Products Available",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 🔥 Categories Section
        ArrayList<Category> categories = new ArrayList<>();

        categories.add(new Category(R.drawable.tshirt,
                "Fashion",
                ShoppingDatabase.TB_FASHION));


        categories.add(new Category(R.drawable.mobile,
                "Mobile",
                ShoppingDatabase.TB_MOBILE));

        categories.add(new Category(R.drawable.laptop,
                "Laptop",
                ShoppingDatabase.TB_LAPTOP));

        categories.add(new Category(R.drawable.book,
                "Books",
                ShoppingDatabase.TB_BOOK));

        categories.add(new Category(R.drawable.game,
                "Games",
                ShoppingDatabase.TB_GAME));

        CategoryAdapter categoryAdapter =
                new CategoryAdapter(this, categories);

        rvCategories.setLayoutManager(
                new LinearLayoutManager(this,
                        RecyclerView.HORIZONTAL,
                        false));

        rvCategories.setAdapter(categoryAdapter);

        // 🔥 Discount Products Section
        ArrayList<Products> discountProducts =
                db.getAllProducts(ShoppingDatabase.TB_PRODUCT_DISCOUNT);

        HomeAdabter adapter = new HomeAdabter(discountProducts,
                productId -> {

                    Intent i = new Intent(getBaseContext(),
                            DisplayProductsActivity.class);

                    i.putExtra(PRODUCT_KEY, productId);
                    i.putExtra(TABLE_NAME_KEY,
                            ShoppingDatabase.TB_PRODUCT_DISCOUNT);

                    flag = true;
                    startActivity(i);
                });

        rv.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL,false));

        rv.setAdapter(adapter);

        // 🔥 Bottom Navigation
        bnv.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.home:
                    return true;

                case R.id.products:
                    startActivity(new Intent(this, MainActivity.class));
                    return true;

                case R.id.profile:
                    startActivity(new Intent(this, ProfileActivity.class));
                    return true;

                case R.id.basket:
                    startActivity(new Intent(this, PurchasesActivity.class));
                    return true;
            }
            return false;
        });

        // 🔥 Update Cart Badge
        updateCartBadge();
    }

    // 🛒 Cart Badge Method
    private void updateCartBadge(){

        int count = db.getCartCount();

        if(count > 0){
            BadgeDrawable badge = bnv.getOrCreateBadge(R.id.basket);
            badge.setVisible(true);
            badge.setNumber(count);
        } else {
            bnv.removeBadge(R.id.basket);
        }
    }

    // 🔄 Auto Refresh When Coming Back
    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_up_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.logout){
            logout();
        } else if(item.getItemId() == R.id.settings){
            startActivity(new Intent(this, SettingActivity.class));
        }

        return true;
    }

    private void logout() {

        shpEditor = shp.edit();
        shpEditor.putInt("user", 0);
        shpEditor.apply();

        shpEditor_id = shp_id.edit();
        shpEditor_id.putInt("user_id", 0);
        shpEditor_id.apply();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
