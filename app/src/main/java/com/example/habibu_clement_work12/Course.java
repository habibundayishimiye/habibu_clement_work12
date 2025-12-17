package com.example.habibu_clement_work12;

public class Course {
    private int courseId;
    private String studentId;
    private String courseName;
    private String courseCode;
    private int credits;
    private String grade;

    public Course() {
    }

    public Course(String studentId, String courseName, String courseCode, int credits, String grade) {
        this.studentId = studentId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
        this.grade = grade;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}

