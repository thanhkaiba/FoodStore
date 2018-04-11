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

public class UserDetailActivity extends AppCompatActivity {

    public static final String USER_INFO = "user info";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ImageView image = findViewById(R.id.info_image);
        TextView phone = findViewById(R.id.phone_info);
        TextView email = findViewById(R.id.email_info);
        TextView birthday = findViewById(R.id.birthday_info);
        TextView gender = findViewById(R.id.gender_info);
        TextView privilege = findViewById(R.id.privilege_info);
        TextView address = findViewById(R.id.info_address);


        Intent intent = getIntent();
        if (intent.hasExtra(USER_INFO)) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            user = (User)intent.getSerializableExtra(USER_INFO);
            image.setImageBitmap(FoodStoreDatabaseHelper.loadImageFromStorage(user.getImg(), size.x, 300));
            setTitle(user.getName());

            privilege.setText(User.PRIVILEGE[user.getPrivilege()]);
            gender.setText(user.getGender());
            birthday.setText(user.getBirthday());
            email.setText(user.getEmail());
            phone.setText(user.getPhone());
            address.setText(user.getAddress());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

 /*   @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.FRAGMENT, R.id.nav_user_list);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, EditUserActivity.class);
                intent.putExtra(EditUserActivity.EDIT_USER, user);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                new DeleteUserTask().execute(user.getId());
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
                Toast toast = Toast.makeText(UserDetailActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                if (user.getImg() != null) {
                    File myPath = new File(user.getImg());
                    if (myPath.exists()) {
                        myPath.delete();
                    }
                }
                onBackPressed();
            }
        }


        @Override
        protected Boolean doInBackground(Long... ids) {
            SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(UserDetailActivity.this);

            try {
                SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
                long id = ids[0];
                db.delete("USERS", "_id=?", new String[]{Long.toString(id)});
                db.close();
                /*Intent intent = new Intent(UserDetailActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT, R.id.nav_user_list);
                startActivity(intent);*/

                return true;
            } catch (Exception e) {
                e.getStackTrace();
                return false;
            }
        }
    }


}
