package com.example.myapplication.Bean;

public class Question {
    private int id;
    private int questionNo;
    private String question;
    private int departmentId;
    public Question(){
    }
    public Question(int id, int questionNo, String question, int departmentId) {
        this.id = id;
        this.questionNo = questionNo;
        this.question = question;
        this.departmentId = departmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
