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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


public class BillListFragment extends Fragment implements BillListAdapter.Listener{


    public static final String TYPE = "type";
    private ArrayList<Bill> bills;
    private BillListAdapter adapter;
    private int billType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView billRecycler = (RecyclerView)  inflater.inflate(R.layout.fragment_bill_list, container, false);

        savedInstanceState = getArguments();
        if (savedInstanceState != null) {

            billType = savedInstanceState.getInt(TYPE);
        }

        bills = getBillDatabase();

        adapter = new BillListAdapter(bills);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        billRecycler.setLayoutManager(layoutManager);
        adapter.setListener(this);
        billRecycler.setAdapter(adapter);
        setHasOptionsMenu(true);
        return billRecycler;


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
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private ArrayList<Bill> getBillDatabase() {
        ArrayList<Bill> billList = new ArrayList<>();

        SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(getActivity());
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT BILL.*, USERS.NAME FROM BILL, USERS WHERE USERID = USERS._id AND TYPE = '" +
                    billType +"';", null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    Double total = cursor.getDouble(1);
                    String date = cursor.getString(2);
                    int type = cursor.getInt(3);
                    int amount = cursor.getInt(4);
                    int userID = cursor.getInt(5);
                    String userName = cursor.getString(6);

                    Bill bill = new Bill(id, total, date, type, amount, userID, userName);
                    billList.add(bill);
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
        return billList;
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), BillDetailActivity.class);
        intent.putExtra(BillDetailActivity.BILL_INFO, bills.get(position));
        startActivity(intent);
    }
}
