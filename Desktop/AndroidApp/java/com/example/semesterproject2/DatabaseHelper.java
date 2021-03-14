package com.example.semesterproject2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DBNAME = "CoDatabase";
    public static String TABLE_NAME = "Coordinates";

    // columns names
    public static String COLUMNE_ID = "ID";
    public static String COLUMNE_LAT = "LATITUDE";
    public static String COLUMNE_LONG = "LONGITUDE";
    public static String COLUMNE_TIMESTAMP = "TIMESTAMP";

    private String SQL_query = "CREATE TABLE "+ TABLE_NAME + "( " + COLUMNE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMNE_LAT + " REAL, " + COLUMNE_LONG + " REAL, " +  COLUMNE_TIMESTAMP + " TEXT ) ";

    public DatabaseHelper(@Nullable Context context) {
        super(context,DBNAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Coordinates");
        onCreate(db);
    }

    public long AddCoordinates (CoordinatesModel coordinates) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMNE_LAT,coordinates.getLat());
        values.put(COLUMNE_LONG,coordinates.getLon());
        values.put(COLUMNE_TIMESTAMP,coordinates.getTimestamp());

        long id = db.insert(TABLE_NAME,null,values);
        return id;
    }

    public long AddCoordinates (ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();

        values.put(COLUMNE_LAT,values.getAsFloat("LATITUDE"));
        values.put(COLUMNE_LONG,values.getAsFloat("LONGITUDE"));
        values.put(COLUMNE_TIMESTAMP,values.getAsString("TIMESTAMP"));

        long id = db.insert(TABLE_NAME,null,values);
        return id;
    }

}
