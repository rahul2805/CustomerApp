package com.abc.templatecustomerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.abc.templatecustomerapp.Model.User;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Profile");
        }

        User user = User.getUserInstance();
        ((TextView)findViewById(R.id.name_tv)).setText(user.getName());
        ((TextView)findViewById(R.id.username_tv)).setText(user.getUsername());
        ((TextView)findViewById(R.id.email_tv)).setText(user.getEmail());
        ((TextView)findViewById(R.id.address_tv)).setText(user.getAddress());
        ((TextView)findViewById(R.id.contact_tv)).setText(user.getContact());

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
