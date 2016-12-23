package com.momo.mvp_demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RealMo on 2016-12-23.
 */
public class DBHelper extends SQLiteOpenHelper {
    private final static String DBNAME = "my.db";
    private final static String USER = "user";
    private final static String FOOD = "food";
    private final static String PERSON = "person";
    private final static int STARTVERSION = 1;
    private final static int CURRENTVERSION = 3;


    public DBHelper(Context context) {
        super(context, DBNAME, null, CURRENTVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + USER + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME,PASSWORD)");
        onUpgrade(db, STARTVERSION, CURRENTVERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                //添加FOOD
                db.execSQL("CREATE TABLE IF NOT EXISTS " + FOOD + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME,PASSWORD)");
            case 2:
                //添加PERSON
                db.execSQL("CREATE TABLE IF NOT EXISTS " + PERSON + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME,PASSWORD)");
                break;
        }
    }
}