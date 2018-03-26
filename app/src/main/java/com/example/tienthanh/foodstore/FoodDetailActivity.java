package com.example.tienthanh.foodstore;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodDetailActivity extends AppCompatActivity {

    public static final String FOOD_INFO = "food_info";
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Food food = (Food)intent.getSerializableExtra(FOOD_INFO);
        id = food.getId();
        ImageView imageView = findViewById(R.id.info_image);
        imageView.setImageResource(food.getImg());
        TextView name = (TextView) findViewById(R.id.info_name);
        name.setText("Name: " + food.getName());
        TextView type = (TextView) findViewById(R.id.info_type);
        type.setText("Type: " + food.getType());
        TextView description = (TextView) findViewById(R.id.info_description);
        description.setText(food.getDescription());
        TextView cost = (TextView) findViewById(R.id.info_cost);
        cost.setText(food.getCost() + "$ per " + food.getUnit());

    }
}
