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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EditFoodActivity extends AppCompatActivity {

    public static final String EDIT_FOOD = "Edit Food";
    private static final int RESULT_LOAD_IMG = 201;
    private static final int RESULT_CAPTURE_IMG = 200;
    private ArrayList<Vendor> vendors = new ArrayList<>();
    private Food food;
    private ImageView imageView;
    private Bitmap selectedImage;
    private EditText name;
    private TextView description;
    private Spinner type;
    private EditText cost;
    private EditText unit;
    private String preName;
    private Spinner vendor;
    private ArrayList<Food> foods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        foods = getFoodDatabase();

        imageView = findViewById(R.id.info_image);
        name = findViewById(R.id.info_name);
        description = (EditText) findViewById(R.id.info_description);
        cost = findViewById(R.id.info_cost);
        unit = findViewById(R.id.info_unit);
        vendor = findViewById(R.id.info_vendor);
        type = findViewById(R.id.info_type);
        ArrayAdapter<String> typeAdapter = new MySpinnerAdapter(this, android.R.layout.simple_list_item_1, Food.TYPE);
        ArrayList<String> vendorName = new ArrayList<>();
        for (Vendor v : vendors) {
            vendorName.add(v.getName());
        }
        ArrayAdapter<String> vendorAdapter = new MySpinnerAdapter(this, android.R.layout.simple_list_item_1, vendorName);
        type.setAdapter(typeAdapter);
        vendor.setAdapter(vendorAdapter);

        Intent intent = getIntent();
        if (intent.hasExtra(EDIT_FOOD)) {
            food = (Food) intent.getSerializableExtra(EDIT_FOOD);
            imageView.setImageBitmap(FoodStoreDatabaseHelper.loadImageFromStorage(food.getImg(), 132, 128));
            name.setText(food.getName());
            preName = food.getName().toLowerCase();
            description.setText(food.getDescription());
            cost.setText(String.valueOf(food.getCost()));
            unit.setText(food.getUnit());
            type.setSelection(typeAdapter.getPosition(food.getType()));
            vendor.setSelection(vendorAdapter.getPosition(food.getVendorName()));
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
                    for (Food food : foods) {
                        if (food.getName().toLowerCase().equals(text)) {
                            name.setError("Food's name already exists!");
                            break;
                        }
                    }

                }
                ;
            }
        });
    }

    public void onChangeImage(View view) {

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

                        if (imageView != null) {
                            imageView.setImageBitmap(selectedImage);
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
                    if (imageView != null) {
                        imageView.setImageBitmap(selectedImage);
                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }

    }

    public void onClickDone(View view) {

        if (name.getText().toString().isEmpty()) {
            name.setError("You did not enter food's name!");
            name.requestFocus();
            return;
        }
        if (name.getError() != null) {
            name.requestFocus();
            return;
        }
        if (description.getText().toString().isEmpty()) {
            description.setError("You did not enter food's description!");
            description.requestFocus();
            return;
        }
        if (cost.getText().toString().isEmpty()) {
            cost.setError("You did not enter food's cost!");
            cost.requestFocus();
            return;
        }
        if (unit.getText().toString().isEmpty()) {
            unit.setError("You did not enter food's name!");
            unit.requestFocus();
            return;
        }

        if (isDataChanged()) {

            if (food == null)
                food = new Food();
            food.setName(name.getText().toString());
            food.setDescription(description.getText().toString());
            food.setCost(Double.valueOf(cost.getText().toString()));
            food.setUnit(unit.getText().toString());
            food.setType(type.getSelectedItem().toString());
            String vendorNameSelected = vendor.getSelectedItem().toString();
            food.setVendorName(vendorNameSelected);
            for (Vendor v : vendors) {
                if (v.getName().equals(vendorNameSelected)) {
                    food.setVendorID(v.getId());
                    break;
                }
            }
            if (selectedImage != null)
                food.setImg(FoodStoreDatabaseHelper.saveToInternalStorage(selectedImage, food.getName(), FoodStoreDatabaseHelper.FOOD));
            else
                food.setImg(FoodStoreDatabaseHelper.saveDrawableToInternalStorage(R.drawable.default_user, food.getName(), FoodStoreDatabaseHelper.FOOD));
            new UpdateFoodTask().execute();
            Snackbar snackbar = Snackbar.make(findViewById(R.id.constraint), "Your food has been update", Snackbar.LENGTH_SHORT);
            snackbar.show();
            selectedImage = null;
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.constraint), "Nothing change!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

    }

    private ArrayList<Food> getFoodDatabase() {
        ArrayList<Food> foodList = new ArrayList<>();

        SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(this);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT FOOD.*, VENDOR.NAME FROM FOOD LEFT JOIN VENDOR ON FOOD.VENDORID = VENDOR._id", null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    String name = cursor.getString(1);
                    String type = cursor.getString(2);
                    String description = cursor.getString(3);
                    double cost = cursor.getDouble(5);
                    String image = cursor.getString(4);
                    String unit = cursor.getString(6);
                    long vendorID = cursor.getInt(7);
                    int amount = cursor.getInt(8);
                    String vendorName = cursor.getString(9);

                    Food food = new Food(id, name, type, description, image, cost, unit, vendorID, vendorName, amount);
                    foodList.add(food);

                    if (cursor.isLast()) {
                        break;
                    }
                    cursor.moveToNext();
                } while (true);
            }

            cursor = db.rawQuery("SELECT * FROM VENDOR", null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    String name = cursor.getString(1);
                    String image = cursor.getString(2);
                    String address = cursor.getString(3);
                    String email = cursor.getString(4);
                    String phone = cursor.getString(5);

                    Vendor vendor = new Vendor(id, name, image, address, email, phone);
                    vendors.add(vendor);
                    if (cursor.isLast() ) {
                        break;
                    }
                    cursor.moveToNext();
                } while(true);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return foodList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class UpdateFoodTask extends AsyncTask<Void, Void, Boolean> {


        private ContentValues foodValues;

        @Override
        protected void onPostExecute(Boolean done) {
            Intent returnIntent = new Intent();
            if (!done) {
                Toast toast = Toast.makeText(EditFoodActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
                setResult(RESULT_CANCELED, returnIntent);
            } else {
                returnIntent.putExtra(FoodDetailActivity.FOOD_INFO, food);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
            finish();
        }

        @Override
        protected void onPreExecute() {
            foodValues = new ContentValues();
            foodValues.put("NAME", food.getName());
            foodValues.put("TYPE", food.getType());
            foodValues.put("DESCRIPTION", food.getDescription());
            foodValues.put("IMAGE", food.getImg());
            foodValues.put("COST", food.getCost());
            foodValues.put("UNIT", food.getUnit());
            foodValues.put("AMOUNT", food.getAmount());
            foodValues.put("VENDORID", food.getVendorID());
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(EditFoodActivity.this);
            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                if (preName == null) {
                    long id = db.insert("FOOD", null, foodValues);
                    food.setId(id);

                } else {
                    db.update("FOOD", foodValues, "_id = ?", new String[]{String.valueOf(food.getId())});
                }

                db.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    private boolean isDataChanged() {

        if (food == null && preName == null) {
            return true;
        }
        if (selectedImage != null) {
            return true;
        }
        if (!food.getName().toLowerCase().equals(name.getText().toString())) {
            return true;
        }
        if (!food.getDescription().equals(description.getText().toString())) {
            return true;
        }
        if (food.getCost() != Double.parseDouble(cost.getText().toString())) {
            return true;
        }
        if (!food.getUnit().equals(unit.getText().toString())) {
            return true;
        }
        if (!food.getType().equals(type.getSelectedItem())) {
            return true;
        }
        if (!food.getVendorName().equals(vendor.getSelectedItem())) {
            return true;
        }
        return false;
    }

}
