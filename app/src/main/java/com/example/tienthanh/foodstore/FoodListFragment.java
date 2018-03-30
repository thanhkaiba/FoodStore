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

public class FoodListFragment extends Fragment implements CaptionImageAdapter.Listener{

    ArrayList<Food> foods;
    CaptionImageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView foodRecycler = (RecyclerView)  inflater.inflate(R.layout.fragment_food_list, container, false);

        foods = getFoodDatabase();
        ArrayList<Info> infos = new ArrayList<>();



        for (Food food : foods) {
           Info info = new Info(food.getName(), String.valueOf(food.getCost()), food.getImg());
           infos.add(info);
        }

        adapter = new CaptionImageAdapter(infos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        foodRecycler.setLayoutManager(layoutManager);
        adapter.setListener(this);
        foodRecycler.setAdapter(adapter);
        setHasOptionsMenu(true);
        return  foodRecycler;

    }

    private ArrayList<Food> getFoodDatabase() {
        ArrayList<Food> foodList = new ArrayList<Food>();

        SQLiteOpenHelper sqLiteOpenHelper = new FoodStoreDatabaseHelper(getActivity());
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.query("FOOD",
                    new String[]{"_id", "NAME", "TYPE", "DESCRIPTION", "IMAGE", "COST", "UNIT"},
                    null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(1);
                    String type = cursor.getString(2);
                    String description = cursor.getString(3);
                    double cost = cursor.getDouble(5);
                    String image = cursor.getString(4);
                    String unit = cursor.getString(6);
                    long id = cursor.getLong(0);

                    Food food = new Food(id, name, type, description, image, cost, unit);
                    foodList.add(food);
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
        return foodList;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        search(searchView);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(getActivity(), EditFoodActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(int position) {
       Intent intent = new Intent(getActivity(), FoodDetailActivity.class);

       intent.putExtra(FoodDetailActivity.FOOD_INFO, foods.get(position));
       startActivity(intent);
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
