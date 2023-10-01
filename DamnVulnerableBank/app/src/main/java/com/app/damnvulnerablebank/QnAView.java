package com.app.damnvulnerablebank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class QnAView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_view);
    }

    public void writeQnA(View V) {
        startActivity(new Intent(getApplicationContext(), QnAWrite.class));
    }

}
