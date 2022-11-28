package com.abc.templatecustomerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abc.templatecustomerapp.Utils.NetworkResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    String username, psswd, name, email, address, contact;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.register_progresbar);
        Button registerButton = findViewById(R.id.register_btn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ((EditText) findViewById(R.id.username_et)).getText().toString();
                psswd = ((EditText) findViewById(R.id.psswd_et)).getText().toString();
                name = ((EditText) findViewById(R.id.name_et)).getText().toString();
                email = ((EditText) findViewById(R.id.email_et)).getText().toString();
                address = ((EditText) findViewById(R.id.address_et)).getText().toString();
                contact = ((EditText) findViewById(R.id.contact_et)).getText().toString();

                if (username.length() == 0 || psswd.length() == 0 || name.length() == 0 || email.length() == 0 || address.length() == 0 || contact.length() == 0 ) {
                    Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    new RegisterTask().execute();
                }
            }
        });
    }

    private class RegisterTask extends AsyncTask<Void, Void, NetworkResponse>{

        @Override
        protected NetworkResponse doInBackground(Void... voids) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", username);
                jsonObject.put("psswd", psswd);
                jsonObject.put("name", name);
                jsonObject.put("email", email);
                jsonObject.put("address", address);
                jsonObject.put("contact", contact);

                return Network.Register(jsonObject, Network.getAppId(RegisterActivity.this));
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NetworkResponse response) {
            progressBar.setVisibility(View.GONE);
            if (response != null) {
                if (response.getResponseCode() == 200) {
                    Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                } else if (response.getResponseCode() == 402) {
                    Toast.makeText(RegisterActivity.this, "This username is already taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
