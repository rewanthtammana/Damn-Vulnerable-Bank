package com.app.damnvulnerablebank;

public class Brecords {
    private String benificiaryaccnt;
    private String isapproved;

    public   Brecords(){}

    public String getBenificiaryaccnt() {
        return benificiaryaccnt;
    }

    public void setBenificiaryaccnt(String benificiaryaccnt) {
        this.benificiaryaccnt = "Account Number\n"+benificiaryaccnt+"\n\n";
    }

    public String getIsapproved() {
        return isapproved;
    }

    public void setIsapproved(String isapproved) {
        this.isapproved = "\n\nAccount Approved By Admin\n"+isapproved;
    }

    public  Brecords(String benificiaryaccnt, String isapproved){

    }
}
