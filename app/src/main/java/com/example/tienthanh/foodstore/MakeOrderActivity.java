package com.example.tienthanh.foodstore;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MakeOrderActivity extends AppCompatActivity {

    private static final int LOGIN = 1;
    private TextView name;
    private TextView email;
    private TextView phone;
    private TextView address;

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
}
