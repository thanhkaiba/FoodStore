package com.example.tienthanh.foodstore;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class FoodDetailActivity extends AppCompatActivity {

    public static final String FOOD_INFO = "food_info";
    private static final int EDIT = 22;
    private Food food;
    final int[] number = {0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (intent.hasExtra(FOOD_INFO)) {
            food = (Food) intent.getSerializableExtra(FOOD_INFO);
            setUpView();
        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (MainActivity.user != null &&  MainActivity.user.getPrivilege() == 0) {
            getMenuInflater().inflate(R.menu.detail_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, EditFoodActivity.class);
                intent.putExtra(EditFoodActivity.EDIT_FOOD, food);
                startActivityForResult(intent, EDIT);
                return true;
            case R.id.action_delete:
                new DeleteFoodTask().execute(food.getId());
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickBuy(View view) {
        TextView qty = findViewById(R.id.sizeno);
        if (number[0] > 0) {
            for (OrderDetail od : MainActivity.cart) {
                if (od.getFoodName().equals(food.getName())) {
                    od.setAmount(od.getAmount() + number[0]);
                    finish();
                    return;
                }
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setFoodName(food.getName());
            orderDetail.setFoodID(food.getId());
            orderDetail.setCost(food.getCost());
            orderDetail.setAmount(Integer.parseInt(qty.getText().toString()));
            MainActivity.cart.add(orderDetail);
            MainActivity.foodCart.add(food);
            finish();
        } else {
            Toast.makeText(this, "You have not selected the quantity", Toast.LENGTH_SHORT).show();
        }
    }

    private class DeleteFoodTask extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean done) {
            if (!done) {
                Toast toast = Toast.makeText(FoodDetailActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                if (food.getImg() != null) {
                    File myPath = new File(food.getImg());
                    if (myPath.exists()) {
                        myPath.delete();
                    }
                }
                finish();
            }
        }


        @Override
        protected Boolean doInBackground(Long... ids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(FoodDetailActivity.this);

            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                long id = ids[0];
                db.delete("FOOD", "_id=?", new String[]{Long.toString(id)});
                db.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT) {
            if (resultCode == RESULT_OK) {
                food = (Food) data.getSerializableExtra(FoodDetailActivity.FOOD_INFO);
                setUpView();
            }
        }
    }

    private void setUpView() {

        ImageView imageView = findViewById(R.id.info_image);
        TextView description = findViewById(R.id.info_description);
        TextView cost = findViewById(R.id.info_cost);
        TextView vendorName = findViewById(R.id.info_vendor);
        TextView amount = findViewById(R.id.info_amount);
        TextView name = findViewById(R.id.info_name);
        ImageView plus = findViewById(R.id.plus);
        ImageView minus = findViewById(R.id.minus);
        final TextView qty = findViewById(R.id.sizeno);

        if (MainActivity.user == null || MainActivity.user.getPrivilege() != 2) {
            int size = MainActivity.foodCart.size();
            for (int i = 0; i < size; i++) {
                if (MainActivity.foodCart.get(i).getId() == food.getId()) {
                    food.setAmount(food.getAmount() - MainActivity.cart.get(i).getAmount());
                    if (food.getAmount() < 0) {
                        food.setAmount(0);
                    }
                    break;
                }
            }
        }

        imageView.setImageBitmap(FoodStoreDatabaseHelper.loadImageFromStorage(food.getImg(), 200, 300));
        name.setText(food.getName());
        String amountText = "There are " + food.getAmount() + " " + food.getUnit() + " in stock";
        amount.setText(amountText);
        description.setText(food.getDescription());
        String costText = food.getCost() + "/" + food.getUnit();
        cost.setText(costText);
        vendorName.setText(food.getVendorName());
        TextView buyButton = findViewById(R.id.buy);
        if (MainActivity.user != null && MainActivity.user.getPrivilege() == 0) {
            buyButton.setVisibility(View.GONE);
            plus.setVisibility(View.GONE);
            minus.setVisibility(View.GONE);
            qty.setVisibility(View.GONE);
        } else {


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

                    if (MainActivity.user == null || MainActivity.user.getPrivilege() != 2) {
                        if (number[0] == food.getAmount()) {
                            qty.setText("" + number[0]);
                        }

                        if (number[0] < food.getAmount()) {

                            number[0] = number[0] + 1;
                            qty.setText("" + number[0]);

                        }
                    } else {
                        number[0] = number[0] + 1;
                        qty.setText("" + number[0]);
                    }
                }
            });
        }
    }

}

