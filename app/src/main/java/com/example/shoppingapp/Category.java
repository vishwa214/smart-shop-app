package com.example.shoppingapp;

public class Category {

    private int image;
    private String name;
    private String tableName;

    public Category(int image, String name, String tableName) {
        this.image = image;
        this.name = name;
        this.tableName = tableName;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getTableName() {
        return tableName;
    }
}
