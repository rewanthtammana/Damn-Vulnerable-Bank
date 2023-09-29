package com.app.damnvulnerablebank;

public class BoardListRecords {

    private String id;
    private String date;
    private String writer;
    private String subject;

    public BoardListRecords() { }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String subject) {
        this.date = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String subject) {
        this.id = subject;
    }
}
