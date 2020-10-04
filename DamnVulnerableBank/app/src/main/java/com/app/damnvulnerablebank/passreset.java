package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class passreset extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passreset);
    }

    public void reset(View view) {
        EditText oldpass = findViewById(R.id.oldlogin_password_editText);
        EditText newpass = findViewById(R.id.newlogin_password_editText);
        EditText newpass1 = findViewById(R.id.new1login_password_editText);
        final String oldpassword = oldpass.getText().toString().trim();
        final String newpassword = newpass.getText().toString().trim();
        final String new1password = newpass1.getText().toString().trim();

        if (!newpassword.equals(new1password)) {
            Toast.makeText(getApplicationContext(), "Something Entered Password is Different", Toast.LENGTH_SHORT).show();
        }

        else{
        SharedPreferences sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url = sharedPreferences.getString("apiurl", null);
        String endpoint = "/api/user/change-password";
        String finalurl = url + endpoint;


        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("password", oldpassword);
            object.put("new_password", newpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, finalurl, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplicationContext(),"Done", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putBoolean("isloggedin", false).apply();
                        startActivity(new Intent(passreset.this, login.class));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
                final String retrivedToken  = sharedPreferences.getString("accesstoken",null);
                HashMap headers=new HashMap();
                headers.put("Authorization","Bearer "+retrivedToken);
                return headers;
            }};

        requestQueue.add(jsonObjectRequest);

    }
    }
}