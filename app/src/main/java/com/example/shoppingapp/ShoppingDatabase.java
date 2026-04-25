package com.example.shoppingapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ShoppingDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "shopping_db";
    public static final int DB_VERSION = 8; // 🔥 UPDATED

    public static final String TB_FASHION = "fashion";
    public static final String TB_BOOK = "book";
    public static final String TB_BEAUTY = "beauty";
    public static final String TB_ELECTRICS = "electrics";
    public static final String TB_GAME = "game";
    public static final String TB_HOME_COOKER = "homeCooker";
    public static final String TB_LAPTOP = "laptop";
    public static final String TB_MOBILE = "mobile";
    public static final String TB_SPORTS = "sports";
    public static final String TB_CAR_TOOL = "carTools";
    public static final String TB_USERS = "users";
    public static final String TB_PURCHASES = "purchases";
    public static final String TB_PRODUCT_DISCOUNT = "product_discount";
    // 🔥 ORDERS TABLE
    public static final String TB_ORDERS = "orders";
    public static final String TB_CLM_ORDER_DATE = "order_date";
    public static final String TB_CLM_ORDER_TOTAL = "order_total";

    public static final String TB_CLM_ID = "id";
    public static final String TB_CLM_IMAGE = "image";
    public static final String TB_CLM_NAME = "name";
    public static final String TB_CLM_PRICE = "price";
    public static final String TB_CLM_BRAND = "brand";
    public static final String TB_CLM_PIECES = "pieces";
    public static final String TB_CLM_DESCRIPTION = "description";
    public static final String TB_CLM_DISCOUNT = "discount";
    public static final String TB_CLM_RATING = "rating";
    public static final String TB_CLM_QUANTITY = "quantity";

    public static final String TB_CLM_USER_ID = "user_id";
    public static final String TB_CLM_USER_NAME = "user_name";
    public static final String TB_CLM_USER_FULL_NAME = "full_name";
    public static final String TB_CLM_USER_PASSWORD = "user_password";
    public static final String TB_CLM_USER_EMAIL = "user_email";
    public static final String TB_CLM_USER_PHONE = "user_phone";
    public static final String TB_CLM_USER_IMAGE = "user_image";
    // 🔥 ORDER ITEMS TABLE
    public static final String TB_ORDER_ITEMS = "order_items";
    public static final String TB_CLM_ORDER_ID = "order_id";
    public static final String TB_CLM_PAYMENT_METHOD = "payment_method";

    public ShoppingDatabase(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(createTables(TB_FASHION));
        sqLiteDatabase.execSQL(createTables(TB_BOOK));
        sqLiteDatabase.execSQL(createTables(TB_BEAUTY));
        sqLiteDatabase.execSQL(createTables(TB_ELECTRICS));
        sqLiteDatabase.execSQL(createTables(TB_GAME));
        sqLiteDatabase.execSQL(createTables(TB_HOME_COOKER));
        sqLiteDatabase.execSQL(createTables(TB_LAPTOP));
        sqLiteDatabase.execSQL(createTables(TB_MOBILE));
        sqLiteDatabase.execSQL(createTables(TB_SPORTS));
        sqLiteDatabase.execSQL(createTables(TB_CAR_TOOL));
        sqLiteDatabase.execSQL(createTables(TB_PRODUCT_DISCOUNT));

        sqLiteDatabase.execSQL("CREATE TABLE "+TB_USERS+" ("+TB_CLM_USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+TB_CLM_USER_NAME+" TEXT UNIQUE , "+
                TB_CLM_USER_FULL_NAME+" TEXT , "+TB_CLM_USER_PASSWORD+" TEXT , "+TB_CLM_USER_EMAIL+" TEXT UNIQUE , "+TB_CLM_USER_PHONE+" TEXT , "+TB_CLM_USER_IMAGE+" TEXT );");

        sqLiteDatabase.execSQL("CREATE TABLE "+TB_PURCHASES+" ("+TB_CLM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+TB_CLM_IMAGE+" INTEGER , "+
                TB_CLM_NAME+" TEXT , "+TB_CLM_PRICE+" REAL , "+TB_CLM_BRAND+" TEXT , "+TB_CLM_RATING+" REAL , "+TB_CLM_QUANTITY+" INTEGER );");
// 🔥 CREATE ORDERS TABLE
        sqLiteDatabase.execSQL("CREATE TABLE " + TB_ORDERS + " (" +
                TB_CLM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TB_CLM_USER_ID + " INTEGER, " +
                TB_CLM_ORDER_DATE + " TEXT, " +
                TB_CLM_ORDER_TOTAL + " REAL, " +
                "payment_method TEXT)");

// 🔥 CREATE ORDER ITEMS TABLE
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TB_ORDER_ITEMS + " (" +
                        TB_CLM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TB_CLM_ORDER_ID + " INTEGER, " +
                        TB_CLM_IMAGE + " INTEGER, " +
                        TB_CLM_NAME + " TEXT, " +
                        TB_CLM_PRICE + " REAL, " +
                        TB_CLM_QUANTITY + " INTEGER)"
        );
        // 🔥 INSERT DEFAULT FASHION PRODUCT
        // 🔥 INSERT DEFAULT PRODUCTS FOR ALL CATEGORIES
        insertDefaultFashionProduct(sqLiteDatabase);
        insertDefaultBookProduct(sqLiteDatabase);
        insertDefaultBeautyProduct(sqLiteDatabase);
        insertDefaultElectronicsProduct(sqLiteDatabase);
        insertDefaultGameProduct(sqLiteDatabase);
        insertDefaultHomeCookerProduct(sqLiteDatabase);
        insertDefaultLaptopProduct(sqLiteDatabase);
        insertDefaultMobileProduct(sqLiteDatabase);
        insertDefaultSportsProduct(sqLiteDatabase);
        insertDefaultCarToolProduct(sqLiteDatabase);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TB_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_PURCHASES);
        db.execSQL("DROP TABLE IF EXISTS " + TB_USERS);

        // Optional: Drop category tables also if needed
        db.execSQL("DROP TABLE IF EXISTS " + TB_FASHION);
        db.execSQL("DROP TABLE IF EXISTS " + TB_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TB_BEAUTY);
        db.execSQL("DROP TABLE IF EXISTS " + TB_ELECTRICS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_GAME);
        db.execSQL("DROP TABLE IF EXISTS " + TB_HOME_COOKER);
        db.execSQL("DROP TABLE IF EXISTS " + TB_LAPTOP);
        db.execSQL("DROP TABLE IF EXISTS " + TB_MOBILE);
        db.execSQL("DROP TABLE IF EXISTS " + TB_SPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_CAR_TOOL);
        db.execSQL("DROP TABLE IF EXISTS " + TB_PRODUCT_DISCOUNT);

        onCreate(db);
    }

    public String createTables(String tableName){
        return "CREATE TABLE "+tableName+" ("+TB_CLM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+TB_CLM_IMAGE+" INTEGER , "+
                TB_CLM_NAME+" TEXT , "+TB_CLM_PRICE+" REAL , "+TB_CLM_BRAND+" TEXT , "+TB_CLM_PIECES+" INTEGER , "+
                TB_CLM_DESCRIPTION+" TEXT , "+TB_CLM_DISCOUNT+" REAL) ; ";
    }

    // 🔥 ADDED METHOD
    private void insertDefaultFashionProduct(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(TB_CLM_IMAGE, R.drawable.tshirt);
        values.put(TB_CLM_NAME, "Black Formal Shirt");
        values.put(TB_CLM_PRICE, 1);
        values.put(TB_CLM_BRAND, "Snitch");
        values.put(TB_CLM_PIECES, 1);
        values.put(TB_CLM_DESCRIPTION, "Premium slim fit black formal shirt for men");
        values.put(TB_CLM_DISCOUNT, 15);
        db.insert(TB_FASHION, null, values);
    }
    // 🔥 NEW ELECTRONICS PRODUCT
    private void insertDefaultElectronicsProduct(SQLiteDatabase db){

        ContentValues values = new ContentValues();
        values.put(TB_CLM_IMAGE, R.drawable.homecooker); // Add image in drawable
        values.put(TB_CLM_NAME, "Wireless Bluetooth Headphones");
        values.put(TB_CLM_PRICE, 1);
        values.put(TB_CLM_BRAND, "Boat");
        values.put(TB_CLM_PIECES, 1);
        values.put(TB_CLM_DESCRIPTION, "High quality wireless headphones with deep bass and 20h battery life");
        values.put(TB_CLM_DISCOUNT, 20);

        db.insert(TB_ELECTRICS, null, values);
    }
    private void insertDefaultBookProduct(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(TB_CLM_IMAGE, R.drawable.book);
        values.put(TB_CLM_NAME, "Atomic Habits");
        values.put(TB_CLM_PRICE, 1);
        values.put(TB_CLM_BRAND, "James Clear");
        values.put(TB_CLM_PIECES, 1);
        values.put(TB_CLM_DESCRIPTION, "Best selling self improvement book");
        values.put(TB_CLM_DISCOUNT, 10);
        db.insert(TB_BOOK, null, values);
    }
    private void insertDefaultBeautyProduct(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(TB_CLM_IMAGE, R.drawable.beauty);
        values.put(TB_CLM_NAME, "Lakme Face Cream");
        values.put(TB_CLM_PRICE, 1);
        values.put(TB_CLM_BRAND, "Lakme");
        values.put(TB_CLM_PIECES, 1);
        values.put(TB_CLM_DESCRIPTION, "Daily glow moisturizing cream");
        values.put(TB_CLM_DISCOUNT, 5);
        db.insert(TB_BEAUTY, null, values);
    }
    private void insertDefaultGameProduct(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(TB_CLM_IMAGE, R.drawable.game);
        values.put(TB_CLM_NAME, "PS5 Controller");
        values.put(TB_CLM_PRICE, 1);
        values.put(TB_CLM_BRAND, "Sony");
        values.put(TB_CLM_PIECES, 1);
        values.put(TB_CLM_DESCRIPTION, "DualSense wireless controller");
        values.put(TB_CLM_DISCOUNT, 12);
        db.insert(TB_GAME, null, values);
    }
    private void insertDefaultHomeCookerProduct(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(TB_CLM_IMAGE, R.drawable.homecooker);
        values.put(TB_CLM_NAME, "Prestige Pressure Cooker");
        values.put(TB_CLM_PRICE, 1);
        values.put(TB_CLM_BRAND, "Prestige");
        values.put(TB_CLM_PIECES, 1);
        values.put(TB_CLM_DESCRIPTION, "3L stainless steel pressure cooker");
        values.put(TB_CLM_DISCOUNT, 18);
        db.insert(TB_HOME_COOKER, null, values);
    }
    private void insertDefaultLaptopProduct(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(TB_CLM_IMAGE, R.drawable.laptop);
        values.put(TB_CLM_NAME, "HP Pavilion Laptop");
        values.put(TB_CLM_PRICE, 1);
        values.put(TB_CLM_BRAND, "HP");
        values.put(TB_CLM_PIECES, 1);
        values.put(TB_CLM_DESCRIPTION, "i5 12th Gen, 16GB RAM, 512GB SSD");
        values.put(TB_CLM_DISCOUNT, 15);
        db.insert(TB_LAPTOP, null, values);
    }
    private void insertDefaultMobileProduct(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(TB_CLM_IMAGE, R.drawable.iphone15);
        values.put(TB_CLM_NAME, "iPhone 15");
        values.put(TB_CLM_PRICE, 1);
        values.put(TB_CLM_BRAND, "Apple");
        values.put(TB_CLM_PIECES, 1);
        values.put(TB_CLM_DESCRIPTION, "Latest Apple iPhone with A17 chip");
        values.put(TB_CLM_DISCOUNT, 5);
        db.insert(TB_MOBILE, null, values);
    }
    private void insertDefaultSportsProduct(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(TB_CLM_IMAGE, R.drawable.sports);
        values.put(TB_CLM_NAME, "Football");
        values.put(TB_CLM_PRICE, 1);
        values.put(TB_CLM_BRAND, "Nivia");
        values.put(TB_CLM_PIECES, 1);
        values.put(TB_CLM_DESCRIPTION, "Professional training football");
        values.put(TB_CLM_DISCOUNT, 8);
        db.insert(TB_SPORTS, null, values);
    }
    private void insertDefaultCarToolProduct(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(TB_CLM_IMAGE, R.drawable.cartool);
        values.put(TB_CLM_NAME, "Car Vacuum Cleaner");
        values.put(TB_CLM_PRICE, 1);
        values.put(TB_CLM_BRAND, "Black+Decker");
        values.put(TB_CLM_PIECES, 1);
        values.put(TB_CLM_DESCRIPTION, "Portable car vacuum cleaner");
        values.put(TB_CLM_DISCOUNT, 20);
        db.insert(TB_CAR_TOOL, null, values);
    }










    public boolean insertProduct(Products p,String tableName){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TB_CLM_IMAGE,p.getImage());
        values.put(TB_CLM_NAME,p.getName());
        values.put(TB_CLM_PRICE,p.getPrice());
        values.put(TB_CLM_BRAND,p.getBrand());
        values.put(TB_CLM_PIECES,p.getPieces());
        values.put(TB_CLM_DESCRIPTION,p.getDescription());
        values.put(TB_CLM_DISCOUNT,p.getDiscount());

        long res = db.insert(tableName,null,values);
        db.close();
        if(p.getDiscount()>0){
            insertProductDiscount(p);
        }
        return res != -1;
    }

    public boolean insertProductDiscount(Products p){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TB_CLM_IMAGE,p.getImage());
        values.put(TB_CLM_NAME,p.getName());
        values.put(TB_CLM_PRICE,p.getPrice());
        values.put(TB_CLM_BRAND,p.getBrand());
        values.put(TB_CLM_PIECES,p.getPieces());
        values.put(TB_CLM_DESCRIPTION,p.getDescription());
        values.put(TB_CLM_DISCOUNT,p.getDiscount());

        long ress = db.insert(TB_PRODUCT_DISCOUNT,null,values);
        db.close();
        return ress != -1;
    }

    public ArrayList<Products> getAllProducts(String tableName){
        ArrayList<Products> products = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+tableName,null);

        if(cursor.moveToFirst()){
            do{
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_NAME));
                Double price = cursor.getDouble(cursor.getColumnIndexOrThrow(TB_CLM_PRICE));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_BRAND));
                int pieces = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_PIECES));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_DESCRIPTION));
                double discount = cursor.getDouble(cursor.getColumnIndexOrThrow(TB_CLM_DISCOUNT));

                Products p = new Products(image,name,price,brand,pieces,description,discount);
                products.add(p);
            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return products;
    }

    public boolean insertProductInPurchases(Products p){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TB_CLM_IMAGE,p.getImage());
        values.put(TB_CLM_NAME,p.getName());
        values.put(TB_CLM_PRICE,p.getPrice());
        values.put(TB_CLM_BRAND,p.getBrand());
        values.put(TB_CLM_RATING,p.getRating());
        values.put(TB_CLM_QUANTITY,p.getQuantity());

        long res = db.insert(TB_PURCHASES,null,values);
        db.close();
        return res != -1;
    }
    public Products getProduct(int product_id,String tableName){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+tableName+" WHERE "+TB_CLM_ID+" =?",new String[]{String.valueOf(product_id)});

        if(cursor.moveToFirst()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_ID));
            int image = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_IMAGE));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_NAME));
            Double price = cursor.getDouble(cursor.getColumnIndexOrThrow(TB_CLM_PRICE));
            String brand = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_BRAND));
            int pieces = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_PIECES));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_DESCRIPTION));
            double discount = cursor.getDouble(cursor.getColumnIndexOrThrow(TB_CLM_DISCOUNT));

            Products p = new Products(id,image,name,price,brand,pieces,description,discount);
            cursor.close();
            db.close();
            return p;
        }

        cursor.close();
        db.close();
        return null;
    }
    @SuppressLint("Range")
    public int checkUser(String user_name, String password){
        SQLiteDatabase db = getReadableDatabase();
        String[] selectionArgs = {user_name, password};
        String[] columns = {TB_CLM_USER_ID};

        Cursor cursor = db.query(TB_USERS,
                columns,
                TB_CLM_USER_NAME+" =? AND "+TB_CLM_USER_PASSWORD+" =?",
                selectionArgs,
                null,null,null);

        int id = 0;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex(TB_CLM_USER_ID));
        }

        cursor.close();
        db.close();

        return id;
    }
    public ArrayList<Products> getProductForSearch(String nameProduct, String tableName){
        ArrayList<Products> products = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM "+tableName+" WHERE "+TB_CLM_NAME+" LIKE ?",
                new String[]{"%"+nameProduct+"%"});

        if(cursor.moveToFirst()){
            do{
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_NAME));
                Double price = cursor.getDouble(cursor.getColumnIndexOrThrow(TB_CLM_PRICE));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_BRAND));
                int pieces = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_PIECES));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_DESCRIPTION));
                double discount = cursor.getDouble(cursor.getColumnIndexOrThrow(TB_CLM_DISCOUNT));

                Products p = new Products(image,name,price,brand,pieces,description,discount);
                products.add(p);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }
    public Users getUser(int user_id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM "+TB_USERS+" WHERE "+TB_CLM_USER_ID+" =?",
                new String[]{String.valueOf(user_id)});

        if(cursor.moveToFirst()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_USER_ID));
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_USER_NAME));
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_USER_FULL_NAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_USER_PASSWORD));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_USER_EMAIL));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_USER_PHONE));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_USER_IMAGE));

            Users user = new Users(id,userName,fullName,image,password,email,phone);

            cursor.close();
            db.close();
            return user;
        }

        cursor.close();
        db.close();
        return null;
    }
    public boolean upDataUser(Users user){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TB_CLM_USER_FULL_NAME, user.getFullName());
        values.put(TB_CLM_USER_EMAIL, user.getEmail());
        values.put(TB_CLM_USER_PHONE, user.getPhone());
        values.put(TB_CLM_USER_IMAGE, user.getUserImage());

        String args[] = new String[]{String.valueOf(user.getId())};

        long result = db.update(TB_USERS, values, TB_CLM_USER_ID + "=?", args);

        db.close();
        return result > 0;
    }
    public ArrayList<Products> getAllProductsInPurchases(){
        ArrayList<Products> products = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TB_PURCHASES, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_ID));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_NAME));
                Double price = cursor.getDouble(cursor.getColumnIndexOrThrow(TB_CLM_PRICE));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_BRAND));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(TB_CLM_RATING));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_QUANTITY));

                Products p = new Products(id,image,name,price,brand,rating,quantity);
                products.add(p);

            }while(cursor.moveToNext());
        }



        cursor.close();
        db.close();

        return products;
    }
    public boolean insertUser(Users user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TB_CLM_USER_NAME, user.getUserName());
        values.put(TB_CLM_USER_FULL_NAME, user.getFullName());
        values.put(TB_CLM_USER_PASSWORD, user.getUserPassword());
        values.put(TB_CLM_USER_EMAIL, user.getEmail());
        values.put(TB_CLM_USER_PHONE, user.getPhone());
        values.put(TB_CLM_USER_IMAGE, user.getUserImage());

        long res = db.insert(TB_USERS, null, values);
        db.close();

        return res != -1;
    }
    public boolean deletePurchaseItem(int id){
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete(TB_PURCHASES, TB_CLM_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }
    public double getTotalPurchasesPrice(){
        double total = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT price, quantity FROM " + TB_PURCHASES, null);

        if(cursor.moveToFirst()){
            do{
                double price = cursor.getDouble(0);
                int quantity = cursor.getInt(1);
                total += price * quantity;
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return total;
    }
    public void clearCart(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TB_PURCHASES, null, null);
        db.close();
    }
    public int getCartCount() {

        int count = 0;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + TB_CLM_QUANTITY + ") FROM " + TB_PURCHASES,
                null);

        if(cursor.moveToFirst()){
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }
    // 🔥 Insert Order
    public int insertOrder(int userId, double total, String paymentMethod){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TB_CLM_USER_ID, userId);
        values.put(TB_CLM_ORDER_DATE, String.valueOf(System.currentTimeMillis()));
        values.put(TB_CLM_ORDER_TOTAL, total);
        values.put("payment_method", paymentMethod);   // 🔥 ADD THIS

        long result = db.insert(TB_ORDERS, null, values);
        db.close();

        return (int) result;
    }



    // 🔥 Get Orders of User
    public ArrayList<Order> getUserOrders(int userId){

        ArrayList<Order> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TB_ORDERS +
                        " WHERE " + TB_CLM_USER_ID + "=?",
                new String[]{String.valueOf(userId)});

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_ORDER_DATE));
                double total = cursor.getDouble(cursor.getColumnIndexOrThrow(TB_CLM_ORDER_TOTAL));

                // 🔥 VERY IMPORTANT
                String payment = cursor.getString(
                        cursor.getColumnIndexOrThrow("payment_method"));

                list.add(new Order(id, date, total, payment));

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public boolean insertOrderItem(int orderId, Products p){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TB_CLM_ORDER_ID, orderId);
        values.put(TB_CLM_IMAGE, p.getImage());
        values.put(TB_CLM_NAME, p.getName());
        values.put(TB_CLM_PRICE, p.getPrice());
        values.put(TB_CLM_QUANTITY, p.getQuantity());

        long result = db.insert(TB_ORDER_ITEMS, null, values);
        db.close();

        return result > 0;
    }
    public ArrayList<Products> getOrderItems(int orderId){

        ArrayList<Products> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TB_ORDER_ITEMS +
                        " WHERE " + TB_CLM_ORDER_ID + "=?",
                new String[]{String.valueOf(orderId)});

        if(cursor.moveToFirst()){
            do{
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(TB_CLM_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(TB_CLM_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(TB_CLM_QUANTITY));

                Products p = new Products(image,name,price,"",0,"",0);
                p.setQuantity(quantity);

                list.add(p);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }
}
