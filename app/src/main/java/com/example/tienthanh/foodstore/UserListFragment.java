package com.example.tienthanh.foodstore;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class UserListFragment extends Fragment implements CaptionImageAdapter.Listener {

    private ArrayList<User> users;
    CaptionImageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView userRecycler = (RecyclerView)  inflater.inflate(R.layout.fragment_user_list,
                container, false);

       users = getUsersDatabase();
        ArrayList<Info> infos = new ArrayList<>();


        for (User user : users) {
            Info info = new Info(user.getName(), user.getEmail(), user.getImg());
            infos.add(info);
        }

        adapter = new CaptionImageAdapter(infos);
        adapter.setType(CaptionImageAdapter.USER_ADAPTER);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        userRecycler.setLayoutManager(layoutManager);
        adapter.setListener(this);
        userRecycler.setAdapter(adapter);
        setHasOptionsMenu(true);
        return  userRecycler;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        search(searchView);
        super.onPrepareOptionsMenu(menu);
    }

    private ArrayList<User> getUsersDatabase() {
        ArrayList<User> userList = new ArrayList<User>();

        SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(getActivity());
        try {

            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM USERS", null);

            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    String password = cursor.getString(1);
                    String image = cursor.getString(2);
                    String name = cursor.getString(3);
                    String gender = cursor.getString(4);
                    String birthday = cursor.getString(5);
                    String email = cursor.getString(6);
                    String phone = cursor.getString(7);
                    int privilege = cursor.getInt(8);
                    String address = cursor.getString(9);

                    User user = new User(id, password, image, name, gender, birthday, email, phone, privilege, address);

                    userList.add(user);
                    if (cursor.isLast() ) {
                        break;
                    }
                    cursor.moveToNext();
                } while(true);
                cursor.close();
                db.close();
            }
        }catch (SQLiteException e) {
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return userList;
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), UserDetailActivity.class);
        intent.putExtra(UserDetailActivity.USER_INFO, users.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(getActivity(), EditUserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
