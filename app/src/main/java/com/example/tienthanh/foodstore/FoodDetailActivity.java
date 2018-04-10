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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

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
        name.setText(food.getName());
        TextView amount = findViewById(R.id.info_amount);
        String amountText = "There are " +food.getAmount()+ " " + food.getUnit() + " in stock";
        amount.setText(amountText);
        TextView description = findViewById(R.id.info_description);
        description.setText(food.getDescription());
        TextView cost = findViewById(R.id.info_cost);
        String costText = food.getCost() + "$ / " + food.getUnit();
        cost.setText(costText);
        TextView vendorName = findViewById(R.id.info_vendor);
        vendorName.setText(food.getVendorName());

        ImageView plus =  findViewById(R.id.plus);
        ImageView minus =  findViewById(R.id.minus);
        final TextView qty = findViewById(R.id.sizeno);

        final int[] number = {0};
        qty.setText("" + number[0]);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number[0] == 0) {
                    qty.setText("" + number[0]);
                }

                if (number[0] > 0) {

                    number[0] = number[0] - 1;
                    qty.setText("" + number[0]);
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number[0] == food.getAmount()) {
                    qty.setText("" + number[0]);
                }

                if (number[0] < food.getAmount()) {

                    number[0] = number[0] + 1;
                    qty.setText("" + number[0]);

                }
            }
        });
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

    public void onClickBuy(View view) {
        TextView qty = findViewById(R.id.sizeno);
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setFoodName(food.getName());
        orderDetail.setFoodID(food.getId());
        orderDetail.setCost(food.getCost());
        orderDetail.setAmount(Integer.parseInt(qty.getText().toString()));
        MainActivity.cart.add(orderDetail);
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
        protected void onPreExecute() {
            if (food.getImg() != null) {
                File myPath = new File(food.getImg());
                if (myPath.exists()) {
                    myPath.delete();
                }
            }
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Long... ids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(FoodDetailActivity.this);

            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                long id = ids[0];
                db.delete("FOOD", "_id=?", new String[]{Long.toString(id)});

                db.close();
                Intent intent = new Intent(FoodDetailActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT, R.id.nav_food_list);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }




}

