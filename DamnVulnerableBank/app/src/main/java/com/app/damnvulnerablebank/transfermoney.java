package com.app.damnvulnerablebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;

public class transfermoney extends AppCompatActivity {

    EditText e1,e2;
    public void routes(View v){
        e1=findViewById(R.id.username);
        String valu =e1.getText().toString();
        e2=findViewById(R.id.amount);
        String valu1 =e2.getText().toString();
        Intent next = new Intent();
        String url = "dvba://sendmoney?username="+valu+"&amount="+valu1;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfermoney);
    }
}