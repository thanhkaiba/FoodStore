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

public class VendorListFragment extends Fragment implements CaptionImageAdapter.Listener{

    ArrayList<Vendor> vendors;
    CaptionImageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView vendorRecycler = (RecyclerView)  inflater.inflate(R.layout.fragment_vendor_list, container, false);

        vendors = getVendorDatabase();
        ArrayList<Info> infos = new ArrayList<>();

        for (Vendor vendor : vendors) {
            Info info = new Info(vendor.getName(), "", vendor.getImg());
            infos.add(info);
        }

        adapter = new CaptionImageAdapter(infos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        vendorRecycler.setLayoutManager(layoutManager);
        adapter.setListener(this);
        vendorRecycler.setAdapter(adapter);
        setHasOptionsMenu(true);
        return  vendorRecycler;

    }

    private ArrayList<Vendor> getVendorDatabase() {
        ArrayList<Vendor> vendorList = new ArrayList<Vendor>();

        SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(getActivity());
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM VENDOR", null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(0);
                    String name = cursor.getString(1);
                    String image = cursor.getString(2);
                    String address = cursor.getString(3);
                    String email = cursor.getString(4);
                    String phone = cursor.getString(5);

                    Vendor vendor = new Vendor(id, name, image, address, email, phone);
                    vendorList.add(vendor);
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
        return vendorList;
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
                Intent intent = new Intent(getActivity(), EditVendorActivity.class);
                startActivity(intent);
                return true;
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

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getActivity(), VendorDetailActivity.class);
        intent.putExtra(VendorDetailActivity.VENDOR_INFO, vendors.get(position));
        startActivity(intent);
    }
}
