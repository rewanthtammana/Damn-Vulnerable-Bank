package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class confirmtrans extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmtrans);
        Intent intent = getIntent();

        Uri data = intent.getData();
        assert data != null;

        String a = data.getQueryParameter("username");
        String b = data.getQueryParameter("amount");
        TextView t1 = findViewById(R.id.con);
        t1.setText("Username\n" + a);
        TextView t2 = findViewById(R.id.con1);
        t2.setText("Amount\n" + b);
    }
}