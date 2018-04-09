package com.example.tienthanh.foodstore;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ShareActionProvider shareActionProvider;
    public static final String FRAGMENT = "fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Intent intent = getIntent();
        Fragment fragment = new FoodTypeFragment();

        if (intent.hasExtra(FRAGMENT)) {
            int id = intent.getExtras().getInt(FRAGMENT);
            switch (id) {
                case R.id.nav_food_list:
                    fragment = new FoodTypeFragment();
                    break;
                case R.id.nav_user_list:
                    fragment = new UserListFragment();
                    break;
                case R.id.nav_vendor_list:
                    fragment = new VendorListFragment();
                    break;
                case R.id.nav_order_list:
                    fragment = new OrderListFragment();
                    break;
                case R.id.nav_bill_list:
                    fragment = new BillListFragment();
                    break;
                default:
                    fragment = new FoodTypeFragment();
            }
        }
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        FoodStoreDatabaseHelper.setContext(new ContextWrapper(getApplicationContext()));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment;
        Intent intent = null;
        switch (id) {
            case R.id.nav_food_list:
                fragment = new FoodTypeFragment();
                setTitle("Food");
                break;
            case R.id.nav_user_list:
                fragment = new UserListFragment();
                setTitle("User");
                break;
            case R.id.nav_vendor_list:
                fragment = new VendorListFragment();
                setTitle("Vendor");
                break;
            case R.id.nav_order_list:
                fragment = new OrderListFragment();
                break;
            case R.id.nav_bill_list:
                fragment = new BillListFragment();
                break;
            default:
                fragment = new FoodTypeFragment();
                setTitle("Food");
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        } else {
            startActivity(intent);
        }
        DrawerLayout drawerLayout =  findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareActionIntent();
        return super.onCreateOptionsMenu(menu);
    }


    private void setShareActionIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Let use FoodStore app!");
       shareActionProvider.setShareIntent(intent);
    }


}
