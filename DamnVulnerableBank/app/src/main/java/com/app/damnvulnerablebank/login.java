package com.app.damnvulnerablebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    FirebaseAuth mauth;
    private ProgressBar spinner;
    private RelativeLayout priv;
    public void backtomain(View view){
        Intent into =new Intent(login.this,banklogin.class);
        startActivity(into);
    }


    public void firebaselogin(View view)
    {
        final TextView t1=findViewById(R.id.log);
        EditText inputEmail=findViewById(R.id.loginemail_editText);
        EditText inputPaassword=findViewById(R.id.login_password_editText);
        priv=(RelativeLayout)findViewById(R.id.relp);
        spinner = (ProgressBar)findViewById(R.id.progressb);
        priv.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        final String email =inputEmail.getText().toString().trim();
        final String password =inputPaassword.getText().toString().trim();


        SharedPreferences sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url  = sharedPreferences.getString("apiurl",null);
        String endpoint="/api/user/login";
        String finalurl = url+endpoint;

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("username",email);
            object.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, finalurl, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            JSONObject obj = response.getJSONObject("data");
                           String accessToken=obj.getString("accessToken");
                            SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("accesstoken",accessToken).apply();
                            sharedPreferences.edit().putBoolean("isloggedin",true).apply();
                            startActivity(new Intent(login.this, Dashboard.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }
}