package com.app.damnvulnerablebank;
// 클래스 정의

public class BeneficiaryRecords {
    private String benificiaryaccnt;
    private String isapproved;

    public BeneficiaryRecords(){}

    public String getBeneficiaryAccount() {
        return benificiaryaccnt;
    }

    public void setBeneficiaryAccount(String benificiaryaccnt) {
        this.benificiaryaccnt = "수취자 계좌 번호:\n"+benificiaryaccnt+"\n\n";
    }

    public String getIsApproved() {
        return isapproved;
    }

    public void setIsApproved(String isapproved) {
        if (isapproved.equals("true")) {
            this.isapproved = "\n관리자 승인 여부:\n" + "승인";
        }
        else {
            this.isapproved = "\n관리자 승인 여부:\n" + "거부";
        }
    }

    public BeneficiaryRecords(String benificiaryaccnt, String isapproved){

    }
}
