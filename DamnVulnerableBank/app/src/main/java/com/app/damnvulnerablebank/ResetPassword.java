package com.app.damnvulnerablebank;
// 비밀번호 재설정

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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passreset);
    }

    public void backToMain(View view) {
        // "Back" 버튼을 클릭하면 호출되는 메서드
        // MainActivity로 이동
        Intent into = new Intent(ResetPassword.this, Dashboard.class);
        startActivity(into);
    }

    public void resetPassword(View view) {
        EditText oldPass = findViewById(R.id.oldlogin_password_editText);
        EditText newPass = findViewById(R.id.newlogin_password_editText);
        EditText newPassConfirm = findViewById(R.id.new1login_password_editText);
        final String oldPassword = oldPass.getText().toString().trim();
        final String newPassword = newPass.getText().toString().trim();
        final String newpasswordConfirm = newPassConfirm.getText().toString().trim();

        if (!newPassword.equals(newpasswordConfirm)) {
            Toast.makeText(getApplicationContext(), "Something Entered Password is Different", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
            final String url = sharedPreferences.getString("apiurl", null);
            String endpoint = "/api/user/change-password";
            String finalurl = url + endpoint;

            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject requestData = new JSONObject();
            JSONObject requestDataEncrypted = new JSONObject();
            try {
                //input your API parameters
                String hOldPassword = hashPassword(oldPassword);
                String hNewPassword = hashPassword(newPassword);
                requestData.put("password", hOldPassword);
                requestData.put("new_password", hNewPassword);

                // Encrypt data before sending
                requestDataEncrypted.put("enc_data", EncryptDecrypt.encrypt(requestData.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Enter the correct url for your api service site
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, finalurl, requestDataEncrypted,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putBoolean("isloggedin", false).apply();
                            startActivity(new Intent(ResetPassword.this, BankLogin.class));


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
                    final String retrivedToken = sharedPreferences.getString("accesstoken", null);
                    HashMap headers = new HashMap();
                    headers.put("Authorization", "Bearer " + retrivedToken);
                    return headers;
                }
            };

            requestQueue.add(jsonObjectRequest);

        }
    }

    protected static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}

