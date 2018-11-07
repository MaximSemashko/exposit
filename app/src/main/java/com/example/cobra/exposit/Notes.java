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

    public Notes(String name, String date) {

        this.name = name;
        this.date = date;
    }

    private String date;
}
