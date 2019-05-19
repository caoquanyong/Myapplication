package com.example.myapplication.Bean;

public class User {
    private String username;
    private String password;
    private String trueName;
    private String gender;
    private String birthday;
    private String tel;
    private String address;
    private String postalCode;
    private String emergency_people;
    private String emergency_tel;
    public User(){
    }

    public User(String username, String password, String trueName, String gender, String birthday, String tel, String address, String postalCode, String emergency_people, String emergency_tel) {
        this.username = username;
        this.password = password;
        this.trueName = trueName;
        this.gender = gender;
        this.birthday = birthday;
        this.tel = tel;
        this.address = address;
        this.postalCode = postalCode;
        this.emergency_people = emergency_people;
        this.emergency_tel = emergency_tel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmergency_people() {
        return emergency_people;
    }

    public void setEmergency_people(String emergency_people) {
        this.emergency_people = emergency_people;
    }

    public String getEmergency_tel() {
        return emergency_tel;
    }

    public void setEmergency_tel(String emergency_tel) {
        this.emergency_tel = emergency_tel;
    }
}
