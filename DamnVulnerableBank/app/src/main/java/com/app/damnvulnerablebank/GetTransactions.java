package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

public class GetTransactions extends AppCompatActivity {

        RecyclerView recyclerView;
        List<TransactionRecords> trecords;

        Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        recyclerView=findViewById(R.id.transt);
        trecords=new ArrayList<>();

        extractRecords();
    }

    private void extractRecords() {
        SharedPreferences sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url  = sharedPreferences.getString("apiurl",null);
        String endpoint="/api/transactions/view";
        final String finalurl = url+endpoint;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.POST, finalurl, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                    try {
                        JSONArray jsonArray=response.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++) {


                            JSONObject transrecobject = jsonArray.getJSONObject(i);
                            TransactionRecords trecorder = new TransactionRecords();
                            trecorder.setFromaccnt(transrecobject.getString("from_account").toString());
                            trecorder.setToaccnt(transrecobject.getString("to_account").toString());
                            trecorder.setAmount(transrecobject.getString("amount").toString());
                            trecords.add(trecorder);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter=new Adapter(getApplicationContext(),trecords);
                recyclerView.setAdapter(adapter);

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
}