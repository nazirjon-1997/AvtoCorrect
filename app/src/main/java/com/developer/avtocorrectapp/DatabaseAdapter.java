package com.developer.avtocorrectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private Cursor getAllEntries(){
        String[] columns = new String[] {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_RU, DatabaseHelper.COLUMN_EN, DatabaseHelper.COLUMN_CREATE_AT, DatabaseHelper.COLUMN_UPDATE_AT};
        return  database.query(DatabaseHelper.TABLE, columns, null, null, null, null, null);
    }

    public List<Slovar> getSlovars(){ // получение все данные из бд
        ArrayList<Slovar> slovars = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String ru = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RU));
                String en = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EN));
                String create_at = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CREATE_AT));
                String update_at = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_UPDATE_AT));
                slovars.add(new Slovar(id, ru, en, create_at, update_at));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  slovars;
    }


    public Slovar getSlovarSearch(String slova){// получение данные через ru из бд
        Slovar slovar = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE, DatabaseHelper.COLUMN_RU);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(slova)});
        if(cursor.moveToFirst()){
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            String ru = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RU));
            String en = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EN));
            String create_at = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CREATE_AT));
            String update_at = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_UPDATE_AT));
            slovar = new Slovar(id, ru, en, create_at, update_at);
        }
        cursor.close();
        return  slovar;
    }

    public List<Slovar> getSlovarWord(String searchTerm) {// получение данные через ru или en из бд
        Slovar slovar = null;
        List<Slovar> slovarList = new ArrayList<>();
        String sql = "SELECT * FROM slovars WHERE ru LIKE '%" + searchTerm + "%' OR en LIKE '%" + searchTerm + "%'";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            String ru = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RU));
            String en = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EN));
            String create_at = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CREATE_AT));
            String update_at = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_UPDATE_AT));
            slovar = new Slovar(id, ru, en, create_at, update_at);
            slovarList.add(slovar);
            cursor.moveToNext();
        }
        cursor.close();
        return slovarList;
    }

    public long insert(Slovar slovar){ // добавленные данные
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_RU, slovar.getRu());
        cv.put(DatabaseHelper.COLUMN_EN, slovar.getEn());
        cv.put(DatabaseHelper.COLUMN_CREATE_AT, slovar.getCreated_at());
        cv.put(DatabaseHelper.COLUMN_UPDATE_AT, slovar.getUpdated_at());
        return  database.insert(DatabaseHelper.TABLE, null, cv);
    }

    public int delete(int slovarId){ // удалить данные через id
        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(slovarId)};
        return database.delete(DatabaseHelper.TABLE, whereClause, whereArgs);
    }

    public long update(Slovar slovar){ // обновленые данные
        String whereClause = DatabaseHelper.COLUMN_ID + "=" + String.valueOf(slovar.getId());
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_RU, slovar.getRu());
        cv.put(DatabaseHelper.COLUMN_EN, slovar.getEn());
        cv.put(DatabaseHelper.COLUMN_UPDATE_AT, slovar.getUpdated_at());
        return database.update(DatabaseHelper.TABLE, cv, whereClause, null);
    }
}
