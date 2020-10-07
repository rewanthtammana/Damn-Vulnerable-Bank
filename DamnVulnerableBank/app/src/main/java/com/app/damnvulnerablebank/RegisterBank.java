package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterBank extends AppCompatActivity {
    FirebaseAuth auth;

    public void backToMain(View view){
        Intent intent =new Intent(RegisterBank.this, MainActivity.class);
        startActivity(intent);
    }
    public void register(View view)
    {
        EditText inputEmail=findViewById(R.id.signupemail_editText);
        EditText inputPassword=findViewById(R.id.signup_password_editText);

        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url = sharedPreferences.getString("apiurl",null);
        String endpoint = "/api/user/register";
        String finalUrl = url + endpoint;

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("username", email);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, finalUrl, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "User created" + response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterBank.this, BankLogin.class));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "You did something wrong", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);




    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

}