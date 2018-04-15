package com.example.tienthanh.foodstore;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class VendorDetailActivity extends AppCompatActivity {

    public static final String VENDOR_INFO = "vendor info";
    private static final int EDIT = 21;
    private Vendor vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent intent = getIntent();
        if (intent.hasExtra(VENDOR_INFO)) {
            vendor= (Vendor) intent.getSerializableExtra(VENDOR_INFO);
            setUpView();
        }
        setTitle(vendor.getName());

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
               Intent intent = new Intent(this, EditVendorActivity.class);
                intent.putExtra(EditVendorActivity.EDIT_VENDOR, vendor);
                startActivityForResult(intent, EDIT);
                return true;
            case R.id.action_delete:
                new DeleteUserTask().execute(vendor.getId());
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DeleteUserTask extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean done) {
            if (!done) {
                Toast toast = Toast.makeText(VendorDetailActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                if (vendor.getImg() != null) {
                    File myPath = new File(vendor.getImg());
                    if (myPath.exists()) {
                        myPath.delete();
                    }
                }
                finish();
            }
        }



        @Override
        protected Boolean doInBackground(Long... ids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(VendorDetailActivity.this);

            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                long id = ids[0];
                db.delete("VENDOR", "_id=?", new String[]{Long.toString(id)});
                db.close();

                return true;
            } catch (Exception e) {
                e.getStackTrace();
                return false;
            }
        }
    }

    private void setUpView() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ImageView image = findViewById(R.id.info_image);
        TextView phone = findViewById(R.id.phone_info);
        TextView email = findViewById(R.id.email_info);
        TextView address = findViewById(R.id.info_address);
        image.setImageBitmap(FoodStoreDatabaseHelper.loadImageFromStorage(vendor.getImg(), size.x, 300));
        email.setText(vendor.getEmail());
        phone.setText(vendor.getPhone());
        address.setText(vendor.getAddress());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT) {
            if (resultCode == RESULT_OK) {
                vendor = (Vendor)data.getSerializableExtra(VendorDetailActivity.VENDOR_INFO);
                setUpView();
            }
        }
    }
}
