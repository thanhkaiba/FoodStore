package com.example.tienthanh.foodstore;


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


public class UserListFragment extends Fragment {


    private ArrayList<User> users;
    CaptionImageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView userRecycler = (RecyclerView)  inflater.inflate(R.layout.fragment_user_list, container, false);

       users = getUsersDatabase();
        ArrayList<Info> infos = new ArrayList<>();


        for (User user : users) {
            Info info = new Info(user.getName(), "", user.getImg());
            infos.add(info);
        }

        adapter = new CaptionImageAdapter(infos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        userRecycler.setLayoutManager(layoutManager);
        //adapter.setListener(this);
        userRecycler.setAdapter(adapter);
        setHasOptionsMenu(true);
        return  userRecycler;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        search(searchView);
        super.onCreateOptionsMenu(menu, inflater);

    }

    private ArrayList<User> getUsersDatabase() {
        ArrayList<User> userList = new ArrayList<User>();

        SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(getActivity());
        try {

            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM USERS", null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String loginName = cursor.getString(1);
                    String password = cursor.getString(2);
                    int image = cursor.getInt(3);
                    String name = cursor.getString(4);
                    String gender = cursor.getString(5);
                    String birthday = cursor.getString(6);
                    String email = cursor.getString(7);
                    String phone = cursor.getString(8);
                    int privilege = cursor.getInt(9);

                    User user = new User(id, loginName, password, image, name, gender, birthday, email, phone, privilege);

                    userList.add(user);
                    if (cursor.isLast() ) {
                        break;
                    }
                    cursor.moveToNext();
                } while(true);
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

}
