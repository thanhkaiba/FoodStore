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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class EditVendorActivity extends AppCompatActivity {

    public static final String EDIT_VENDOR = "Edit vendor";
    private static final int RESULT_LOAD_IMG = 201;
    private static final int RESULT_CAPTURE_IMG = 200;
    private Bitmap selectedImage;
    private ImageView image;
    private Vendor vendor;
    private ArrayList<Vendor> vendors;
    private EditText name;
    private EditText address;
    private EditText email;
    private EditText phone;
    private String preName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vendor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        vendors = getVendorsDatabase();
        image = findViewById(R.id.info_image);
        name = findViewById(R.id.info_name);
        phone = findViewById(R.id.info_phone);
        email = findViewById(R.id.info_email);
        address = findViewById(R.id.info_address);

        Intent intent = getIntent();
        if (intent.hasExtra(EDIT_VENDOR)) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            vendor = (Vendor) intent.getSerializableExtra(EDIT_VENDOR);
            preName = vendor.getName();
            image.setImageBitmap(FoodStoreDatabaseHelper.loadImageFromStorage(vendor.getImg(), size.x, 300));
            name.setText(vendor.getName());
            phone.setText(vendor.getPhone());
            email.setText(vendor.getEmail());
            address.setText(vendor.getAddress());

        }
        
        name.addTextChangedListener(new TextWatcher() {
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
                if (!text.equals(preName)) {
                    for (Vendor vendor : vendors) {
                        if (vendor.getName().toLowerCase().equals(text)) {
                            name.setError("Name already exists!");
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
                finish();
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


    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private ArrayList<Vendor> getVendorsDatabase() {
        ArrayList<Vendor> vendorsList = new ArrayList<Vendor>();

        SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(this);
        try {

            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM VENDOR", null);

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    String image = cursor.getString(1);
                    String name = cursor.getString(3);
                    String email = cursor.getString(2);
                    String phone = cursor.getString(4);
                    String address = cursor.getString(5);

                    Vendor vendor = new Vendor(id, image, name, email, phone, address);

                    vendorsList.add(vendor);
                    if (cursor.isLast()) {
                        break;
                    }
                    cursor.moveToNext();
                } while (true);
                cursor.close();
                db.close();
            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return vendorsList;
    }

    private class UpdateVendorTask extends AsyncTask<Void, Void, Boolean> {


        private ContentValues vendorValues;

        @Override
        protected void onPostExecute(Boolean done) {
            Intent returnIntent = new Intent();
            if (!done) {
                Toast toast = Toast.makeText(EditVendorActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
                setResult(RESULT_CANCELED, returnIntent);

            } else {
                returnIntent.putExtra(VendorDetailActivity.VENDOR_INFO, vendor);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
            finish();

        }

        @Override
        protected void onPreExecute() {
            vendorValues = new ContentValues();
            vendorValues.put("NAME", vendor.getName());
            vendorValues.put("ADDRESS", vendor.getAddress());
            vendorValues.put("EMAIL", vendor.getEmail());
            vendorValues.put("PHONE", vendor.getPhone());
            vendorValues.put("IMAGE", vendor.getImg());
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(EditVendorActivity.this);
            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                if (preName == null) {
                    long id = db.insert("VENDOR", null, vendorValues);
                    vendor.setId(id);

                } else {
                    db.update("VENDOR", vendorValues, "_id = ?", new String[]{String.valueOf(vendor.getId())});

                }

                db.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private boolean isDataChanged() {

        if (vendor == null && preName == null) {
            return true;
        }
        if (selectedImage != null) {
            return true;
        }

        if (!vendor.getEmail().toLowerCase().equals(email.getText().toString().toLowerCase())) {
            return true;
        }
        if (!vendor.getName().toLowerCase().equals(name.getText().toString().toLowerCase())) {
            return true;
        }
        if (!vendor.getAddress().equals(address.getText().toString())) {
            return true;
        }

        if (!vendor.getPhone().equals(phone.getText().toString())) {
            return true;
        }
        return false;

    }

    public void onClickDone(View view) {


        if (name.getText().toString().isEmpty()) {
            name.setError("You did not enter name");
            name.requestFocus();
            return;
        }

        if (name.getError() != null) {
            name.requestFocus();
            return;
        }

        if (email.getText().toString().isEmpty()) {
            email.setError("You did not enter email!");
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

        if (address.getText().toString().isEmpty()) {
            address.setError("You did not enter address!");
            address.requestFocus();
            return;
        }
        if (isDataChanged()) {
            if (vendor == null) {
                vendor = new Vendor();
            }
            vendor.setName(name.getText().toString());
            vendor.setEmail(email.getText().toString());
            if (selectedImage != null)
                vendor.setImg(FoodStoreDatabaseHelper.saveToInternalStorage(selectedImage, vendor.getEmail(), FoodStoreDatabaseHelper.FOOD));
            vendor.setPhone(phone.getText().toString());
            vendor.setAddress(address.getText().toString());
            
            new UpdateVendorTask().execute();
        } else {
            Toast.makeText(this, "Nothing change", Toast.LENGTH_SHORT).show();
        }

    }
}


