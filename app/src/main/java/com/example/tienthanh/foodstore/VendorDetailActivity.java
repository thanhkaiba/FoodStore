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
    private Vendor vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView image = findViewById(R.id.info_image);
        TextView phone = findViewById(R.id.phone_info);
        TextView email = findViewById(R.id.email_info);
        TextView address = findViewById(R.id.info_address);

        Intent intent = getIntent();
        if (intent.hasExtra(VENDOR_INFO)) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            vendor= (Vendor) intent.getSerializableExtra(VENDOR_INFO);
            image.setImageBitmap(FoodStoreDatabaseHelper.loadImageFromStorage(vendor.getImg(), size.x, 300));
            setTitle(vendor.getName());

            email.setText(vendor.getEmail());
            phone.setText(vendor.getPhone());
            address.setText(vendor.getAddress());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.FRAGMENT, R.id.nav_vendor_list);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
               Intent intent = new Intent(this, EditVendorActivity.class);
                intent.putExtra(EditVendorActivity.EDIT_VENDOR, vendor);
                startActivity(intent);
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
            }
        }

        @Override
        protected Boolean doInBackground(Long... ids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(VendorDetailActivity.this);

            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                long id = ids[0];
                db.delete("VENDOR", "_id=?", new String[]{Long.toString(id)});
                File myPath = new File(vendor.getImg());
                if (myPath.exists()) {
                    myPath.delete();
                }
                db.close();
                Intent intent = new Intent(VendorDetailActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT, R.id.nav_vendor_list);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                e.getStackTrace();
                return false;
            }
        }
    }


}
