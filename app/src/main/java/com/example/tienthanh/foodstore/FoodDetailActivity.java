package com.example.tienthanh.foodstore;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class FoodDetailActivity extends AppCompatActivity {

    public static final String FOOD_INFO = "food_info";
    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        food = (Food) intent.getSerializableExtra(FOOD_INFO);
        ImageView imageView = findViewById(R.id.info_image);
        imageView.setImageBitmap(FoodStoreDatabaseHelper.loadImageFromStorage(food.getImg(), 200, 300));
        TextView name = findViewById(R.id.info_name);
        name.setText("Name: " + food.getName());
        TextView type = findViewById(R.id.info_type);
        type.setText("Type: " + food.getType());
        TextView description = findViewById(R.id.info_description);
        description.setText(food.getDescription());
        TextView cost = findViewById(R.id.info_cost);
        cost.setText(food.getCost() + "$ per " + food.getUnit());
        TextView vendorName = findViewById(R.id.info_vendor);
        vendorName.setText(food.getVendorName());

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
                new DeleteFoodTask().execute(food.getId());
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DeleteFoodTask extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean done) {
            if (!done) {
                Toast toast = Toast.makeText(FoodDetailActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        protected Boolean doInBackground(Long... ids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(FoodDetailActivity.this);

            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                long id = ids[0];
                db.delete("FOOD", "_id=?", new String[]{Long.toString(id)});
                File myPath = new File(food.getImg());
                if (myPath.exists()) {
                    myPath.delete();
                }
                db.close();
                Intent intent = new Intent(FoodDetailActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT, R.id.nav_food_list);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.FRAGMENT, R.id.nav_food_list);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

