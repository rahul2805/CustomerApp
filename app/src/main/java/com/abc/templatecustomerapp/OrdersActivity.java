package com.abc.templatecustomerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.abc.templatecustomerapp.Model.MainTask;
import com.abc.templatecustomerapp.Utils.OrdersAdapter;
import com.abc.templatecustomerapp.Utils.callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    ExpandableListView ordersListView;
    ArrayList<JSONObject> pendingOrdersList = new ArrayList<>();
    ArrayList<JSONObject> completedOrdersList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ordersListView = findViewById(R.id.orders_list_view);
        swipeRefreshLayout = findViewById(R.id.orders_activity_swipe_layout);

        getOrdersData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrdersData();
            }
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Orders");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getOrdersData() {
        pendingOrdersList.clear();
        completedOrdersList.clear();
        swipeRefreshLayout.setRefreshing(true);

        MainTask.getData(OrdersActivity.this, "app/getMyOrders", new callback<String>() {
            @Override
            public void onSucess(String s) {
                swipeRefreshLayout.setRefreshing(false);
                addOrdersToList(s);
            }

            @Override
            public void onError(Exception e) {
                if (e.getMessage().equals("Invalid Token")) {
                    startActivity(new Intent(OrdersActivity.this, LoginActivity.class));
                }
                swipeRefreshLayout.setRefreshing(false);
                Log.d("Error", e.getMessage());
                Toast.makeText(OrdersActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addOrdersToList(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getInt("status") == 0) {
                    pendingOrdersList.add(jsonObject);
                } else {
                    completedOrdersList.add(jsonObject);
                }
            }
            OrdersAdapter ordersAdapter = new OrdersAdapter(OrdersActivity.this, pendingOrdersList, completedOrdersList);
            ordersListView.setAdapter(ordersAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
