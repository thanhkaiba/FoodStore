package com.example.tienthanh.foodstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodDetailActivity extends AppCompatActivity {

    public static final String FOOD_INFO = "food_info";
    private int id;
    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        food = (Food) intent.getSerializableExtra(FOOD_INFO);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, EditFoodActivity.class);
                intent.putExtra(EditFoodActivity.EDIT_FOOD, food);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                //do something
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

