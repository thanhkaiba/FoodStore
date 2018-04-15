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


public class OrderListFragment extends Fragment implements OrderListAdapter.Listener{


    public static final String TYPE = "status";
    public static final int UNFINISHED = 0;
    public static final int FINISHED = 1;
    ArrayList<Order> orders;
    OrderListAdapter adapter;
    private int status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView orderRecycler = (RecyclerView)  inflater.inflate(R.layout.fragment_order_list, container, false);

        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            status = savedInstanceState.getInt(TYPE);
        }

        orders = getOrderDatabase();

        adapter = new OrderListAdapter(orders);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        orderRecycler.setLayoutManager(layoutManager);
        adapter.setListener(this);
        orderRecycler.setAdapter(adapter);
        if (MainActivity.user != null && MainActivity.user.getPrivilege() == 3) {
            adapter.getFilter().filter(MainActivity.user.getEmail());
        }
        setHasOptionsMenu(true);
        return  orderRecycler;
        
        
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem addItem = menu.findItem(R.id.action_add);
        addItem.setVisible(false);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        search(searchView);
        super.onPrepareOptionsMenu(menu);
    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (MainActivity.user != null && MainActivity.user.getPrivilege() == 3) {
                    newText += MainActivity.user.getEmail();
                }

                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private ArrayList<Order> getOrderDatabase() {
        ArrayList<Order> orderList = new ArrayList<Order>();

        SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(getActivity());
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM ORDERS WHERE STATUS = " + status + ";", null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    Double total = cursor.getDouble(1);
                    String date = cursor.getString(2);
                    String name = cursor.getString(3);
                    String email = cursor.getString(4);
                    String phone = cursor.getString(5);
                    String address = cursor.getString(6);
                    int amount = cursor.getInt(7);
                    int status = cursor.getInt(8);

                    Order order = new Order(id, total, date, name, email, phone, address, amount, status);
                    orderList.add(order);
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
        return orderList;
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra(OrderDetailActivity.ORDER_INFO, orders.get(position));
        startActivity(intent);
    }
}
