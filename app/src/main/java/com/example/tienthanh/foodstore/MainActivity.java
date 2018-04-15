package com.example.tienthanh.foodstore;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int LOGIN = 90;
    private ShareActionProvider shareActionProvider;
    public static ArrayList<OrderDetail> cart;
    public static ArrayList<Food> foodCart;
    public static User user;
    private Fragment currentFragment;
    private NavigationView navigationView;

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
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        cart = new ArrayList<>();
        foodCart = new ArrayList<>();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fragment fragment = new FoodTypeFragment();

        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        FoodStoreDatabaseHelper.setContext(new ContextWrapper(getApplicationContext()));


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent = null;
        switch (id) {
            case R.id.nav_login:
                if (user != null) {
                    user = null;
                }
                intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, LOGIN);
                break;
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
                fragment = new OrderTypeFragment();
                setTitle("Order");
                break;
            case R.id.nav_bill_list:
                fragment = new BillTypeFragment();
                setTitle("Bill");
                break;
            default:
                fragment = new FoodTypeFragment();
                setTitle("Food");
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_cart:
                Intent intent = new Intent(this, CartDetailActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setNavMenu();
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem addItem = menu.findItem(R.id.action_add);
        addItem.setVisible(false);
        if (user != null && user.getPrivilege() == 0) {
            MenuItem cartItem = menu.findItem(R.id.action_view_cart);
            cartItem.setVisible(false);
            addItem.setVisible(true);
        }
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

    @Override
    protected void onStop() {
        super.onStop();
        currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (currentFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, currentFragment);
            ft.detach(currentFragment);
            ft.attach(currentFragment);
            ft.commit();
        }


    }


    private void setNavMenu() {

        Menu menu = navigationView.getMenu();
        ImageView imageView = findViewById(R.id.profile_image);
        TextView profileEmail = findViewById(R.id.profile_email);
        TextView profileName = findViewById(R.id.profile_name);
        if (MainActivity.user != null) {

            imageView.setImageBitmap(FoodStoreDatabaseHelper.loadImageFromStorage(user.getImg(), 200, 200));
            profileEmail.setText(user.getEmail());
            profileName.setText(user.getName());
            MenuItem item = menu.findItem(R.id.nav_login);
            item.setTitle("Logout");

            if (MainActivity.user.getPrivilege() == 3) {

                MenuItem userItem = menu.findItem(R.id.nav_user_list);
                userItem.setTitle("Your profile");
                MenuItem orderItem = menu.findItem(R.id.nav_order_list);
                orderItem.setTitle("Your order");
                MenuItem billItem = menu.findItem(R.id.nav_bill_list);
                billItem.setTitle("Your bill");

            } else {

                MenuItem userItem = menu.findItem(R.id.nav_user_list);
                userItem.setTitle("User");
                MenuItem orderItem = menu.findItem(R.id.nav_order_list);
                orderItem.setTitle("Order");
                MenuItem billItem = menu.findItem(R.id.nav_bill_list);
                billItem.setTitle("Bill");
                billItem.setVisible(true);
                orderItem.setVisible(true);
            }
        } else {

            MenuItem item = menu.findItem(R.id.nav_login);
            item.setTitle("Login");
            imageView.setImageResource(R.drawable.logo);
            profileName.setText(R.string.app_name_login);
            profileEmail.setText(R.string.enjoy);

            MenuItem orderItem = menu.findItem(R.id.nav_order_list);
            orderItem.setVisible(false);
            MenuItem billItem = menu.findItem(R.id.nav_bill_list);
            billItem.setVisible(false);
        }
    }
}
