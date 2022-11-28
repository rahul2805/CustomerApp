package com.abc.templatecustomerapp.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Cart {
    private static Cart mCart;
    private HashMap<Integer, JSONObject> cartMap;

    public static Cart getCartInstance() {
        if (mCart == null) {
            mCart = new Cart();
            mCart.cartMap = new HashMap<>();
        }
        return mCart;
    }

    public void addItemToCart(JSONObject object, int count) {
        try {
            Integer itemId = object.getInt("id");
            if (mCart.cartMap.containsKey(itemId)) {
                int currCount = mCart.cartMap.get(itemId).getInt("count");
                object.put("count", currCount + count);
                mCart.cartMap.put(itemId, object);
                if (currCount + count == 0) {
                    mCart.cartMap.remove(itemId);
                }
            } else {
                object.put("count", 1);
                mCart.cartMap.put(itemId, object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void makeNull() {
        mCart = null;
    }

    public HashMap<Integer, JSONObject> getCartMap() {
        return mCart.cartMap;
    }
}
