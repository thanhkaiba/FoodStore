package com.example.tienthanh.foodstore;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        users = getUsersDatabase();
        email = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
    }

    private ArrayList<User> getUsersDatabase() {
        ArrayList<User> userList = new ArrayList<User>();

        SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(this);
        try {

            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM USERS", null);

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    String password = cursor.getString(1);
                    String image = cursor.getString(2);
                    String name = cursor.getString(3);
                    String gender = cursor.getString(4);
                    String birthday = cursor.getString(5);
                    String email = cursor.getString(6);
                    String phone = cursor.getString(7);
                    int privilege = cursor.getInt(8);
                    String address = cursor.getString(9);

                    User user = new User(id, password, image, name, gender, birthday, email, phone, privilege, address);

                    userList.add(user);
                    if (cursor.isLast() ) {
                        break;
                    }
                    cursor.moveToNext();
                } while(true);
                cursor.close();
                db.close();
            }
        }catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return userList;
    }

    public void onClickLogin(View view) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email.getText().toString())) {
                if (user.getPassword().equals(FoodStoreDatabaseHelper.MD5(pass.getText().toString()))) {
                    MainActivity.user = user;
                    onBackPressed();
                    return;
                }
            }
        }
        Toast toast = Toast.makeText(this, "Email or Password not match!", Toast.LENGTH_SHORT);
        toast.show();

    }
}
