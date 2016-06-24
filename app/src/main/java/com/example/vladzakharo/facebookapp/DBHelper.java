package com.example.vladzakharo.facebookapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vlad Zakharo on 10.06.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
<<<<<<< HEAD
        // Создаем таблицу с полями
=======
        // создаем таблицу с полями
>>>>>>> abaf2eb7d1da7082f4e2cd79ae7d690dbbf3e0b5
        db.execSQL("create table mytable ("
                + "message text,"
                + "latitude real,"
                + "longitude real" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}





