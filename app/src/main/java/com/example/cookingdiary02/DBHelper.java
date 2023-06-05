package com.example.cookingdiary02;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//데이터베이스 스키마 생성
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "database";
    public static final int DATABASE_VERSION = 1;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlInfo = "CREATE TABLE IF NOT EXISTS INFO ("        //로그인 정보
                + "id text primary key, "
                + "pw text, "
                + "name text,"
                + "address text,"
                + "number text);";
        db.execSQL(sqlInfo);


        String sqlPersonal = "CREATE TABLE IF NOT EXISTS PERSONAL ("   //유저 정보
                + "id text primary key references INFO(id), "
                + "count integer);";
        db.execSQL(sqlPersonal);

        String sqlIngredient = "CREATE TABLE IF NOT EXISTS INGREDIENT ("     //재료 정보
                + "num_recipe integer primary key autoincrement references  RECIPE(num_recipe), "
                + "ingredient text);";
        db.execSQL(sqlIngredient);

        String sqlFood = "CREATE TABLE IF NOT EXISTS FOOD ("                  //음식 정보
                + "num_recipe integer primary key autoincrement references RECIPE(num_recipe), "
                + "food text references RECIPE(food), "
                + "fcategory text);";
        db.execSQL(sqlFood);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlInfo = "DROP TABLE IF EXISTS INFO";
        db.execSQL(sqlInfo);

        String sqlRecipe = "DROP TABLE IF EXISTS RECIPE";
        db.execSQL(sqlRecipe);

        String sqlPersonal = "DROP TABLE IF EXISTS PERSONAL";
        db.execSQL(sqlPersonal);

        String sqlIngredient = "DROP TABLE IF EXISTS INGREDIENT";
        db.execSQL(sqlIngredient);

        String sqlFood = "DROP TABLE IF EXISTS FOOD";
        db.execSQL(sqlFood);

        onCreate(db);
    }
}