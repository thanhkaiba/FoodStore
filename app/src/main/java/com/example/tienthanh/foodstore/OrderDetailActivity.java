package com.example.tienthanh.foodstore;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {

    public static final String ORDER_INFO = "order info";
    private Order order;
    private ArrayList<OrderDetail> details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(ORDER_INFO)) {
            order = (Order) intent.getSerializableExtra(ORDER_INFO);
            TextView id = findViewById(R.id.info_id);
            TextView name = findViewById(R.id.info_name);
            TextView phone = findViewById(R.id.info_phone);
            TextView email = findViewById(R.id.info_email);
            TextView address = findViewById(R.id.info_address);
            TextView date = findViewById(R.id.info_date);
            TextView total = findViewById(R.id.info_total);

            total.setText(String.valueOf(order.getTotal()));
            id.setText("Id:" + String.valueOf(order.getId()));
            name.setText("Name:" + order.getName());
            phone.setText("Phone: "+ order.getPhone());
            email.setText("Email: "+ order.getEmail());
            date.setText("Date: "+ order.getDate());
            address.setText("Address:" + order.getAddress());
        }

        ListView listView =  findViewById(R.id.list_view);
        details = getOrderDetailDatabase();

        listViewAdapter adapter = new listViewAdapter(this, details);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        MenuItem editAction = menu.findItem(R.id.action_edit);
        editAction.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                //Intent intent = new Intent(this, EditVendorActivity.class);
                //intent.putExtra(EditVendorActivity.EDIT_VENDOR, vendor);
                //startActivity(intent);
                return true;
            case R.id.action_delete:
                new DeleteOrderTask().execute(order.getId());
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<OrderDetail> getOrderDetailDatabase() {
        ArrayList<OrderDetail> detailList = new ArrayList<OrderDetail>();

        SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(this);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT ORDERDETAIL.*, FOOD.NAME FROM ORDERDETAIL, FOOD WHERE ORDERID = " +
                    + order.getId() + " AND FOOD._id = FOODID;", null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    int orderID = cursor.getInt(1);
                    int foodID = cursor.getInt(2);
                    int amount = cursor.getInt(3);
                    Double cost = cursor.getDouble(4);
                    String foodName = cursor.getString(5);

                    OrderDetail orderDetail = new OrderDetail(id, orderID, foodID, amount, cost, foodName);
                    detailList.add(orderDetail);

                    if (cursor.isLast() ) {
                        break;
                    }
                    cursor.moveToNext();
                } while(true);
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

    private class DeleteOrderTask extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean done) {
            if (!done) {
                Toast toast = Toast.makeText(OrderDetailActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        protected Boolean doInBackground(Long... ids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(OrderDetailActivity.this);

            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                long id = ids[0];
                db.delete("ORDERS", "_id=?", new String[]{Long.toString(id)});
                db.close();
                Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT, R.id.nav_order_list);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                e.getStackTrace();
                return false;
            }
        }
    }
}
