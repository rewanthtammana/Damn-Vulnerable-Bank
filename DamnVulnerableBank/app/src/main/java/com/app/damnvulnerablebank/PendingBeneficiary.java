package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
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

public class PendingBeneficiary extends AppCompatActivity implements Padapter.OnItemClickListener{
    public static final String id="id";
    RecyclerView recyclerView;
    List<PendingBeneficiaryRecords> precords;
    private TextView emptyView;
    Padapter padapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendingbenificiary);
        recyclerView=findViewById(R.id.pendb);
        precords=new ArrayList<>();
        emptyView = findViewById(R.id.empty_view);
        getPendingBeneficiaries();
    }

    public void getPendingBeneficiaries(){
        SharedPreferences sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url  = sharedPreferences.getString("apiurl",null);
        String endpoint = "/api/beneficiary/pending";
        String finalUrl = url + endpoint;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.POST, finalUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject decryptedResponse = new JSONObject(EncryptDecrypt.decrypt(response.get("enc_data").toString()));

                            Log.d("Pending Beneficiary", decryptedResponse.toString());
//                            Log.d("Pending Beneficiary", decryptedResponse.getJSONObject("status").getInt("code"));
                            // Check for error message
                            if(decryptedResponse.getJSONObject("status").getInt("code") != 200) {
                                Toast.makeText(getApplicationContext(), "Error: " + decryptedResponse.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                                return;
                                // This is buggy. Need to call Login activity again if incorrect credentials are given
                            }

                            JSONArray jsonArray = decryptedResponse .getJSONArray("data");

                            for(int i=0; i < jsonArray.length(); i++) {

                                JSONObject transrecobject = jsonArray.getJSONObject(i);
                                PendingBeneficiaryRecords precorder = new PendingBeneficiaryRecords();
                                precorder.setAccountNumber(transrecobject.getString("account_number").toString());
                                precorder.setBeneficiaryAccountNumber(transrecobject.getString("beneficiary_account_number").toString());
                                precorder.setId(transrecobject.getString("id").toString());
                                precords.add(precorder);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        padapter = new Padapter(getApplicationContext(),precords);
                        recyclerView.setAdapter(padapter);
                        padapter.setOnItemClickListener(PendingBeneficiary.this);
                        Integer count=padapter.getItemCount();
                        if (count == 0) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                        else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }

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
                final String retrivedToken = sharedPreferences.getString("accesstoken",null);
                HashMap headers = new HashMap();
                headers.put("Authorization","Bearer " + retrivedToken);
                return headers;
            }};

        queue.add(jsonArrayRequest);
        queue.getCache().clear();
    }

    @Override
    public void onItemClick(int position) {
        Intent de=new Intent(this, ApproveBeneficiary.class);
        PendingBeneficiaryRecords cf =precords.get(position);
        de.putExtra(id,cf.getId());
        startActivity(de);
    }
}