package com.example.tienthanh.foodstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class EditFoodActivity extends AppCompatActivity {

    public static final String EDIT_FOOD = "Edit Food";
    private static final int RESULT_LOAD_IMG = 100;
    private Food food;
    private ImageView imageView;
    private EditText name;
    private TextView description;
    private Spinner type;
    private EditText cost;
    private EditText unit;
    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView imageView = (ImageView) findViewById(R.id.info_image);
        EditText name = (EditText) findViewById(R.id.info_name);
        EditText description = (EditText) findViewById(R.id.info_description);
        EditText cost = (EditText) findViewById(R.id.info_cost);
        EditText unit = (EditText) findViewById(R.id.info_unit);
        Spinner type = (Spinner) findViewById(R.id.info_type);


        Intent intent = getIntent();
        if (intent.hasExtra(EDIT_FOOD)) {
            food = (Food) intent.getSerializableExtra(EDIT_FOOD);
            imageView.setImageBitmap(FoodStoreDatabaseHelper.loadImageFromStorage(food.getImg()));
            name.setText(food.getName());
            description.setText(food.getDescription());
            cost.setText(String.valueOf(food.getCost()));
            unit.setText(food.getUnit());

        }
    }

    public void onChangeImage(View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                ImageView imageView = (ImageView) findViewById(R.id.info_image);
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

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
    }


    public void onClickDone(View view) {
        imageView = (ImageView) findViewById(R.id.info_image);
        if (food == null) {
            food = new Food();
            food.setName(name.getText().toString());
            food.setDescription(description.getText().toString());
            food.setCost(Double.valueOf(cost.getText().toString()));
            food.setUnit(unit.getText().toString());
            food.setType(type.getSelectedItem().toString());
        }


        CharSequence text = "Your food has been update";
        int duration = Snackbar.LENGTH_SHORT;
        Snackbar snackbar = Snackbar.make(findViewById(R.id.constraint), text, duration);
        snackbar.setAction("Undo", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(EditFoodActivity.this, "Undone", Toast.LENGTH_SHORT).show();
            }
        });
        snackbar.show();
    }



}
