package com.developer.avtocorrectapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "slovarstore.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "slovars"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RU = "ru";
    public static final String COLUMN_EN = "en";
    public static final String COLUMN_CREATE_AT = "create_at";
    public static final String COLUMN_UPDATE_AT = "update_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RU + " TEXT, "
                + COLUMN_EN + " TEXT, "
                + COLUMN_CREATE_AT + " TEXT, "
                + COLUMN_UPDATE_AT + " TEXT);");
        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_RU + ", "
                + COLUMN_EN + ", "
                + COLUMN_CREATE_AT + ", "
                + COLUMN_UPDATE_AT  + ") VALUES ('Книга', 'Book', '01.06.2020', '01.06.2020');");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}