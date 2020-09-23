package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import static com.app.damnvulnerablebank.viewbenif.beneficiary_account_number;

public class sendmoney extends AppCompatActivity {

    TextView tt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmoney);
        tt=findViewById(R.id.actid);
        Intent i =getIntent();
        String p=i.getStringExtra(beneficiary_account_number);
        tt.setText(p);
    }



    public void sendmon(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
        final String retrivedToken  = sharedPreferences.getString("accesstoken",null);
        EditText ed=findViewById(R.id.edact);
        EditText ed1=findViewById(R.id.edamt);
        int n = Integer.parseInt(ed.getText().toString());
        int na = Integer.parseInt(ed1.getText().toString());
        String url ="http://192.168.43.131:8000/api/balance/transfer";
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("to_account",n);
            object.put("amount",na);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                            Toast.makeText(getApplicationContext(),""+response, Toast.LENGTH_SHORT).show();


                        startActivity(new Intent(sendmoney.this, Dashboard.class));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers=new HashMap();
                headers.put("Authorization","Bearer "+retrivedToken);
                return headers;
            }


        };
        requestQueue.add(jsonObjectRequest);


    }
}