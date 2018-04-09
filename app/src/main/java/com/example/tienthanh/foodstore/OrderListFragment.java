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


public class OrderListFragment extends Fragment implements CaptionImageAdapter.Listener{


    ArrayList<Order> orders;
    CaptionImageAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView orderRecycler = (RecyclerView)  inflater.inflate(R.layout.fragment_order_list, container, false);

        orders = getOrderDatabase();
        ArrayList<Info> infos = new ArrayList<>();


        for (Order order : orders) {
            Info info = new Info(order.getName() + "\n" + order.getStatus(), String.valueOf(order.getTotal()), null);
            infos.add(info);
        }

        adapter = new CaptionImageAdapter(infos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        orderRecycler.setLayoutManager(layoutManager);
        adapter.setListener(this);
        adapter.setType(CaptionImageAdapter.ORDER_ADAPTER);
        orderRecycler.setAdapter(adapter);
        setHasOptionsMenu(true);
        return  orderRecycler;
        
        
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        search(searchView);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                /*Intent intent = new Intent(getActivity(), EditOrderActivity.class);
                startActivity(intent);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }

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

    private ArrayList<Order> getOrderDatabase() {
        ArrayList<Order> orderList = new ArrayList<Order>();

        SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(getActivity());
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM ORDERS", null);
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
