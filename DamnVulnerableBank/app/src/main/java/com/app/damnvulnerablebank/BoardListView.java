package com.app.damnvulnerablebank;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardListView extends AppCompatActivity implements FileAdapter.OnItemClickListener {
    FileAdapter fadapter;
    RecyclerView recyclerView;
    String url;
    String retrivedToken;
    String subject;
    String content;
    RequestQueue requestQueue;
    String qnaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_list_view);
        Intent intent = getIntent();

        qnaID = intent.getStringExtra("qna_id");

        SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
        retrivedToken = sharedPreferences.getString("accesstoken",null);

        sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        url = sharedPreferences.getString("apiurl",null);
        String endpoint="/api/qna/view";
        String finalurl = url+endpoint;
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        final TextView subjectView=findViewById(R.id.qna_v_file_lst);
        final TextView contentView=findViewById(R.id.view_content);
        recyclerView = findViewById(R.id.qna_v_file_lst);

        JSONObject requestData = new JSONObject();
        JSONObject requestDataEncrypted = new JSONObject();
        try {
            requestData.put("qna_id", qnaID);

            requestDataEncrypted.put("enc_data", EncryptDecrypt.encrypt(requestData.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, finalurl, requestDataEncrypted,
                new Response.Listener<JSONObject>()  {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject decryptedResponse = new JSONObject(EncryptDecrypt.decrypt(response.get("enc_data").toString()));

                            JSONObject data = decryptedResponse.getJSONObject("data");

                            subject=data.getString("title");
                            content=data.getString("content");
                            JSONArray file=data.getJSONArray("file");

                            List<FileInfo> fileInfoes = new ArrayList<>();
                            for(int i=0; i<file.length(); i++){
                                FileInfo fileInfo = new FileInfo();
                                fileInfo.setFileName(file.getJSONObject(i).getString("file_name"));
                                fileInfo.setFileID(file.getJSONObject(i).getString("id"));
                                fileInfoes.add(fileInfo);
                            }

                            subjectView.setText(subject);
                            contentView.setText(content);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            fadapter = new FileAdapter(getApplicationContext(), fileInfoes);
                            fadapter.setOnItemClickListener(BoardListView.this);
                            recyclerView.setAdapter(fadapter);

                            Integer count = fadapter.getItemCount();
                            if(count == 0){
                                recyclerView.setVisibility(View.GONE);
                            }else{
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            recyclerView.setHasFixedSize(true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers=new HashMap();
                headers.put("Authorization","Bearer "+retrivedToken);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    public void edit(View view){
        Intent intent = new Intent(getApplicationContext(), BoardWrite.class);
        intent.putExtra("title", subject);
        intent.putExtra("content", content);
        intent.putExtra("qna_id", qnaID);
        ArrayList<FileInfo> file_list = new ArrayList<>();
        for(int i = 0; i<fadapter.getItemCount(); i++){
            file_list.add(fadapter.getItem(i));
        }
        intent.putExtra("file_id_list", file_list);
        intent.putExtra("rewrite", true);
        startActivity(intent);
        finish();
    }

    public void delete(View view){
        String endPoint = "/api/qna/delete";
        String finalurl = url+endPoint;

        JSONObject requestData = new JSONObject();
        JSONObject requestDataEncrypted = new JSONObject();
        try {
            requestData.put("qna_id", qnaID);
            Log.i("response", requestData.toString());

            requestDataEncrypted.put("enc_data", EncryptDecrypt.encrypt(requestData.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // create request
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, finalurl, requestDataEncrypted,
                new Response.Listener<JSONObject>()  {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "QnA deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers=new HashMap();
                headers.put("Authorization","Bearer "+retrivedToken);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onItemClick(int position) {
        FileInfo clickedItem = fadapter.getItem(position);
        downLoadFile(clickedItem.getFileName());
    }

    public void downLoadFile(String fileName){
        String endpoint="/api/qna/filedown";
        String finalurl = url+endpoint;

        DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

        finalurl+="?filename=upload/"+fileName;

        File file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), fileName);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(finalurl));

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(file));

        downloadManager.enqueue(request);
    }
}
