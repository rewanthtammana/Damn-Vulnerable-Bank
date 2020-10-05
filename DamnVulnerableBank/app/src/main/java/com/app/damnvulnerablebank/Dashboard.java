package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    public void logout(View view){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("jwt", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isloggedin", false);
        editor.apply();
        startActivity(new Intent(getApplicationContext(), banklogin.class));
    }

    public void addben(View v){
        startActivity(new Intent(getApplicationContext(),addbenif.class));
    }

     public void balnc(View V){
        startActivity(new Intent(getApplicationContext(),balanceview.class));
}
    public void transactions(View V){
        startActivity(new Intent(getApplicationContext(),transactions.class));
    }
    public void viewben(View V){
        startActivity(new Intent(getApplicationContext(),viewbenif.class));
    }
    public void benfs(View V){
        startActivity(new Intent(getApplicationContext(),pendingbenificiary.class));
    }
    public void mybenifs(View V){
        startActivity(new Intent(getApplicationContext(),mybenifview.class));
    }

    public void reset(View v){
        startActivity(new Intent(getApplicationContext(),passreset.class));
    }

    public void curc(View v){
        startActivity(new Intent(getApplicationContext(),currencyrates.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        TextView t1=findViewById(R.id.dasht);
        if(RootUtil.isDeviceRooted()) {
            Toast.makeText(getApplicationContext(), "Phone is Rooted", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}