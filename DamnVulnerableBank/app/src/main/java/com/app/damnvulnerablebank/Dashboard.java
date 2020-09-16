package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(getApplicationContext(), banklogin.class));
    }

    public void deeptran(View v){
        startActivity(new Intent(getApplicationContext(), transfermoney.class));
    }

    public void secretnoteso(View v){
        startActivity(new Intent(getApplicationContext(), passcode.class));
    }

    public void forates(View v){
        startActivity(new Intent(getApplicationContext(), frates.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }
}