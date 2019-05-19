package com.example.myapplication.Bean;

import java.io.Serializable;

public class Disease implements Serializable{
    int id;
    String name;
    public Disease(){

    }

    public Disease(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
