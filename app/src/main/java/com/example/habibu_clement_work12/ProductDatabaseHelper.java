package com.example.habibu_clement_work12;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "products_database.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_PRODUCTS = "products";

    // Column names
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_PRICE = "price";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_CATEGORY = "category";
    private static final String COL_IMAGE = "image";
    private static final String COL_IMAGE_URL = "image_url";

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_PRICE + " TEXT NOT NULL, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_IMAGE + " BLOB, " +
                COL_IMAGE_URL + " TEXT" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // Convert Bitmap to byte array
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Convert byte array to Bitmap
    private Bitmap byteArrayToBitmap(byte[] image) {
        if (image == null) return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    // CREATE - Insert new product
    public long insertProduct(Product product, String description, String category, Bitmap image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, product.getName());
        values.put(COL_PRICE, product.getPrice());
        values.put(COL_DESCRIPTION, description);
        values.put(COL_CATEGORY, category);
        values.put(COL_IMAGE, bitmapToByteArray(image));
        values.put(COL_IMAGE_URL, product.getImageUrl());

        long result = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return result;
    }

    // READ - Get all products
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " ORDER BY " + COL_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
                product.setPrice(cursor.getString(cursor.getColumnIndexOrThrow(COL_PRICE)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
                product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_URL)));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE));
                product.setImageBitmap(byteArrayToBitmap(imageBytes));
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    // READ - Get product by ID
    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null,
                COL_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

        Product product = null;
        if (cursor != null && cursor.moveToFirst()) {
            product = new Product();
            product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
            product.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
            product.setPrice(cursor.getString(cursor.getColumnIndexOrThrow(COL_PRICE)));
            product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
            product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)));
            product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_URL)));
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE));
            product.setImageBitmap(byteArrayToBitmap(imageBytes));
            cursor.close();
        }
        db.close();
        return product;
    }

    // UPDATE - Update product
    public int updateProduct(Product product, String description, String category, Bitmap image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, product.getName());
        values.put(COL_PRICE, product.getPrice());
        values.put(COL_DESCRIPTION, description);
        values.put(COL_CATEGORY, category);
        values.put(COL_IMAGE, bitmapToByteArray(image));
        values.put(COL_IMAGE_URL, product.getImageUrl());

        int result = db.update(TABLE_PRODUCTS, values,
                COL_ID + "=?", new String[]{String.valueOf(product.getId())});
        db.close();
        return result;
    }

    // DELETE - Delete product
    public int deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PRODUCTS, COL_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}

