package com.app.damnvulnerablebank;
// 송금 Activity

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static com.app.damnvulnerablebank.PendingBeneficiary.beneficiary_account_number;

public class SendMoney extends AppCompatActivity {

    Button send;
    TextView tt;
    Date currentDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmoney);
        tt=findViewById(R.id.actid);
        Intent i =getIntent();
        String p=i.getStringExtra(beneficiary_account_number);
        tt.setText(p);
        send=findViewById(R.id.sendbutton);
        Log.d("sendmoney", "Hello Sendmoney.class");
    }



    public void sendMoney(){
        SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
        final String retrivedToken  = sharedPreferences.getString("accesstoken",null);
        SharedPreferences sharedPreferences1 = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url  = sharedPreferences1.getString("apiurl",null);
        String endpoint = "/api/balance/transfer";
        final String finalUrl = url+endpoint;
        EditText ed = findViewById(R.id.edact);     // 수취계좌
        EditText ed1 = findViewById(R.id.edamt);    // 이체금액
        int to_account = 0;
        int amount = 0;
        String sendtime = dateFormat.format(currentDate);

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject requestData = new JSONObject();
        JSONObject requestDataEncrypted = new JSONObject();
        try {

            // fetch values
            if (ed.getText().toString() != "" && ed1.getText().toString() != "") {
                to_account = Integer.parseInt(ed.getText().toString());
                amount = Integer.parseInt(ed1.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Input ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SendMoney.this, SendMoney.class));
            }
            //input your API parameters
            requestData.put("to_account", to_account);          // 수취계좌 varchar
            requestData.put("amount", amount);                  // 이체금액 int
            requestData.put("sendtime", sendtime);              // 전송시간 datetime


            // Encrypt data before sending
            requestDataEncrypted.put("enc_data", EncryptDecrypt.encrypt(requestData.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, finalUrl, requestDataEncrypted,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject decryptedResponse =  new JSONObject(EncryptDecrypt.decrypt(response.get("enc_data").toString()));
                            Log.d("Send Money", decryptedResponse.toString());

                            if(decryptedResponse.getJSONObject("status").getInt("code") != 200) {
                                Toast.makeText(getApplicationContext(), "Error: " + decryptedResponse.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();

                                return;
                                // This is buggy. Need to call Login activity again if incorrect credentials are given
                            }



                            Toast.makeText(getApplicationContext(),""+EncryptDecrypt.decrypt(response.get("enc_data").toString()), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        startActivity(new Intent(SendMoney.this, Dashboard.class));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong[Send]", Toast.LENGTH_SHORT).show();
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



    public void Biometrics(View view){
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){

            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(this,"Authenticate to continue",Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this,"No fingerprint sensor",Toast.LENGTH_LONG).show();
                send.setVisibility(View.INVISIBLE);

                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this,"Biometric sensor is not available",Toast.LENGTH_LONG).show();
                send.setVisibility(View.INVISIBLE);

                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this,"Your device don't have any fingerprint, check your security setting",Toast.LENGTH_LONG).show();
                send.setVisibility(View.INVISIBLE);
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);


        final BiometricPrompt biometricPrompt = new BiometricPrompt(SendMoney.this,executor,new BiometricPrompt.AuthenticationCallback(){

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),"Transfer Successful",Toast.LENGTH_LONG).show();
                sendMoney();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        final BiometricPrompt.PromptInfo  promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("User fingerprint to Proceed")
                .setDeviceCredentialAllowed(true)
                .build();



        biometricPrompt.authenticate(promptInfo);



    }

}