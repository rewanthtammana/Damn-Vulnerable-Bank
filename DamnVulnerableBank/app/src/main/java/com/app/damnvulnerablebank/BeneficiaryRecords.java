package com.app.damnvulnerablebank;

public class BeneficiaryRecords {
    private String benificiaryaccnt;
    private String isapproved;

    public BeneficiaryRecords(){}

    public String getBeneficiaryAccount() {
        return benificiaryaccnt;
    }

    public void setBeneficiaryAccount(String benificiaryaccnt) {
        this.benificiaryaccnt = "Account Number\n"+benificiaryaccnt+"\n\n";
    }

    public String getIsApproved() {
        return isapproved;
    }

    public void setIsApproved(String isapproved) {
        this.isapproved = "\n\nAccount Approved By Admin\n" + isapproved;
    }

    public BeneficiaryRecords(String benificiaryaccnt, String isapproved){

    }
}
