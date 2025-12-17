package com.example.habibu_clement_work12;

import android.graphics.Bitmap;

public class Student {
    private String studentId;
    private String studentName;
    private String enrollmentDate;
    private int age;
    private String email;
    private String phone;
    private String department;
    private String status;
    private Bitmap photo;

    public Student() {
    }

    public Student(String studentId, String studentName, String enrollmentDate, 
                   int age, String email, String phone, String department, String status) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.enrollmentDate = enrollmentDate;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}

