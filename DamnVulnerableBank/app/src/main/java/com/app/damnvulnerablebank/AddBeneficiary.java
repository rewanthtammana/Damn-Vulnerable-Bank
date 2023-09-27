/*
안드로이드 앱에서 서버로 데이터를 보내고 응답을 처리하는데 사용되며, SharedPreferences를 통해 중요한 데이터를 저장 및
검색한다. volley는 네트워크 요청을 처리하고 서버와의 통신을 담당한다.
즉, 접근 토큰을 사용하여 사용자 인증 및 데이터 교환을 보여주는 코드
 */
package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

/*
수신 계좌를 추가하는 Activite
 */
public class AddBeneficiary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Activity 생성 때 호출되는 메소드.
        super.onCreate(savedInstanceState);
        // activity_addbenif.xml을 화면에 표시함.
        setContentView(R.layout.activity_addbenif);


    }

    public void addBeneficiary(View view){
        // add beneficiary 버튼 클릭 시 호출되는 메소드.(activity_addbenif.xml)
        SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
        // 저장된 접근 토큰 및 API URL을 가져옴.
        final String retrivedToken  = sharedPreferences.getString("accesstoken",null);
        // editText에서 입력한 계좌번호를 가져옴.
        EditText ed=findViewById(R.id.edt);
        final String edd=ed.getText().toString().trim();
        // 서버에 보낼 요청 url 조립
        sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url  = sharedPreferences.getString("apiurl",null);
        String endpoint="/api/beneficiary/add";
        String finalurl = url+endpoint;
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        // JSON 형식의 데이터 생성
        JSONObject requestData = new JSONObject();
        JSONObject requestDataEncrypted = new JSONObject();
        try {
            //input your API parameters
            requestData.put("account_number",edd);

            // Encrypt the data before sending
            // 데이터 암호화
            requestDataEncrypted.put("enc_data", EncryptDecrypt.encrypt(requestData.toString()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        // HTTP POST 요청 생성
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, finalurl, requestDataEncrypted,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            // 성공 응답일 경우 토스트 메세지 표시
                            Toast.makeText(getApplicationContext(), EncryptDecrypt.decrypt(response.get("enc_data").toString()), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // 성공하면 대시보드 화면으로 이동
                        startActivity(new Intent(AddBeneficiary.this, Dashboard.class));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 오류 응답 -> something went wrong 메세지 표시
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                // volley 요청 헤더에 접근 토큰을 추가하는 메소드
                // volley: 안드로이드 앱 개발을 위한 네트워킹 라이브러리
                HashMap headers=new HashMap();
                headers.put("Authorization","Bearer "+retrivedToken);
                return headers;
            }


        };
        requestQueue.add(jsonObjectRequest);


    }
}