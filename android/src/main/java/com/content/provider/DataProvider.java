package com.content.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.net.UriCompat;

import java.sql.RowId;
import java.util.HashMap;

public class DataProvider extends ContentProvider {
  public static final String PROVIDER_NAME = "com.content.provider.DataProvider";
  static final String RESOURCE = "users";
  static final String URL = "content://" + PROVIDER_NAME + "/" + RESOURCE;
  public static final Uri CONTENT_URI = Uri.parse( URL );

  static final int uriCode = 1;
  static final UriMatcher uriMatcher;
  private static HashMap<String,String> values;

  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI( PROVIDER_NAME, RESOURCE, uriCode);
    uriMatcher.addURI(PROVIDER_NAME, RESOURCE + "/*", uriCode);
  }

  private SQLiteDatabase db;
  static final String DATABASE_NAME = "usersDB";
  static final String TABLE_NAME = "users";
  static final String id = "id";
  static final String name = "name";
  static final int DATABASE_VERSION = 1;
  static final String CREATE_DB_TABLE = " CREATE TABLE IF NOT EXISTS " + TABLE_NAME
    + "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
    + name + " TEXT NOT NULL);";

  private static class DatabaseHelper extends SQLiteOpenHelper {
    DatabaseHelper(Context context) {
      super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME );
       onCreate(db);
    }
  }

  @Override
  public boolean onCreate() {
    Context context = getContext();
    DatabaseHelper dbHelper = new DatabaseHelper(context);
    db = dbHelper.getWritableDatabase();
    return db != null;
  }

  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
    SQLiteQueryBuilder _db = new SQLiteQueryBuilder();
    _db.setTables(TABLE_NAME);

    switch(uriMatcher.match(uri)){
      case uriCode:
        _db.setProjectionMap(values);
        break;
      default:
          throw new IllegalArgumentException("Unknown URI" + uri);
    }

    if( sortOrder == null || sortOrder == "" ){
      sortOrder = id;
    }

    Cursor cursor = _db.query( db, projection, selection, selectionArgs, null, null, sortOrder );
    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    switch(uriMatcher.match(uri)) {
      case uriCode:
        return "vnd.android.dir/users";
      default:
        throw new IllegalArgumentException(" Unsuported URI: " + uri);
    }
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    long rowID = db.insert( TABLE_NAME, "", values );
    if ( rowID > 0 ){
      Uri _uri = ContentUris.withAppendedId( CONTENT_URI, rowID);
      getContext().getContentResolver().notifyChange(_uri, null);
      return _uri;
    }
    throw new SQLiteException( "Failed to add record into " + uri );
  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
    int count;
      switch (uriMatcher.match(uri)) {
        case uriCode:
          count = db.delete(TABLE_NAME, selection, selectionArgs);
          break;
        default:
          throw new IllegalArgumentException(" Unsuported URI: " + uri);
      }
      getContext().getContentResolver().notifyChange(uri, null);
      return count;
  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
    int count;
    switch (uriMatcher.match(uri)) {
      case uriCode:
        count = db.update(TABLE_NAME, values, selection, selectionArgs);
        break;
      default:
        throw new IllegalArgumentException(" Unsuported URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }
}
