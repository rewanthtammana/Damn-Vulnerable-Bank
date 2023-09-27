package com.app.damnvulnerablebank;
/*
Android 앱에서 수신 계좌를 승인하는 "ApproveBeneficiary" Activity.
사용자가 입력한 정보를 서버로 전송하고 응답을 처리하는데 Volley 라이브러리 사용
 */
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

public class ApproveBeneficiary extends AppCompatActivity {
    TextView hey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // activity_approvebenificiary.xml 화면에 표시
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvebenificiary);
        hey=findViewById(R.id.accountid);
        // 이전 액티비티에서 전달된 데이터를 인텐트(Intent)를 통해 받아서 'hey' TextView에 표시
        Intent i =getIntent();
        String we=i.getStringExtra("id");
        hey.setText(we);
    }

 public void approveBeneficiary(View view){
        // "Approve" 버튼을 클릭하면 호출되는 메서드
     SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
     // SharedPreferences를 사용하여 저장된 접근 토큰(access token)을 가져온다.
     final String retrivedToken  = sharedPreferences.getString("accesstoken",null);
     // EditText에서 입력한 계좌 ID를 가져와 정수로 변환
     EditText ed=findViewById(R.id.accountid1);
     int n = Integer.parseInt(ed.getText().toString());

     final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
     JSONObject requestData = new JSONObject();
     JSONObject requestDataEncrypted = new JSONObject();
     try {
         //input your API parameters
         requestData.put("id",n);

         // Encrypt data before sending
         // 데이터를 암호화
         requestDataEncrypted.put("enc_data", EncryptDecrypt.encrypt(requestData.toString()));

     } catch (JSONException e) {
         e.printStackTrace();
     }
     // Enter the correct url for your api service site
     // 서버에 보낼 요청 URL을 조립
     sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
     final String url  = sharedPreferences.getString("apiurl",null);
     String endpoint="/api/beneficiary/approve";
     String finalurl = url+endpoint;
     // Volley 라이브러리를 사용하여 HTTP POST 요청을 생성
     final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, finalurl, requestDataEncrypted,
             new Response.Listener<JSONObject>() {
                 @Override
                 public void onResponse(JSONObject response) {


                     try {
                         // 서버로부터의 응답을 처리하고, 성공적인 응답일 경우 토스트 메시지를 표시
                         Toast.makeText(getApplicationContext(),""+EncryptDecrypt.decrypt(response.get("enc_data").toString()), Toast.LENGTH_SHORT).show();
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }

                     // 대시보드 화면으로 이동
                     startActivity(new Intent(ApproveBeneficiary.this, Dashboard.class));

                 }
             }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
             // 오류 응답일 경우 "Something went wrong" 메시지를 표시
             Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
         }
     }){
         @Override
         public Map getHeaders() throws AuthFailureError {
             // Volley 요청 헤더에 접근 토큰을 추가하는 메서드
             HashMap headers=new HashMap();
             headers.put("Authorization","Bearer "+retrivedToken);
             return headers;
         }


     };
     requestQueue.add(jsonObjectRequest);
 }

}