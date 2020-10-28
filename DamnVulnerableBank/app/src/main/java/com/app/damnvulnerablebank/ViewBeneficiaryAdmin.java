package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class ViewBeneficiaryAdmin extends AppCompatActivity  implements Badapter.OnItemClickListener {
public static final String beneficiary_account_number="beneficiary_account_number";
    RecyclerView recyclerView;
    List<BeneficiaryRecords> brecords;
    private TextView emptyView;
    Badapter badapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewbenif);
        recyclerView=findViewById(R.id.benif);
        emptyView=findViewById(R.id.empty_view);
        brecords=new ArrayList<>();
        viewBeneficiaries();
    }
    public void viewBeneficiaries(){
        SharedPreferences sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url = sharedPreferences.getString("apiurl",null);
        String endpoint = "/api/beneficiary/view";
        final String finalurl = url+endpoint;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.POST, finalurl, null,
                new Response.Listener<JSONObject>() {
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

                            JSONArray jsonArray=decryptedResponse.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject transrecobject = jsonArray.getJSONObject(i);
                                BeneficiaryRecords brecorder = new BeneficiaryRecords();
                                String approved=transrecobject.getString("approved").toString();
                                if(approved=="false")
                                {continue;}
                                else{
                                brecorder.setBeneficiaryAccount(transrecobject.getString("beneficiary_account_number").toString());
                                //brecorder.setIsapproved(transrecobject.getString("approved").toString());
                                brecords.add(brecorder);}
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        badapter=new Badapter(getApplicationContext(),brecords);
                        recyclerView.setAdapter(badapter);

                        Integer count=badapter.getItemCount();
                        if (count == 0) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                        else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                        badapter.setOnItemClickListener(ViewBeneficiaryAdmin.this);
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
        Intent de=new Intent(this, SendMoney.class);
        BeneficiaryRecords cf =brecords.get(position);

        de.putExtra(beneficiary_account_number,cf.getBeneficiaryAccount());
        startActivity(de);
    }
}