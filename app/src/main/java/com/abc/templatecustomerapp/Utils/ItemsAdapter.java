package com.abc.templatecustomerapp.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abc.templatecustomerapp.MainActivity;
import com.abc.templatecustomerapp.Model.Cart;
import com.abc.templatecustomerapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

public class ItemsAdapter extends ArrayAdapter<JSONObject> {
    private Context mContext;
    public ItemsAdapter(@NonNull Context context, int resource, @NonNull List<JSONObject> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_list_layout, parent, false);
        }

        final JSONObject object = getItem(position);

        try {

            Button itemAddButton = view.findViewById(R.id.item_add_button);
            Button itemRemoveButton = view.findViewById(R.id.item_remove_button);
            final TextView itemCountTv = view.findViewById(R.id.item_count_tv);

            if (Cart.getCartInstance().getCartMap().containsKey(object.getInt("id"))) {
                itemCountTv.setText(String.valueOf(Cart.getCartInstance().getCartMap().get(object.getInt("id")).getInt("count")));
            } else {
                itemCountTv.setText("0");
            }

            itemAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currCount = Integer.valueOf(itemCountTv.getText().toString());
                    currCount += 1;
                    itemCountTv.setText(String.valueOf(currCount));
                    Cart cart = Cart.getCartInstance();
                    cart.addItemToCart(object, 1);
                }
            });

            itemRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currCount = Integer.valueOf(itemCountTv.getText().toString());
                    if (currCount == 0) {
                        Toast.makeText(mContext, "Item is not in cart", Toast.LENGTH_SHORT).show();
                    } else {
                        currCount -= 1;
                        itemCountTv.setText(String.valueOf(currCount));
                        Cart cart = Cart.getCartInstance();
                        cart.addItemToCart(object, -1);
                    }
                }
            });

            ((TextView) view.findViewById(R.id.item_name)).setText(object.getString("itemName"));
            ((TextView) view.findViewById(R.id.item_description)).setText(object.getString("description"));
            ((TextView) view.findViewById(R.id.item_price)).setText("Price: " + object.getString("price"));

            ImageView imageView = view.findViewById(R.id.item_image);
            Picasso.get().load(object.getString("imageURL")).into(imageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
