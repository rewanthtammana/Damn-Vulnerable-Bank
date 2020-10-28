package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
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

public class Myprofile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        final TextView tv1=findViewById(R.id.textView1);
//        tv1.setTypeface(null, Typeface.BOLD_ITALIC);
        final TextView tv2=findViewById(R.id.textView2);
        final TextView tv3=findViewById(R.id.textView3);
        final TextView tv4=findViewById(R.id.textView4);
        SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
        final String retrivedToken  = sharedPreferences.getString("accesstoken",null);
        final RequestQueue queue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url  = sharedPreferences.getString("apiurl",null);
        String endpoint="/api/user/profile";
        String finalurl = url+endpoint;



        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, finalurl,null,
                new Response.Listener<JSONObject>()  {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject decryptedResponse = new JSONObject(EncryptDecrypt.decrypt(response.get("enc_data").toString()));

                            // Check for error message
                            if(decryptedResponse.getJSONObject("status").getInt("code") != 200) {
                                Toast.makeText(getApplicationContext(), "Error: " + decryptedResponse.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                                return;
                                // This is buggy. Need to call Login activity again if incorrect credentials are given
                            }

                            JSONObject obj = decryptedResponse.getJSONObject("data");
                            String balance=obj.getString("balance");
                            String account_number =obj.getString("account_number");
                            String username =obj.getString("username");
                            String is_admin =obj.getString("is_admin");
                            tv1.setText("Name:\t\t" + username);
                            tv2.setText("Account Number:\t\t" + account_number);
                            tv3.setText("Balance:\t\t$" +balance);
                            if(is_admin == "true") {
                                tv4.setText("Admin:\t\tYes");
                            } else {
                                tv4.setText("Admin:\t\tNo");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers=new HashMap();
                headers.put("Authorization","Bearer "+retrivedToken);
                return headers;
            }


        };





        queue.add(stringRequest);
        queue.getCache().clear();
    }
}