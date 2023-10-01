package com.app.damnvulnerablebank;

public class PendingBeneficiaryRecords {
    private String account_number;
    private String beneficiary_account_number;
    private String id;

    public PendingBeneficiaryRecords(){}

    public String getAccountNumber() {
        return account_number;
    }

    public void setAccountNumber(String account_number) {
        this.account_number = "보내는 분:\t:"+account_number;
    }

    public String getBeneficiaryAccountNumber() {
        return beneficiary_account_number;
    }

    public void setBeneficiaryAccountNumber(String beneficiary_account_number) {
        this.beneficiary_account_number = "받는 분:\t:"+beneficiary_account_number+"\n";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = "ID\t\t:" + id;
    }

    public PendingBeneficiaryRecords(String account_number, String beneficiary_account_number, String idd){}

}
