package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hanks.passcodeview.PasscodeView;

public class passcode extends AppCompatActivity {
    PasscodeView passcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        passcodeView=findViewById(R.id.passy);
        passcodeView.setPasscodeLength(6).setLocalPasscode("123456").setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {
                Toast.makeText(passcode.this,"Passcode is wrong",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String number) {
                Intent intent =new Intent(getApplicationContext(),banksecrets.class);
                startActivity(intent);
            }
        });
    }
}