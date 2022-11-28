package com.abc.templatecustomerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.templatecustomerapp.Model.Cart;
import com.abc.templatecustomerapp.Model.MainTask;
import com.abc.templatecustomerapp.Model.User;
import com.abc.templatecustomerapp.Utils.callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    String orderItemsString = "";
    double total_amnt = 0.0;
    ProgressBar progressBar;
    JSONObject finalOrderObject = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Cart");
        }

        LinearLayout cartItemsLinearLayout = findViewById(R.id.cart_items_linear_layout);
        TextView cartTotalAmountTv = findViewById(R.id.cart_total_amount_tv);
        Button placeOrderButton = findViewById(R.id.place_order_btn);
        progressBar = findViewById(R.id.cart_progressbar);

        Cart cart = Cart.getCartInstance();
        HashMap<Integer, JSONObject> cartMap = cart.getCartMap();
        JSONArray orderArray = new JSONArray();

        try {
            for (int i : cartMap.keySet()) {
                JSONObject object = cartMap.get(i);
                String itemName = object.getString("itemName");
                int itemId = object.getInt("id");
                int qty = object.getInt("count");
                double price = Double.valueOf(object.getString("price"));
                double amount = price*qty;
                total_amnt += amount;

                JSONObject itemObject = new JSONObject();
                itemObject.put("itemId", String.valueOf(itemId));
                itemObject.put("name", itemName);
                itemObject.put("qty", String.valueOf(qty));
                orderArray.put(itemObject);

                View itemSummaryView = this.getLayoutInflater().inflate(R.layout.cart_item_layout, null);
                ((TextView)itemSummaryView.findViewById(R.id.cart_item_detail)).setText(itemName + " x " + qty);
                ((TextView)itemSummaryView.findViewById(R.id.cart_item_price)).setText(String.valueOf(amount));

                cartItemsLinearLayout.addView(itemSummaryView);
            }
            cartTotalAmountTv.setText(String.valueOf(total_amnt));
            orderItemsString = orderArray.toString();

            User user = User.getUserInstance();
            String addressString = user.getName() + "," + user.getAddress();

            Calendar calendar = Calendar.getInstance();
            String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);

            finalOrderObject.put("userId", user.getId());
            finalOrderObject.put("items", orderItemsString);
            finalOrderObject.put("address", addressString);
            finalOrderObject.put("contact", user.getContact());
            finalOrderObject.put("date", date);
            finalOrderObject.put("total_amnt", String.valueOf(total_amnt));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (Cart.getCartInstance().getCartMap().size() == 0) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CartActivity.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    MainTask.placeOrder(CartActivity.this, finalOrderObject, new callback<String>() {
                        @Override
                        public void onSucess(String s) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CartActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CartActivity.this, MainActivity.class));
                        }

                        @Override
                        public void onError(Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CartActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
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
}
