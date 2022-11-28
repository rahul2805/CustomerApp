package com.abc.templatecustomerapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.util.Pair;

import com.abc.templatecustomerapp.Model.MainTask;
import com.abc.templatecustomerapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrdersAdapter extends BaseExpandableListAdapter {

    private List<Pair<String, ArrayList<JSONObject>>> mGroups = new ArrayList<>();
    private Context mContext;

    public OrdersAdapter(Context context, ArrayList<JSONObject> pendingOrders, ArrayList<JSONObject> completedOrders) {
        mContext = context;
        mGroups.add(new Pair<String, ArrayList<JSONObject>>("Pending Orders", pendingOrders));
        mGroups.add(new Pair<String, ArrayList<JSONObject>>("Completed Orders", completedOrders));
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).second.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).second.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.order_list_header_layout, parent, false);
        }

        Pair<String, ArrayList<JSONObject>> group = (Pair<String, ArrayList<JSONObject>>) getGroup(groupPosition);
        ((TextView) view.findViewById(R.id.order_view_heading)).setText(group.first);
        ((TextView) view.findViewById(R.id.order_view_count)).setText(String.valueOf(group.second.size()));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.order_list_layout, parent, false);
        }
        try {
            final JSONObject object = (JSONObject) getChild(groupPosition, childPosition);

            if (object.getInt("status") == 0) {
                view.findViewById(R.id.order_delivered_on_title).setVisibility(View.GONE);
                view.findViewById(R.id.order_delivered_on_tv).setVisibility(View.GONE);

            } else {
                view.findViewById(R.id.order_delivered_on_title).setVisibility(View.VISIBLE);
                view.findViewById(R.id.order_delivered_on_tv).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.order_delivered_on_tv)).setText(object.getString("delivered_on"));
            }
            ((TextView) view.findViewById(R.id.order_id_tv)).setText(String.valueOf(object.getInt("id")));
            ((TextView) view.findViewById(R.id.order_amnt_tv)).setText(object.getString("total_amnt"));
            ((TextView) view.findViewById(R.id.order_date_tv)).setText(object.getString("date"));
            ((TextView) view.findViewById(R.id.order_address_tv)).setText(object.getString("address"));
            ((TextView) view.findViewById(R.id.order_contact_tv)).setText(object.getString("contact"));
            JSONArray items = new JSONArray(object.getString("items"));
            String itemsString = "";
            for (int i = 0; i < items.length(); i++) {
                JSONObject itemObject = items.getJSONObject(i);
                itemsString += itemObject.getString("qty") + " x " + itemObject.getString("name") + ", ";
            }
            ((TextView) view.findViewById(R.id.order_items_tv)).setText(itemsString);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

