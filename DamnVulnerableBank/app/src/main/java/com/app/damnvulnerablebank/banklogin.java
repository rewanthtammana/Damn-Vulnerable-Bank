package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class banklogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_banklogin);
        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
        }
    }
    public void loginpage(View view){
        Intent intent =new Intent(getApplicationContext(),login.class);
        startActivity(intent);
    }
    public void signupage(View view){
        Intent intent =new Intent(getApplicationContext(),signup.class);
        startActivity(intent);
    }
    public void usage(View v){
        Uri uri = Uri.parse("https://www.github.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}