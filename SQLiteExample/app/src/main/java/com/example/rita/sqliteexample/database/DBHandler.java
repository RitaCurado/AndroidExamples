package com.example.rita.sqliteexample.database;

/**
 * Created by Rita on 01/04/2015.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rita.sqliteexample.provider.MyContentProvider;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private ContentResolver myCR;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "exampleDB.db";

    // Table Names
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_USERS = "users";

    // Common column names
    public static final String COLUMN_ID = "id";

    // Products Table - column names
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_QUANTITY = "quantity";

    // Users Table - column names
    public static final String COLUMN_NAME = "name";

    // Table Create Statements

    // Products table create statement
    private static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
            TABLE_PRODUCTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
            + " TEXT," + COLUMN_QUANTITY + " INTEGER" + ")";

    // Users table create statement
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " +
            TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME
            + " TEXT" + ")";


    public DBHandler(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        myCR = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        //create new tables
        onCreate(db);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////


    public void addProduct(Product product) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_QUANTITY, product.getQuantity());

        myCR.insert(MyContentProvider.CONTENT_URI_PRODUCTS, values);
    }

    public Product findProduct(String productname) {

        String[] projection = {COLUMN_ID, COLUMN_PRODUCTNAME, COLUMN_QUANTITY };
        String selection = "productname = \"" + productname + "\"";

        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI_PRODUCTS, projection, selection, null, null);

        Product product = new Product();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product = cursorToProduct(cursor);
            cursor.close();
        } else {
            product = null;
        }
        return product;
    }

    public boolean deleteProduct(String productname) {

        boolean result = false;

        String selection = "productname = \"" + productname + "\"";

        int rowsDeleted = myCR.delete(MyContentProvider.CONTENT_URI_PRODUCTS, selection, null);

        if (rowsDeleted > 0)
            result = true;

        return result;
    }

    public List<Product> getAllProducts(){
        List<Product> listProducts = new ArrayList<Product>();

        String[] projection = {COLUMN_ID, COLUMN_PRODUCTNAME, COLUMN_QUANTITY};

        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI_PRODUCTS, projection, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Product product = cursorToProduct(cursor);
                listProducts.add(product);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
        return listProducts;
    }

    private Product cursorToProduct(Cursor cursor) {
        Product product = new Product();
        product.setID(Integer.parseInt(cursor.getString(0)));
        product.setProductName(cursor.getString(1));
        product.setQuantity(Integer.parseInt(cursor.getString(2)));
        return product;
    }

    ////////////////////////////////////////////////////


    public void addUser(User user) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());

        myCR.insert(MyContentProvider.CONTENT_URI_USERS, values);
    }

    public User findUser(String username) {

        String[] projection = {COLUMN_ID, COLUMN_NAME};
        String selection = "name = \"" + username + "\"";

        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI_USERS, projection, selection, null, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user = cursorToUser(cursor);
            cursor.close();
        } else {
            user = null;
        }
        return user;
    }

    public boolean deleteUser(String username) {

        boolean result = false;

        String selection = "name = \"" + username + "\"";

        int rowsDeleted = myCR.delete(MyContentProvider.CONTENT_URI_USERS, selection, null);

        if (rowsDeleted > 0)
            result = true;

        return result;
    }

    public List<User> getAllUsers(){
        List<User> listUsers = new ArrayList<User>();

        String[] projection = {COLUMN_ID, COLUMN_NAME};

        Cursor cursor = myCR.query(MyContentProvider.CONTENT_URI_USERS, projection, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                User user = cursorToUser(cursor);
                listUsers.add(user);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
        }
        return listUsers;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(Integer.parseInt(cursor.getString(0)));
        user.setName(cursor.getString(1));
        return user;
    }


}
