package com.example.tienthanh.foodstore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    public static final String USER_LIST = "user list";
    private TextView name;
    private TextView email;
    private TextView address;
    private TextView password;
    private TextView phone;
    private User user;
    private ArrayList<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.info_name);
        email = findViewById(R.id.info_email);
        address = findViewById(R.id.info_address);
        password = findViewById(R.id.info_password);
        phone = findViewById(R.id.info_phone);
        Intent intent = getIntent();
        if (intent.hasExtra(USER_LIST)) {
            users = (ArrayList<User>) intent.getSerializableExtra(USER_LIST);
        }

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = s.toString().toLowerCase();
                text = text.trim();
                for (User user : users) {
                    if (user.getEmail().toLowerCase().equals(text)) {
                        email.setError("Email already exists!");
                        break;
                    }
                }

            }
        });
    }

    public void onClickSignUp(View view) {

        if (name.getText().toString().isEmpty()) {
            name.setError("You did not enter your name!");
            name.requestFocus();
            return;
        }

        if (email.getText().toString().isEmpty()) {
            email.setError("You did not enter email!");
            email.requestFocus();
            return;
        }

        if (email.getError() != null) {
            email.requestFocus();
            return;
        }

        if (!isValidEmail(email.getText().toString())) {
            email.setError("Invalid email!");
            email.requestFocus();
            return;
        }


        if (phone.getText().toString().isEmpty()) {
            phone.setError("You did not enter phone number!");
            phone.requestFocus();
            return;
        }

        if (password.getText().toString().isEmpty()) {
            password.setError("You did not enter phone number!");
            password.requestFocus();
            return;
        }

        if (address.getText().toString().isEmpty()) {
            address.setError("You did not enter address!");
            address.requestFocus();
            return;
        }
        user = new User();
        user.setEmail(email.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setName(name.getText().toString());
        user.setGender("Nam");
        Calendar calendar = Calendar.getInstance();
        user.setBirthday(calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR));
        user.setAddress(address.getText().toString());
        user.setPassword(FoodStoreDatabaseHelper.MD5(password.getText().toString()));
        user.setImg(FoodStoreDatabaseHelper.saveDrawableToInternalStorage(R.drawable.default_user, user.getEmail(), FoodStoreDatabaseHelper.USER));
        user.setPrivilege(3);
        new CreateNewUserTask().execute();
    }

    private class CreateNewUserTask extends AsyncTask<Void, Void, Boolean> {


        private ContentValues userValues;

        @Override
        protected void onPostExecute(Boolean done) {
            Intent returnIntent = new Intent();
            if (!done) {
                Toast toast = Toast.makeText(SignUpActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
                setResult(RESULT_CANCELED, returnIntent);
            } else {
                returnIntent.putExtra(LoginActivity.NEW_USER, user);
                setResult(RESULT_OK, returnIntent);
            }
            finish();
        }

        @Override
        protected void onPreExecute() {
            userValues = new ContentValues();
            userValues.put("PASSWORD", user.getPassword());
            userValues.put("IMAGE", user.getImg());
            userValues.put("NAME", user.getName());
            userValues.put("GENDER", user.getGender());
            userValues.put("BIRTHDAY", user.getBirthday());
            userValues.put("EMAIL", user.getEmail());
            userValues.put("PHONE", user.getPhone());
            userValues.put("PRIVILEGE", user.getPrivilege());
            userValues.put("ADDRESS", user.getAddress());

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(SignUpActivity.this);
            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                long id = db.insert("USERS", null, userValues);
                user.setId(id);
                db.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
