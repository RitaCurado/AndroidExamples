package com.example.rita.sqliteexample.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.rita.sqliteexample.database.DBHandler;

/**
 * Created by Rita on 01/04/2015.
 */
public class MyContentProvider extends ContentProvider {

    private DBHandler myDB;

    private static final String AUTHORITY = "com.example.rita.sqliteexample.provider.MyContentProvider";

    private static final String PRODUCTS_TABLE = "products";
    private static final String USERS_TABLE = "users";

    public static final Uri CONTENT_URI_PRODUCTS = Uri.parse("content://" + AUTHORITY + "/" + PRODUCTS_TABLE);
    public static final Uri CONTENT_URI_USERS = Uri.parse("content://" + AUTHORITY + "/" + USERS_TABLE);

    public static final int PRODUCTS = 1;
    public static final int PRODUCTS_ID = 2;

    public static final int USERS = 3;
    public static final int USERS_ID = 4;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, PRODUCTS_TABLE, PRODUCTS);
        sURIMatcher.addURI(AUTHORITY, PRODUCTS_TABLE + "/#", PRODUCTS_ID);
        sURIMatcher.addURI(AUTHORITY, USERS_TABLE, USERS);
        sURIMatcher.addURI(AUTHORITY, USERS_TABLE + "/#", USERS_ID);
    }

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        myDB = new DBHandler(getContext(), null, null, 1);
        return false;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        String suri;

        SQLiteDatabase sqlDB = myDB.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case PRODUCTS:
                id = sqlDB.insert(DBHandler.TABLE_PRODUCTS, null, values);
                suri = PRODUCTS_TABLE + "/" + id;
                break;
            case USERS:
                id = sqlDB.insert(DBHandler.TABLE_USERS, null, values);
                suri = USERS_TABLE + "/" + id;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(suri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsDeleted = 0;
        String id;

        switch (uriType) {
            case PRODUCTS:
                rowsDeleted = sqlDB.delete(DBHandler.TABLE_PRODUCTS,
                        selection,
                        selectionArgs);
                break;

            case PRODUCTS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(DBHandler.TABLE_PRODUCTS,
                            DBHandler.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(DBHandler.TABLE_PRODUCTS,
                            DBHandler.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            case USERS:
                rowsDeleted = sqlDB.delete(DBHandler.TABLE_USERS,
                        selection,
                        selectionArgs);
                break;

            case USERS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(DBHandler.TABLE_USERS,
                            DBHandler.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(DBHandler.TABLE_USERS,
                            DBHandler.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsUpdated = 0;
        String id;

        switch (uriType) {
            case PRODUCTS:
                rowsUpdated = sqlDB.update(DBHandler.TABLE_PRODUCTS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PRODUCTS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DBHandler.TABLE_PRODUCTS,
                            values,
                            DBHandler.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(DBHandler.TABLE_PRODUCTS,
                            values,
                            DBHandler.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            case USERS:
                rowsUpdated = sqlDB.update(DBHandler.TABLE_USERS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case USERS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DBHandler.TABLE_USERS,
                            values,
                            DBHandler.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(DBHandler.TABLE_USERS,
                            values,
                            DBHandler.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        /*
        * URI – specifying the data source on which the query is to be performed.
        * Projection – The projection argument is simply a String array containing the name for each
                       of the columns that is to be returned in the result data set.
        * Selection – The “where” element of the selection to be performed as part of the query.
                      This argument controls which rows are selected from the specified database.
                      For example: productname = “Cat Food”.
        * Selection Args – Any additional arguments that need to be passed to the SQL query
                           operation to perform the selection.
        * Sort Order – The sort order for the selected rows.
        */

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case PRODUCTS_ID:
                queryBuilder.setTables(DBHandler.TABLE_PRODUCTS);
                queryBuilder.appendWhere(DBHandler.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case PRODUCTS:
                queryBuilder.setTables(DBHandler.TABLE_PRODUCTS);
                break;
            case USERS_ID:
                queryBuilder.setTables(DBHandler.TABLE_USERS);
                queryBuilder.appendWhere(DBHandler.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case USERS:
                queryBuilder.setTables(DBHandler.TABLE_USERS);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(myDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null,
                sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

}
