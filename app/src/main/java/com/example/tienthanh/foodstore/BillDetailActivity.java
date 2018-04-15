package com.example.tienthanh.foodstore;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BillDetailActivity extends AppCompatActivity {

    public static final String BILL_INFO = "bill info";
    private Bill bill;
    private ArrayList<OrderDetail> details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(BILL_INFO)) {
            bill = (Bill)intent.getSerializableExtra(BILL_INFO);
            TextView id = findViewById(R.id.info_id);
            TextView userID = findViewById(R.id.info_user_id);
            TextView userName = findViewById(R.id.info_name_user);
            TextView type = findViewById(R.id.info_type);
            TextView total = findViewById(R.id.info_total);
            TextView date = findViewById(R.id.info_date);
            date.setText(bill.getDate());
            total.setText(String.valueOf(bill.getTotal()));
            String info_id = "ID: " + String.valueOf(bill.getId());
            id.setText(info_id);
            userName.setText(bill.getUserName());
            if (bill.getType() == Bill.SELL) {
                userID.setText("Salesman / Saleswoman ID:"  + bill.getUserID());
            } else {
                userID.setText("Storekeeper ID:"  + bill.getUserID());
            }
            switch (bill.getType()) {
                case Bill.SELL:
                    type.setText("Sell goods");
                    break;
                case Bill.RECEIPT:
                    type.setText("Receipt goods");
                    break;
                case Bill.ISSUE:
                    type.setText("Issue goods");
            }
        }
        RecyclerView recyclerView =  findViewById(R.id.recyclerview);
        details = getBillDetailDatabase();

        OrderRecyclerAdapter adapter = new OrderRecyclerAdapter(this, details);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<OrderDetail> getBillDetailDatabase() {
        ArrayList<OrderDetail> detailList = new ArrayList<OrderDetail>();

        SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(this);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT BILLDETAIL.*, FOOD.NAME FROM BILLDETAIL, FOOD WHERE BILLID = " +
                    + bill.getId() + " AND FOOD._id = FOODID;", null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    int billID = cursor.getInt(1);
                    int foodID = cursor.getInt(2);
                    int amount = cursor.getInt(3);
                    Double cost = cursor.getDouble(4);
                    String foodName = cursor.getString(5);

                    OrderDetail orderDetail = new OrderDetail(id, billID, foodID, amount, cost, foodName);
                    detailList.add(orderDetail);

                    cursor.moveToNext();
                } while(!cursor.isAfterLast());
                cursor.close();
                db.close();
            }
        }catch (SQLiteException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return detailList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
