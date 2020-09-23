package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pendingbenificiary extends AppCompatActivity implements Padapter.OnItemClickListener{
    public static final String id="id";
    RecyclerView recyclerView;
    List<Precords> precords;

    Padapter padapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendingbenificiary);
        recyclerView=findViewById(R.id.pendb);
        precords=new ArrayList<>();

        pendingbenif();
    }

    public void pendingbenif(){
        SharedPreferences sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url  = sharedPreferences.getString("apiurl",null);
        String endpoint="/api/beneficiary/pending";
        String finalurl = url+endpoint;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.POST, finalurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray=response.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++) {


                                JSONObject transrecobject = jsonArray.getJSONObject(i);
                                Precords precorder = new Precords();
                                precorder.setAccount_number(transrecobject.getString("account_number").toString());
                                precorder.setBeneficiary_account_number(transrecobject.getString("beneficiary_account_number").toString());
                                precorder.setIdd(transrecobject.getString("id").toString());
                                precords.add(precorder);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        padapter=new Padapter(getApplicationContext(),precords);
                        recyclerView.setAdapter(padapter);
                        padapter.setOnItemClickListener(pendingbenificiary.this);

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

        queue.add(jsonArrayRequest);
        queue.getCache().clear();
    }

    @Override
    public void onItemClick(int position) {
        Intent de=new Intent(this,approvebenificiary.class);
        Precords cf =precords.get(position);
        de.putExtra(id,cf.getIdd());
        startActivity(de);
    }
}