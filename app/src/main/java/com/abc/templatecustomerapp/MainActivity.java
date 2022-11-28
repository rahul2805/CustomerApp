package com.abc.templatecustomerapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.abc.templatecustomerapp.Model.Cart;
import com.abc.templatecustomerapp.Model.MainTask;
import com.abc.templatecustomerapp.Model.User;

import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.abc.templatecustomerapp.Utils.ItemsAdapter;
import com.abc.templatecustomerapp.Utils.callback;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ArrayList<JSONObject> itemsArrayList = new ArrayList<>();
    ListView itemsListView;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        itemsListView = findViewById(R.id.items_list_view);
        swipeRefreshLayout = findViewById(R.id.items_refresh_layout);

        getItemsData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getItemsData();
            }
        });

        User user = User.getUserInstance();
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_header_name)).setText("Hey, " + user.getName());
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_header_email)).setText(user.getEmail());


        if (drawer != null) {
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_orders:
                        startActivity(new Intent(MainActivity.this, OrdersActivity.class));
                        break;
                    case R.id.nav_cart:
                        startActivity(new Intent(MainActivity.this, CartActivity.class));
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                Network.clearUserData(this);
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.menu_cart:
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                break;
            default:
                break;
        }
        return false;
    }

    private void getItemsData() {
        swipeRefreshLayout.setRefreshing(true);
        itemsArrayList.clear();
        Cart.getCartInstance().makeNull();
        MainTask.getData(MainActivity.this, "app/getItems", new callback<String>() {
            @Override
            public void onSucess(String s) {
                swipeRefreshLayout.setRefreshing(false);
                addItemsToList(s);
            }

            @Override
            public void onError(Exception e) {
                if (e.getMessage().equals("Invalid Token")) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                swipeRefreshLayout.setRefreshing(false);
                Log.d("Error", e.getMessage());
                Toast.makeText(MainActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addItemsToList(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i ++) {
                JSONObject object = jsonArray.getJSONObject(i);
                itemsArrayList.add(object);
            }
            ItemsAdapter itemsAdapter = new ItemsAdapter(MainActivity.this, 0, itemsArrayList);
            itemsListView.setAdapter(itemsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
