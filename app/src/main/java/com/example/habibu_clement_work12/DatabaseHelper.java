package com.example.habibu_clement_work12;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "student_database.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_STUDENT_DETAILS = "student_details";
    private static final String TABLE_STUDENT_COURSES = "student_courses";

    // Student Details columns
    private static final String COL_STUDENT_ID = "student_id";
    private static final String COL_STUDENT_NAME = "student_name";
    private static final String COL_ENROLLMENT_DATE = "enrollment_date";
    private static final String COL_AGE = "age";
    private static final String COL_EMAIL = "email";
    private static final String COL_PHONE = "phone";
    private static final String COL_DEPARTMENT = "department";
    private static final String COL_STATUS = "status";
    private static final String COL_PHOTO = "photo";

    // Student Courses columns
    private static final String COL_COURSE_ID = "course_id";
    private static final String COL_COURSE_NAME = "course_name";
    private static final String COL_COURSE_CODE = "course_code";
    private static final String COL_CREDITS = "credits";
    private static final String COL_GRADE = "grade";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create student_details table
        String createStudentTable = "CREATE TABLE " + TABLE_STUDENT_DETAILS + " (" +
                COL_STUDENT_ID + " TEXT PRIMARY KEY, " +
                COL_STUDENT_NAME + " TEXT NOT NULL, " +
                COL_ENROLLMENT_DATE + " TEXT NOT NULL, " +
                COL_AGE + " INTEGER NOT NULL, " +
                COL_EMAIL + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_DEPARTMENT + " TEXT NOT NULL, " +
                COL_STATUS + " TEXT, " +
                COL_PHOTO + " BLOB" +
                ")";

        // Create student_courses table with foreign key
        String createCoursesTable = "CREATE TABLE " + TABLE_STUDENT_COURSES + " (" +
                COL_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_ID + " TEXT NOT NULL, " +
                COL_COURSE_NAME + " TEXT NOT NULL, " +
                COL_COURSE_CODE + " TEXT NOT NULL, " +
                COL_CREDITS + " INTEGER NOT NULL, " +
                COL_GRADE + " TEXT, " +
                "FOREIGN KEY(" + COL_STUDENT_ID + ") REFERENCES " + 
                TABLE_STUDENT_DETAILS + "(" + COL_STUDENT_ID + ") ON DELETE CASCADE" +
                ")";

        db.execSQL(createStudentTable);
        db.execSQL(createCoursesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_DETAILS);
        onCreate(db);
    }

    // Convert Bitmap to byte array
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Convert byte array to Bitmap
    private Bitmap byteArrayToBitmap(byte[] image) {
        if (image == null) return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    // Insert student
    public long insertStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_ID, student.getStudentId());
        values.put(COL_STUDENT_NAME, student.getStudentName());
        values.put(COL_ENROLLMENT_DATE, student.getEnrollmentDate());
        values.put(COL_AGE, student.getAge());
        values.put(COL_EMAIL, student.getEmail());
        values.put(COL_PHONE, student.getPhone());
        values.put(COL_DEPARTMENT, student.getDepartment());
        values.put(COL_STATUS, student.getStatus());
        values.put(COL_PHOTO, bitmapToByteArray(student.getPhoto()));

        long result = db.insert(TABLE_STUDENT_DETAILS, null, values);
        db.close();
        return result;
    }

    // Get all students
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENT_DETAILS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setStudentId(cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)));
                student.setStudentName(cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NAME)));
                student.setEnrollmentDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_ENROLLMENT_DATE)));
                student.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COL_AGE)));
                student.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
                student.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)));
                student.setDepartment(cursor.getString(cursor.getColumnIndexOrThrow(COL_DEPARTMENT)));
                student.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)));
                byte[] photoBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_PHOTO));
                student.setPhoto(byteArrayToBitmap(photoBytes));
                studentList.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return studentList;
    }

    // Get student by ID
    public Student getStudentById(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENT_DETAILS, null, 
                COL_STUDENT_ID + "=?", new String[]{studentId}, 
                null, null, null);

        Student student = null;
        if (cursor != null && cursor.moveToFirst()) {
            student = new Student();
            student.setStudentId(cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)));
            student.setStudentName(cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NAME)));
            student.setEnrollmentDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_ENROLLMENT_DATE)));
            student.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COL_AGE)));
            student.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
            student.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)));
            student.setDepartment(cursor.getString(cursor.getColumnIndexOrThrow(COL_DEPARTMENT)));
            student.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)));
            byte[] photoBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_PHOTO));
            student.setPhoto(byteArrayToBitmap(photoBytes));
            cursor.close();
        }
        db.close();
        return student;
    }

    // Update student
    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_NAME, student.getStudentName());
        values.put(COL_ENROLLMENT_DATE, student.getEnrollmentDate());
        values.put(COL_AGE, student.getAge());
        values.put(COL_EMAIL, student.getEmail());
        values.put(COL_PHONE, student.getPhone());
        values.put(COL_DEPARTMENT, student.getDepartment());
        values.put(COL_STATUS, student.getStatus());
        values.put(COL_PHOTO, bitmapToByteArray(student.getPhoto()));

        int result = db.update(TABLE_STUDENT_DETAILS, values, 
                COL_STUDENT_ID + "=?", new String[]{student.getStudentId()});
        db.close();
        return result;
    }

    // Delete student
    public int deleteStudent(String studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_STUDENT_DETAILS, COL_STUDENT_ID + "=?", 
                new String[]{studentId});
        db.close();
        return result;
    }

    // Insert course
    public long insertCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_ID, course.getStudentId());
        values.put(COL_COURSE_NAME, course.getCourseName());
        values.put(COL_COURSE_CODE, course.getCourseCode());
        values.put(COL_CREDITS, course.getCredits());
        values.put(COL_GRADE, course.getGrade());

        long result = db.insert(TABLE_STUDENT_COURSES, null, values);
        db.close();
        return result;
    }

    // Get courses by student ID
    public List<Course> getCoursesByStudentId(String studentId) {
        List<Course> courseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENT_COURSES, null, 
                COL_STUDENT_ID + "=?", new String[]{studentId}, 
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setCourseId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_COURSE_ID)));
                course.setStudentId(cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)));
                course.setCourseName(cursor.getString(cursor.getColumnIndexOrThrow(COL_COURSE_NAME)));
                course.setCourseCode(cursor.getString(cursor.getColumnIndexOrThrow(COL_COURSE_CODE)));
                course.setCredits(cursor.getInt(cursor.getColumnIndexOrThrow(COL_CREDITS)));
                course.setGrade(cursor.getString(cursor.getColumnIndexOrThrow(COL_GRADE)));
                courseList.add(course);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return courseList;
    }
}
