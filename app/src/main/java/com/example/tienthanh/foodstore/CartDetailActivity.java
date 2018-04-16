package com.example.tienthanh.foodstore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

public class CartDetailActivity extends AppCompatActivity {


    private static final int MAKE_ORDER = 15;
    private int billType;
    RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final CartRecycleAdapter adapter = new CartRecycleAdapter(this);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        final TextView orderButton = findViewById(R.id.order_button);
        if (MainActivity.cart.isEmpty()) {
            orderButton.setEnabled(false);
        }
        if (MainActivity.user != null && MainActivity.user.getPrivilege() == 1) {
            orderButton.setText(R.string.order_button_sell);
            billType = Bill.SELL;
        }
        if (MainActivity.user != null && MainActivity.user.getPrivilege() == 2) {
            RadioGroup vendorBillType = findViewById(R.id.vendor_bill_type);
            orderButton.setText(R.string.order_button_receipt);
            vendorBillType.setVisibility(View.VISIBLE);
            billType = Bill.RECEIPT;
            vendorBillType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.receipt:
                            orderButton.setText(R.string.order_button_receipt);
                            billType = Bill.RECEIPT;
                            for (int i = 0; i < MainActivity.cart.size(); i++) {
                                    View v = mLayoutManager.findViewByPosition(i);
                                    v.setBackgroundColor(Color.WHITE);
                            }

                            break;
                        case R.id.issue:
                            orderButton.setText(R.string.order_button_issue);
                            for (int i = 0; i < MainActivity.cart.size(); i++) {
                                if (MainActivity.cart.get(i).getAmount() > MainActivity.foodCart.get(i).getAmount()) {
                                    View v = mLayoutManager.findViewByPosition(i);
                                    v.setBackgroundColor(Color.RED);

                                }
                            }
                            billType = Bill.ISSUE;
                            break;
                    }
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onClickOrder(View view) {
       if (MainActivity.user == null || MainActivity.user.getPrivilege() == 3) {
           Intent intent = new Intent(this, MakeOrderActivity.class);
           startActivityForResult(intent, MAKE_ORDER);
       } else if (MainActivity.user.getPrivilege() == 1 || MainActivity.user.getPrivilege() == 2){
           boolean flag = true;
           if (billType == Bill.ISSUE) {
               for (int i = 0; i < MainActivity.cart.size(); i++) {
                   if (MainActivity.cart.get(i).getAmount() > MainActivity.foodCart.get(i).getAmount()) {
                       View v = mLayoutManager.findViewByPosition(i);
                       v.setBackgroundColor(Color.RED);
                       flag = false;
                   }
               }
           }
           if (flag) {
               new MakeBillTask().execute();
               finish();
           } else {
               Toast.makeText(this, "Some products have larger" +
                       " quantities than their stock", Toast.LENGTH_SHORT).show();
           }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAKE_ORDER) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    private class MakeBillTask extends AsyncTask<Void, Void, Boolean> {

        private ContentValues billValues;
        private ContentValues foodValues;

        @Override
        protected void onPostExecute(Boolean done) {
            if (!done) {
                Toast toast = Toast.makeText(CartDetailActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                MainActivity.cart.clear();
                MainActivity.foodCart.clear();
                finish();
            }

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(CartDetailActivity.this);

            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                long id = db.insert("BILL", null, billValues);
                ContentValues billDetailValue = new ContentValues();

                for (int i = 0; i < MainActivity.cart.size(); i++) {
                    OrderDetail orderDetail = MainActivity.cart.get(i);
                    Food food = MainActivity.foodCart.get(i);
                    billDetailValue.put("BILLID", id);
                    billDetailValue.put("FOODID", orderDetail.getFoodID());
                    billDetailValue.put("AMOUNT ", orderDetail.getAmount());
                    billDetailValue.put("COST", orderDetail.getCost());
                    db.insert("BILLDETAIL", null, billDetailValue);
                    if (billType != Bill.RECEIPT)
                        foodValues.put("AMOUNT", food.getAmount() - orderDetail.getAmount());
                    else
                        foodValues.put("AMOUNT", food.getAmount() + orderDetail.getAmount());
                    db.update("FOOD", foodValues, "_id = ?", new String[]{String.valueOf(food.getId())});
                }

                db.close();
                return true;
            } catch (Exception e) {
                e.getStackTrace();
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            billValues = new ContentValues();
            foodValues = new ContentValues();
            double total = 0;
            for (OrderDetail orderDetail : MainActivity.cart) {
                total += orderDetail.getCost() * orderDetail.getAmount();
            }
            Calendar calendar = Calendar.getInstance();

            String date = calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
            billValues.put("TOTAL", total);
            billValues.put("DATE", date);
            billValues.put("AMOUNT", MainActivity.cart.size());
            billValues.put("USERID", MainActivity.user.getId());
            billValues.put("TYPE", billType);

        }
    }
}
