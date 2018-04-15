package com.example.tienthanh.foodstore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MakeOrderActivity extends AppCompatActivity {

    private static final int LOGIN = 1;
    private TextView name;
    private TextView email;
    private TextView phone;
    private TextView address;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.info_name);
        email = findViewById(R.id.info_email);
        phone = findViewById(R.id.info_phone);
        address = findViewById(R.id.info_address);
        setUpView();

    }

    private void setUpView() {
        if (MainActivity.user != null) {
            name.setText(MainActivity.user.getName());
            email.setText(MainActivity.user.getEmail());
            phone.setText(MainActivity.user.getPhone());
            address.setText(MainActivity.user.getAddress());
            TextView login = findViewById(R.id.login);
            login.setVisibility(View.GONE);
        }
    }

    public void onClickLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (MainActivity.user != null) {
                setUpView();
            }
        }
    }

    public void onClickOrder(View view) {

        if (email.getText().toString().isEmpty()) {
            email.setError("You did not enter email!");
            email.requestFocus();
            return;
        }


        if (!isValidEmail(email.getText().toString())) {
            email.setError("Invalid email!");
            email.requestFocus();
            return;
        }


        if (name.getText().toString().isEmpty()) {
            name.setError("You did not enter name");
            name.requestFocus();
            return;
        }

        if (name.getText().toString().isEmpty()) {
            name.setError("You did not enter name!");
            name.requestFocus();
            return;
        }

        if (phone.getText().toString().isEmpty()) {
            phone.setError("You did not enter phone number!");
            phone.requestFocus();
            return;
        }

        if (address.getText().toString().isEmpty()) {
            address.setError("You did not enter address!");
            address.requestFocus();
            return;
        }

        double total = 0;
        for (OrderDetail orderDetail : MainActivity.cart) {
            total += orderDetail.getCost() * orderDetail.getAmount();
        }
        Calendar calendar = Calendar.getInstance();

        String date = calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
        order = new Order(total, date, name.getText().toString(), email.getText().toString(), phone.getText().toString(),
                address.getText().toString(), MainActivity.cart.size(), 0);
        new MakeOrderTask().execute();

    }

    private boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private class MakeOrderTask extends AsyncTask<Void, Void, Boolean> {

        private ContentValues orderValues;
        private ContentValues foodValues;

        @Override
        protected void onPostExecute(Boolean done) {
            Intent returnIntent = new Intent();
            if (!done) {
                Toast toast = Toast.makeText(MakeOrderActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
                setResult(RESULT_CANCELED, returnIntent);
            } else {
                MainActivity.cart.clear();
                MainActivity.foodCart.clear();
                setResult(RESULT_OK, returnIntent);
            }
            finish();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(MakeOrderActivity.this);

            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                long id = db.insert("ORDERS", null, orderValues);
                ContentValues orderDetailValue = new ContentValues();

                for (int i = 0; i < MainActivity.cart.size(); i++) {
                    OrderDetail orderDetail = MainActivity.cart.get(i);
                    Food food = MainActivity.foodCart.get(i);
                    orderDetailValue.put("ORDERID", id);
                    orderDetailValue.put("FOODID", orderDetail.getFoodID());
                    orderDetailValue.put("AMOUNT ", orderDetail.getAmount());
                    orderDetailValue.put("COST", orderDetail.getCost());
                    db.insert("ORDERDETAIL", null, orderDetailValue);
                    foodValues.put("AMOUNT", food.getAmount() - orderDetail.getAmount());
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
            orderValues = new ContentValues();
            foodValues = new ContentValues();
            orderValues.put("TOTAL", order.getTotal());
            orderValues.put("DATE", order.getDate());
            orderValues.put("NAME", order.getName());
            orderValues.put("EMAIL", order.getEmail());
            orderValues.put("PHONE", order.getPhone());
            orderValues.put("ADDRESS", order.getAddress());
            orderValues.put("AMOUNT", order.getAmount());
            orderValues.put("STATUS ", order.getStatus());
        }
    }
}
