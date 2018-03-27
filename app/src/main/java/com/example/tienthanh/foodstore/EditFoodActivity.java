package com.example.tienthanh.foodstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditFoodActivity extends AppCompatActivity {

    public static final String EDIT_FOOD = "Edit Food";
    Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(EDIT_FOOD)) {
            food = (Food) intent.getSerializableExtra(EDIT_FOOD);
            ImageView imageView = findViewById(R.id.info_image);
            imageView.setImageResource(food.getImg());
            EditText name = (EditText) findViewById(R.id.info_name);
            name.setText("Name: " + food.getName());
            TextView description = (TextView) findViewById(R.id.info_description);
            description.setText(food.getDescription());
            EditText cost = (EditText) findViewById(R.id.info_cost);
            cost.setText(String.valueOf(food.getCost()));
            EditText unit = (EditText) findViewById(R.id.info_unit);
            unit.setText(food.getUnit());

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
