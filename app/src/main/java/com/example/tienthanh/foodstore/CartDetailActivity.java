package com.example.tienthanh.foodstore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class CartDetailActivity extends AppCompatActivity {


    private static final int MAKE_ORDER = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        CartRecycleAdapter adapter = new CartRecycleAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        TextView orderButton = findViewById(R.id.order_button);
        if (MainActivity.cart.isEmpty()) {
            orderButton.setEnabled(false);
        }
        if (MainActivity.user != null && MainActivity.user.getPrivilege() == 1) {
            orderButton.setText("SELL NOW");
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
       if (MainActivity.user == null ) {
           Intent intent = new Intent(this, MakeOrderActivity.class);
           startActivityForResult(intent, MAKE_ORDER);
       } else if (MainActivity.user.getPrivilege() == 1){
           new MakeBillTask().execute();
           finish();
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

        @Override
        protected void onPostExecute(Boolean done) {
            Intent returnIntent = new Intent();
            if (!done) {
                Toast toast = Toast.makeText(CartDetailActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
                setResult(RESULT_CANCELED, returnIntent);
            } else {
                MainActivity.cart.clear();
                setResult(RESULT_OK, returnIntent);
            }
            finish();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(CartDetailActivity.this);

            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                long id = db.insert("BILL", null, billValues);
                ContentValues billDetailValue = new ContentValues();

                for (OrderDetail orderDetail : MainActivity.cart) {
                    billDetailValue.put("BILLID", id);
                    billDetailValue.put("FOODID", orderDetail.getFoodID());
                    billDetailValue.put("AMOUNT ", orderDetail.getAmount());
                    billDetailValue.put("COST", orderDetail.getCost());
                    db.insert("BILLDETAIL", null, billDetailValue);
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
            if (MainActivity.user.getPrivilege() == 1) {
                billValues.put("TYPE", Bill.SELL);
            }

        }
    }
}
