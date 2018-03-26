package com.example.tienthanh.foodstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class FoodStoreDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "foodstore";
    private static final int DB_VERSION = 1;

    public FoodStoreDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE FOOD (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "TYPE TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER, "
                    + "COST REAL, "
                    + "UNIT TEXT);");

            db.execSQL("CREATE TABLE USERS ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "USER TEXT NOT NULL, "
                    + "PASSWORD INTEGER, "
                    + "IMAGE_RESOURCE_ID INTEGER, "
                    + "NAME TEXT, "
                    + "GENDER TEXT, "
                    + "BIRTHDAY TEXT, "
                    + "EMAIL TEXT, "
                    + "PHONE TEXT, "
                    + "PRIVILEGE INTEGER NOT NULL );");

            insertFood(db, "Mango", "Fruit", "\"The king of the fruits,\" mango fruit is one of the most popular, " +
                    "nutritionally rich fruits with unique flavor, fragrance, taste, and heath promoting qualities, making it" +
                    " numero-uno among new functional foods, often labeled as “super fruits", R.drawable.mango, 50, "fruit");
            insertFood(db, "Pineapple", "Fruit", "Pineapples are tropical fruit that are rich in vitamins, enzymes and antioxidants. " +
                    "They may help boost the immune system, build strong bones and aid indigestion. Also, despite their sweetness, pineapples are low in calories",
                    R.drawable.pineapple, 70.5, "fruit");
            insertFood(db, "Coconut", "Fruit", "Coconut oil is high in healthy saturated fats that have different effects than most other fats in your diet.",
                    R.drawable.coconut, 20.5, "fruit");

            insertUser(db, "thanhkaiba", "12345678", R.drawable.thanh,"Nguyễn Tiến Thành", "Nam", "30-04-1997",
                    "tienthanhit97@gmail.com","01679003648", 1);

        }
        if (oldVersion < 2) {

        }

    }

    private static void insertFood(SQLiteDatabase db, String name, String type,
                                   String des, int img, double cost, String unit) {
        ContentValues foodValues = new ContentValues();
        foodValues.put("NAME", name);
        foodValues.put("TYPE", type);
        foodValues.put("DESCRIPTION", des);
        foodValues.put("IMAGE_RESOURCE_ID", img);
        foodValues.put("COST", cost);
        foodValues.put("UNIT", unit);
        db.insert("FOOD", null, foodValues);
    }
    private static void insertUser(SQLiteDatabase db, String user, String password, int img,
                                   String name, String gender, String birthday, String email,
                                   String phone, int privilege) {
        ContentValues userValues = new ContentValues();
        userValues.put("USER", user);
        userValues.put("PASSWORD", password);
        userValues.put("IMAGE_RESOURCE_ID", img);
        userValues.put("NAME", name);
        userValues.put("GENDER", gender);
        userValues.put("BIRTHDAY", birthday);
        userValues.put("EMAIL", email);
        userValues.put("PHONE", phone);
        userValues.put("PRIVILEGE", privilege);
        db.insert("USERS", null, userValues);
    }

}
