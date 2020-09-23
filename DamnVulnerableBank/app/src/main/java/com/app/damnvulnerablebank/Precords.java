package com.app.damnvulnerablebank;

public class Precords {
    private String account_number;
    private String    beneficiary_account_number;
    private String     idd;

    public Precords(){}

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = "Account Number\t\t:"+account_number;
    }

    public String getBeneficiary_account_number() {
        return beneficiary_account_number;
    }

    public void setBeneficiary_account_number(String beneficiary_account_number) {
        this.beneficiary_account_number = "Benificiary Account Number\t\t:"+beneficiary_account_number+"\n";
    }

    public String getIdd() {
        return idd;
    }

    public void setIdd(String idd) {
        this.idd = "ID\t\t:"+idd;
    }

    public Precords(String account_number, String    beneficiary_account_number, String idd){}

}
