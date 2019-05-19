package com.example.myapplication.Bean;


public class Report{
    private String name;
    private String value;
    private String time;

    public Report(String name, String value) {
        this.name = name;
        this.value = value;
    }
    public void setTime(String time){
        this.time=time;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getTime() {
        return time;
    }
}
