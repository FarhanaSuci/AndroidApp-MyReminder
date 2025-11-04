package com.example.smartalarm;

public class TransactionModel {
    private  String id;
    private String note;
    private String amount;
    private String type;
    private String date;
    private TransactionModel(){

    }
    public TransactionModel(String id,String note,String amount,String type,String date){
        this.id =id ;
        this.note = note;
        this.amount = amount;
        this.type = type;
        this.date = date;

    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setType(String type) {
        this.type = type;
    }

    public  String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNote(){
        return note;
    }
}
