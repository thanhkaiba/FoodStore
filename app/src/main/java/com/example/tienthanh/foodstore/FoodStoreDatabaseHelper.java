package com.example.tienthanh.foodstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FoodStoreDatabaseHelper extends SQLiteOpenHelper {

    private static final int VENDOR = 104;
    private static Context context;
    private static final String DB_NAME = "foodstore";
    private static final int DB_VERSION = 1;
    public static final int FOOD = 102;
    public static final int USER = 103;


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

    public static void insertFood(SQLiteDatabase db, Food food) {
        ContentValues foodValues = new ContentValues();
        foodValues.put("NAME", food.getName());
        foodValues.put("TYPE", food.getType());
        foodValues.put("DESCRIPTION", food.getDescription());
        foodValues.put("IMAGE", food.getImg());
        foodValues.put("COST", food.getCost());
        foodValues.put("UNIT", food.getUnit());
        foodValues.put("VENDORID", food.getVendorID());
        db.insert("FOOD", null, foodValues);
    }

    public static void insertVendor(SQLiteDatabase db, Vendor vendor) {
        ContentValues vendorValues = new ContentValues();
        vendorValues.put("NAME", vendor.getName());
        vendorValues.put("ADDRESS", vendor.getAddress());
        vendorValues.put("EMAIL", vendor.getEmail());
        vendorValues.put("PHONE",vendor.getPhone());
        vendorValues.put("IMAGE",vendor.getImg());
        db.insert("VENDOR", null, vendorValues);
    }


    public static void insertUser(SQLiteDatabase db, User user) {
        ContentValues userValues = new ContentValues();
        userValues.put("PASSWORD", user.getPassword());
        userValues.put("IMAGE", user.getImg());
        userValues.put("NAME", user.getName());
        userValues.put("GENDER", user.getGender());
        userValues.put("BIRTHDAY", user.getBirthday());
        userValues.put("EMAIL", user.getEmail());
        userValues.put("PHONE", user.getPhone());
        userValues.put("PRIVILEGE", user.getPrivilege());
        userValues.put("ADDRESS", user.getAddress());
        db.insert("USERS", null, userValues);

    }


    public static Bitmap loadImageFromStorage(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();


        try {
            File f = new File(path);
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, options);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setContext(Context context) {
        FoodStoreDatabaseHelper.context = context;
    }


    public static String saveToInternalStorage(Bitmap bitmapImage, String name, int type) {
        name = name.toLowerCase();
        // path to /data/data/your app/app_data/imageDir
        File directory = context.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir

        File myPath = new File(directory, name.toLowerCase() + type + ".jpeg");
        if (myPath.exists()) {
            myPath.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myPath.getAbsolutePath();

    }

    public static String saveDrawableToInternalStorage(int id, String name, int type) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), id);

        return saveToInternalStorage(bm, name, type);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {

            db.execSQL("CREATE TABLE VENDOR (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT NOT NULL, "
                    + "IMAGE TEXT, "
                    + "ADDRESS TEXT, "
                    + "EMAIL TEXT, "
                    + "PHONE TEXT);");

            db.execSQL("CREATE TABLE FOOD (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT NOT NULL, "
                    + "TYPE TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "IMAGE TEXT, "
                    + "COST REAL, "
                    + "UNIT TEXT, "
                    + "VENDORID INTEGER, "
                    + "FOREIGN KEY(VENDORID) REFERENCES VENDOR(_id));");

            db.execSQL("CREATE TABLE USERS ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "PASSWORD INTEGER NOT NULL, "
                    + "IMAGE TEXT, "
                    + "NAME TEXT, "
                    + "GENDER TEXT, "
                    + "BIRTHDAY TEXT, "
                    + "EMAIL TEXT NOT NULL, "
                    + "PHONE TEXT, "
                    + "PRIVILEGE INTEGER NOT NULL, "
                    + "ADDRESS TEXT);");




            Vendor vendor = new Vendor("Earth's Best", saveDrawableToInternalStorage(R.drawable.earth_best, "EarthsBest@gmail.com", VENDOR), "4600 Sleepytime Dr. Boulder, CO 80301 USA",
                    "EarthsBest@gmail.com", "1-800-442-4221");
            insertVendor(db, vendor);
            vendor = new Vendor("Amy's Kitchen ", saveDrawableToInternalStorage(R.drawable.amyskitchen , "donations@amys.com", VENDOR), "Amy's Customer Care P.O. Box 7129 Petaluma, Ca 94955 ",
                    "donations@amys.com", "707-781-6600");
            insertVendor(db, vendor);

            Food food = new Food("Mango", "Fruit", "\"The king of the fruits,\" mango fruit is one of the most popular, " +
                    "nutritionally rich fruits with unique flavor, fragrance, taste, and heath promoting qualities, making it" +
                    " numero-uno among new functional foods, often labeled as “super fruits", saveDrawableToInternalStorage(R.drawable.mango, "mango", FOOD), 50, "fruit", 2);
            insertFood(db, food);
            food = new Food("Pineapple", "Fruit", "Pineapples are tropical fruit that are rich in vitamins, enzymes and antioxidants. " +
                    "They may help boost the immune system, build strong bones and aid indigestion. Also, despite their sweetness, pineapples are low in calories",
                    saveDrawableToInternalStorage(R.drawable.pineapple, "pineapple", FOOD), 70.5, "fruit", 1);
            insertFood(db, food);
            food = new Food("Coconut", "Fruit", "Coconut oil is high in healthy saturated fats that have different effects than most other fats in your diet.",
                    saveDrawableToInternalStorage(R.drawable.coconut, "coconut", FOOD), 20.5, "fruit", 2);
            insertFood(db, food);

            User user = new User( MD5("12345678"), saveDrawableToInternalStorage(R.drawable.thanh, "tienthanhit97@gmail.com", USER), "Nguyễn Tiến Thành", "Nam", "30-04-1997",
                    "tienthanhit97@gmail.com", "01679003648", 1, "Vietnam");
            insertUser(db, user);
            user = new User( MD5("12345678"), saveDrawableToInternalStorage(R.drawable.tzuyu, "tzuyu@gmail.com", USER), "Tzuyu", "Nữ", "14-6-1999",
                    "tzuyu@gmail.com", "0123456789", 2, "South Korea");
            insertUser(db, user);
            user = new User(MD5("12345678"), saveDrawableToInternalStorage(R.drawable.kimyojung, "kimyooyung@gmail.com", USER), "Kim Yoo Yung", "Nữ", "22-9-1999",
                    "kimyooyung@gmail.com", "0123456789", 2, "South Korea");
            insertUser(db, user);


        }
        if (oldVersion < 2) {

        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(db);
    }
}
