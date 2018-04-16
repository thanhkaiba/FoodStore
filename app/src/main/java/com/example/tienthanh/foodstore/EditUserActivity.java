package com.example.tienthanh.foodstore;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



public class EditUserActivity extends AppCompatActivity {

    public static final String EDIT_USER = "Edit User";
    private static final int RESULT_LOAD_IMG = 201;
    private static final int RESULT_CAPTURE_IMG = 200;

    private User user;
    private ArrayList<User> users;
    private ImageView image;
    private Bitmap selectedImage;
    private EditText name;
    private Spinner gender;
    private EditText address;
    private Spinner privilege;
    private EditText email;
    private EditText phone;
    private DatePicker birthday;
    private String preEmail;
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        users = getUsersDatabase();
        image = findViewById(R.id.info_image);
        privilege = findViewById(R.id.privilege_info);
        name = findViewById(R.id.name_info);
        gender = findViewById(R.id.gender_info);
        phone = findViewById(R.id.phone_info);
        email = findViewById(R.id.email_info);
        birthday = findViewById(R.id.birthday_info);
        address = findViewById(R.id.address_info);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -10);
        birthday.setMaxDate(c.getTimeInMillis());

        ArrayAdapter<String> privilegeAdapter = new MySpinnerAdapter(this, android.R.layout.simple_list_item_1, User.PRIVILEGE);
        privilege.setAdapter(privilegeAdapter);
        ArrayAdapter<String> genderAdapter = new MySpinnerAdapter(this, android.R.layout.simple_list_item_1, User.GENDER);
        gender.setAdapter(genderAdapter);

        Intent intent = getIntent();
        if (intent.hasExtra(EDIT_USER)) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            user = (User) intent.getSerializableExtra(EDIT_USER);
            preEmail = user.getEmail();
            image.setImageBitmap(FoodStoreDatabaseHelper.loadImageFromStorage(user.getImg(), size.x, 300));
            privilege.setSelection(user.getPrivilege());
            if (MainActivity.user.getPrivilege() > 0) {
                privilege.setEnabled(false);
            }
            name.setText(user.getName());
            phone.setText(user.getPhone());
            email.setText(user.getEmail());
            address.setText(user.getAddress());
            try {
                Calendar calendar = Calendar.getInstance();
                if (user.getBirthday() != null) {
                    calendar.setTime(format.parse(user.getBirthday()));
                }
                birthday.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (user.getGender() != null) {
                gender.setSelection(genderAdapter.getPosition(user.getGender()));
            }

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
                if (!text.equals(preEmail)) {
                    for (User user : users) {
                        if (user.getEmail().toLowerCase().equals(text)) {
                            email.setError("Email already exists!");
                            break;
                        }
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_change_image:
                onChangeImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onChangeImage() {

        CharSequence chooses[] = new CharSequence[]{"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        builder.setItems(chooses, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, RESULT_CAPTURE_IMG);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                }
            }
        });
        builder.show();

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(reqCode, resultCode, imageReturnedIntent);
        switch (reqCode) {
            case RESULT_LOAD_IMG:
                if (resultCode == RESULT_OK) {
                    try {

                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);

                        if (image != null) {
                            image.setImageBitmap(selectedImage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
                break;
            case RESULT_CAPTURE_IMG:
                if (resultCode == RESULT_OK) {

                    selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    if (image != null) {
                        image.setImageBitmap(selectedImage);
                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }

    }

    public void onClickDone(View view) {

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


        if (name.getText().toString().isEmpty()) {
            name.setError("You did not enter name");
            name.requestFocus();
            return;
        }

        if (name.getText().toString().isEmpty()) {
            name.setError("You did not enter name!");
            name.requestFocus();
            return;
        }

        if (phone.getText().toString().isEmpty()) {
            phone.setError("You did not enter phone number!");
            phone.requestFocus();
            return;
        }

        if (address.getText().toString().isEmpty()) {
            address.setError("You did not enter address!");
            address.requestFocus();
            return;
        }
        if (isDataChanged()) {
            if (user == null){
                user = new User();
            }
            user.setEmail(email.getText().toString());
            if (selectedImage != null)
                user.setImg(FoodStoreDatabaseHelper.saveToInternalStorage(selectedImage, user.getEmail(), FoodStoreDatabaseHelper.FOOD));
            user.setPhone(phone.getText().toString());
            user.setAddress(address.getText().toString());
            user.setName(name.getText().toString());
            user.setBirthday(birthday.getDayOfMonth() + "-" + (birthday.getMonth() + 1) + "-" + birthday.getYear());
            user.setPrivilege((int)privilege.getSelectedItemId());
            user.setGender(gender.getSelectedItem().toString());
            if (preEmail == null) {
                showPasswordDialog();
            }
            else {
                new UpdateUserTask().execute();
            }
        } else {
            Toast.makeText(this, "Nothing change", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isDataChanged() {

        if (user == null && preEmail == null) {
            return true;
        }
        if (selectedImage != null) {
            return true;
        }

        if (!user.getEmail().toLowerCase().equals(email.getText().toString().toLowerCase())) {
            return true;
        }
        if (!user.getName().equals(name.getText().toString())) {
            return true;
        }
        if (!user.getAddress().equals(address.getText().toString())) {
            return true;
        }
        if (user.getPrivilege() != privilege.getSelectedItemId()) {
            return true;
        }
        if (!user.getPhone().equals(phone.getText().toString())) {
            return true;
        }
        if (!user.getGender().equals(gender.getSelectedItem().toString())) {
            return true;
        }
        return !user.getBirthday().equals(birthday.getDayOfMonth() + "-" + (birthday.getMonth() + 1) + "-" + birthday.getYear());

    }

    public void showPasswordDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText password = dialogView.findViewById(R.id.password);
        final EditText retypePassword = dialogView.findViewById(R.id.retype_password);

        dialogBuilder.setTitle("Password");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (password.getText().toString().isEmpty()) {
                    password.setError("Empty password!");
                }
                if (!password.getText().toString().equals(retypePassword.getText().toString())) {
                    retypePassword.setError("Password not match!");
                }
                else {
                    user.setPassword(FoodStoreDatabaseHelper.MD5(password.getText().toString()));
                    new UpdateUserTask().execute();
                    dialog.dismiss();
                }

            }
        });
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

    private class UpdateUserTask extends AsyncTask<Void, Void, Boolean> {

        private ContentValues userValues;

        @Override
        protected void onPostExecute(Boolean done) {
            Intent returnIntent = new Intent();
            if (!done) {
                Toast toast = Toast.makeText(EditUserActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
                setResult(RESULT_CANCELED, returnIntent);
            } else {
                returnIntent.putExtra(UserDetailActivity.USER_INFO, user);
                setResult(RESULT_OK, returnIntent);
                finish();
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
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(EditUserActivity.this);
            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                if (preEmail == null) {
                    long id = db.insert("USERS", null, userValues);
                    user.setId(id);

                } else {
                    db.update("USERS", userValues, "_id = ?", new String[]{String.valueOf(user.getId())});

                }

                db.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

}
