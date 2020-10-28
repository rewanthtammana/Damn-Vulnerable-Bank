package com.app.damnvulnerablebank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Dashboard.super.onBackPressed();
                        System.exit(0);
                    }
                }).create().show();
    }


    public void logout(View view){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("jwt", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isloggedin", false);
        editor.apply();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    public void addBeneficiary(View v){
        startActivity(new Intent(getApplicationContext(), AddBeneficiary.class));
    }
    public void myprofile(View V){
        startActivity(new Intent(getApplicationContext(), Myprofile.class));
    }
    public void viewbalance(View V){
        startActivity(new Intent(getApplicationContext(), ViewBalance.class));
    }
    public void viewTransactions(View V){
        startActivity(new Intent(getApplicationContext(), GetTransactions.class));
    }
    public void viewBeneficiaryAdmin(View V){
        startActivity(new Intent(getApplicationContext(), ViewBeneficiaryAdmin.class));
    }
    public void getPendingBeneficiaries(View V){
        startActivity(new Intent(getApplicationContext(), PendingBeneficiary.class));
    }
    public void viewMyBeneficiaries(View V){
        startActivity(new Intent(getApplicationContext(), ViewBeneficiary.class));
    }
    public void resetPassword(View v){
        startActivity(new Intent(getApplicationContext(), ResetPassword.class));
    }
    public void getCurrencyRates(View v){
        startActivity(new Intent(getApplicationContext(), CurrencyRates.class));
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