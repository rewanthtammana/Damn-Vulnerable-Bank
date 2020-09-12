package com.app.damnvulnerablebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    FirebaseAuth mauth;
    private ProgressBar spinner;
    private RelativeLayout priv;
    public void backtomain(View view){
        Intent into =new Intent(login.this,banklogin.class);
        startActivity(into);
    }

    public void forgets(View view){
        Intent into =new Intent(login.this,requestpassword.class);
        startActivity(into);
    }
    public void firebaselogin(View view)
    {mauth = FirebaseAuth.getInstance();
        EditText inputEmail=findViewById(R.id.loginemail_editText);
        EditText inputPaassword=findViewById(R.id.login_password_editText);
        priv=(RelativeLayout)findViewById(R.id.relp);
        spinner = (ProgressBar)findViewById(R.id.progressb);
        priv.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        String email =inputEmail.getText().toString().trim();
        String password =inputPaassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "ENTER EMAIL", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "ENTER PASSWORD", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<6){
            Toast.makeText(getApplicationContext(), "Enter valid length", Toast.LENGTH_SHORT).show();
            return;
        }

        mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent intent=new Intent(login.this,Dashboard.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(login.this, "INVALID CREDENTIALS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }
}