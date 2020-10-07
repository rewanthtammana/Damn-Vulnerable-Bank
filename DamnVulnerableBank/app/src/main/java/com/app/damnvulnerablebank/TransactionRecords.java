package com.app.damnvulnerablebank;

import android.widget.ImageView;

public class TransactionRecords {
    private String fromaccnt;
    private  String toaccnt;
    private  String amount;


    public TransactionRecords(){}
    public TransactionRecords(String fromacc, String toacc, String amount){
        this.fromaccnt=fromacc;
        this.toaccnt=toacc;
        this.amount=amount;
    }

    public String getFromaccnt() {
        return fromaccnt;
    }

    public void setFromaccnt(String fromaccnt) {
        this.fromaccnt = "From Account Number\t\t:"+fromaccnt;
    }

    public String getToaccnt() {
        return toaccnt;
    }

    public void setToaccnt(String toaccnt) {
        this.toaccnt = "To    Account Number\t\t:"+toaccnt;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = "Amount Sent\t\t:$"+amount;
    }

}



