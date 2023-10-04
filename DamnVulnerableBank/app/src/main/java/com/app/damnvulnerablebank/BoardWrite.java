package com.app.damnvulnerablebank;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardWrite extends AppCompatActivity implements FileAdapter.OnItemClickListener {
    RecyclerView recyclerView;
    FileAdapter fadapter;
    String url;
    String retrivedToken;
    String subject;
    String contents;
    RequestQueue requestQueue;
    String qnaID;
    TextView title;
    TextView content;
    Button writeBtn;
    ArrayList<FileInfo> fileInfoArray;
    boolean rewrite;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_write);

        title = findViewById(R.id.w_write_title);
        content = findViewById(R.id.w_write_content);

        intent = getIntent();

        subject = intent.getStringExtra("title");
        contents = intent.getStringExtra("content");
        qnaID = intent.getStringExtra("qna_id");
        rewrite = intent.getBooleanExtra("rewrite",false);
        fileInfoArray = (ArrayList<FileInfo>) intent.getSerializableExtra("file_id_list");
        title.setText(subject);
        content.setText(contents);

        writeBtn = findViewById(R.id.qna_w_write_btn);
        if(rewrite){
            writeBtn.setText("수정");
        }

        recyclerView = findViewById(R.id.qna_w_file_lst);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(fileInfoArray==null){
            fileInfoArray = new ArrayList<FileInfo>();
        }
        fadapter = new FileAdapter(getApplicationContext(), fileInfoArray);
        fadapter.setOnItemClickListener(BoardWrite.this);
        recyclerView.setAdapter(fadapter);


        SharedPreferences sharedPreferences = getSharedPreferences("jwt", MODE_PRIVATE);
        retrivedToken = sharedPreferences.getString("accesstoken", null);

        sharedPreferences = getSharedPreferences("apiurl", MODE_PRIVATE);
        url = sharedPreferences.getString("apiurl", null);

        requestQueue = Volley.newRequestQueue(this);
    }

    public void writePost(View view) {
        String endpoint;
        //String postTitle = title.getText().toString();
        //String postContent = content.getText().toString();
        JSONObject requestData;
        JSONObject requestDataEncrypted;

        if (rewrite) {
            endpoint = url + "/api/qna/rewrite";
            requestData = new JSONObject();
            requestDataEncrypted = new JSONObject();
            try {
                requestData.put("title", title.getText().toString());
                requestData.put("content", content.getText().toString());
                requestData.put("qna_id", qnaID);
                JSONArray file_ids = new JSONArray();
                for(int i = 0; i<fadapter.getItemCount(); i++){
                    file_ids.put(fadapter.getItem(i).getFileID());
                }
                requestData.put("file_id_list", file_ids);
                requestDataEncrypted.put("enc_data", EncryptDecrypt.encrypt(requestData.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            endpoint = url + "/api/qna/write";
            requestData = new JSONObject();
            requestDataEncrypted = new JSONObject();
            try {
                requestData.put("title", title.getText().toString());
                requestData.put("content", content.getText().toString());
                JSONArray file_ids = new JSONArray();
                for(int i = 0; i<fadapter.getItemCount(); i++){
                    file_ids.put(fadapter.getItem(i).getFileID());
                }
                requestData.put("file_id_list", file_ids);
                requestDataEncrypted.put("enc_data", EncryptDecrypt.encrypt(requestData.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, endpoint, requestDataEncrypted, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject decryptedResponse = new JSONObject(EncryptDecrypt.decrypt(response.get("enc_data").toString()));

                    if(rewrite){
                        finish();
                        writeComplete();
                        Toast.makeText(getApplicationContext(), "QnA 게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        qnaID = decryptedResponse.getString("data");
                        finish();
                        writeComplete();
                        Toast.makeText(getApplicationContext(), "QnA 게시글이 작성되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "QnA 게시글 작성 리턴 값 미 일치", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "QnA 게시글 작성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","Bearer "+retrivedToken);
                return params;
            }
        };

        requestQueue.add(jsonObjectRequest);
        requestQueue.getCache().clear();
    }
    @Override
    public void onItemClick(final int position) {
        String endpoint;
        endpoint = url + "/api/qna/filedel";
        FileInfo fi = fileInfoArray.get(position);

        JSONObject requestData;
        JSONObject requestDataEncrypted;

        requestData = new JSONObject();
        requestDataEncrypted = new JSONObject();

        try {
            requestData.put("file_id", fi.getFileID());
            requestDataEncrypted.put("enc_data", EncryptDecrypt.encrypt(requestData.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, requestDataEncrypted, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                fileInfoArray.remove(position);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fadapter.notifyDataSetChanged();
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","Bearer "+retrivedToken);
                return params;
            }
        };

        requestQueue.add(request);
    }

    void writeComplete(){
        Intent de = new Intent(this, BoardListView.class);
        de.putExtra("qna_id", qnaID);
        startActivity(de);
    }

    public void selectFile(View view){

        // 파일 선택
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "파일을 선택하세요."),
                    0);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "파일 관련 앱이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadFile(Uri fileUri, byte[] fileData){
        // 파일 업로드
        String endpoint = "/api/qna/fileup";
        String finalurl = url + endpoint;


        Log.e("enc_data", fileUri.getPath());

//        String[] flist = fileUri.getEncodedPath().split("%2F");
//        final String fileName = flist[flist.length-1];

        String scheme = fileUri.getScheme();
        String fileName = null;
        switch (scheme) {
            case "content":
                String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
                Cursor cursor = getContentResolver()
                        .query(fileUri, projection, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        fileName = cursor.getString(
                                cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
                    }
                    cursor.close();
                }
                break;

            case "file":
                fileName = new File(fileUri.getPath()).getName();
                break;

            default:
                break;
        }

        Log.d("scheme", scheme);

        if(Build.VERSION.SDK_INT >= 29){

        }


        final String filename2 = fileName;

        String d = "dfjaisladf";
        byte[] file = d.getBytes();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("path", "upload")
                .addFormDataPart("file", fileName, RequestBody.create(MultipartBody.FORM, fileData))
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(finalurl)
                .addHeader("Authorization", "Bearer "+retrivedToken)
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if(response.isSuccessful()){

                    try {
                        JSONObject myResponse = new JSONObject(response.body().string());
                        JSONObject decryptedResponse = new JSONObject(EncryptDecrypt.decrypt(myResponse.get("enc_data").toString()));
                        JSONObject fileResponse = new JSONObject(decryptedResponse.getString("data"));
                        fileInfoArray.add(new FileInfo(filename2, fileResponse.getString("id")));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fadapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode==RESULT_OK){
            Toast.makeText(BoardWrite.this, "파일 선택 성공", Toast.LENGTH_SHORT).show();

            Uri uri = data.getData();

            InputStream inputStream = null;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int DEFAULT_BUFFER_SIZE = 1024*4;
            try{
                inputStream = getContentResolver().openInputStream(uri);

                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                int len;
                while((len = inputStream.read(buffer))>0){
                    outputStream.write(buffer, 0, len);
                }

                Log.e("enc_data", uri.getLastPathSegment());
                uploadFile(uri, outputStream.toByteArray());
                inputStream.close();
                outputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            } finally{
                if(inputStream!= null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            Toast.makeText(BoardWrite.this, "파일 선택 실패", Toast.LENGTH_SHORT).show();
        }
    }

}
