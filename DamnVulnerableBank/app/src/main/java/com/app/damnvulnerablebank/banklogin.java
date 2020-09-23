package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class banklogin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_banklogin);
        SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("isloggedin", true))
        {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }
    }

    public void addapi(View view){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("apiurl", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        EditText ed=findViewById(R.id.apiurl);
        final String api =ed.getText().toString().trim();
        editor.putString("apiurl", api);
        editor.apply();
    }

    public void loginpage(View view){
        Intent intent =new Intent(getApplicationContext(),login.class);
        startActivity(intent);
    }
    public void signupage(View view){
        Intent intent =new Intent(getApplicationContext(),signup.class);
        startActivity(intent);
    }
    public void healthcheck(View v){
        final View vButton = findViewById(R.id.healthc);
        final Button bButton = (Button) findViewById(R.id.healthc);
        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url  = sharedPreferences.getString("apiurl",null);
        String endpoint="/api/health/check";
        String finalurl = url+endpoint;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        bButton.setText("Api is Up");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bButton.setText("Api is Down");
            }
        });
        queue.add(stringRequest);
        queue.getCache().clear();
    }
}