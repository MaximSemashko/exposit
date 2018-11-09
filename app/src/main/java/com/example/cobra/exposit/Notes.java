package com.example.cobra.exposit;

public class Notes {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Notes() {

    }

    private String date;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public Notes(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public Notes(String name, String date, String userId) {
        this.name = name;
        this.date = date;
        this.userId=userId;
    }
}
