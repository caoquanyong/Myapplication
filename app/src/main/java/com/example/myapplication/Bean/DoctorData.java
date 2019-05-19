package com.example.myapplication.Bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;


public class DoctorData implements Serializable {
    private Drawable icon;
    private String text;

    public DoctorData(Drawable icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }
}