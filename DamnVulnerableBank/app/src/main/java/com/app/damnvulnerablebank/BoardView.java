package com.app.damnvulnerablebank;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class BoardView extends AppCompatActivity implements Qadapter.OnItemClickListener {
    RecyclerView recyclerView;
    List<BoardListRecords> boardRecords;
    Qadapter qadapter;
    private TextView emptyView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_view);
        recyclerView = findViewById(R.id.qna_list);
        boardRecords = new ArrayList<>();
        emptyView = findViewById(R.id.empty_view);
        viewQnAList();
    }

    public void viewQnAList() {
        SharedPreferences sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url = sharedPreferences.getString("apiurl", null);
        String endpoint = "/api/qna/list";
        final String finalurl = url + endpoint;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, finalurl, null,
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

                            JSONArray jsonArray = decryptedResponse.getJSONArray("data");

                            for(int i = 0; i<jsonArray.length(); i++){
                                JSONObject qnalistobject = jsonArray.getJSONObject(i);
                                BoardListRecords boardRecorder = new BoardListRecords();
                                String subject = qnalistobject.getString("title");
                                String writer = qnalistobject.getJSONObject("user").getString("username");
                                String date = qnalistobject.getString("write_at");
                                String id = qnalistobject.getString("id");

                                boardRecorder.setSubject(subject);
                                boardRecorder.setWriter(writer);
                                boardRecorder.setDate(date);
                                boardRecorder.setId(id);

                                boardRecords.add(boardRecorder);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        qadapter = new Qadapter(getApplicationContext(), boardRecords);
                        recyclerView.setAdapter(qadapter);

                        Integer count = qadapter.getItemCount();
                        if(count==0){
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                        else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                        qadapter.setOnItemClickListener(BoardView.this);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
                final String retrivedToken  = sharedPreferences.getString("accesstoken",null);
                HashMap headers=new HashMap();
                headers.put("Authorization","Bearer "+retrivedToken);
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
        queue.getCache().clear();
    }

    public void write(View view){
        Intent de = new Intent(this, BoardWrite.class);
        startActivityForResult(de,1);
    }

    @Override
    public void onItemClick(int position) {
        Intent de = new Intent(this, BoardView.class);
        BoardListRecords cf = boardRecords.get(position);

        de.putExtra("qna_id", cf.getId());
        startActivityForResult(de, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
        overridePendingTransition(0, 0);
        Intent intent = getIntent();
        startActivity(intent);
    }
}

