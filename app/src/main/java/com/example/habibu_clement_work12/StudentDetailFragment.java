package com.example.habibu_clement_work12;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentDetailFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private Student student;
    private RecyclerView recyclerViewCourses;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
        
        Bundle args = getArguments();
        if (args != null && args.containsKey("studentId")) {
            String studentId = args.getString("studentId");
            student = dbHelper.getStudent(studentId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_detail, container, false);
        
        if (student != null) {
            populateDetails(view);
            loadCourses(view);
        }
        
        return view;
    }

    private void populateDetails(View view) {
        TextView textViewName = view.findViewById(R.id.textViewDetailName);
        TextView textViewId = view.findViewById(R.id.textViewDetailId);
        TextView textViewDate = view.findViewById(R.id.textViewDetailDate);
        TextView textViewAge = view.findViewById(R.id.textViewDetailAge);
        TextView textViewEmail = view.findViewById(R.id.textViewDetailEmail);
        TextView textViewPhone = view.findViewById(R.id.textViewDetailPhone);
        TextView textViewDepartment = view.findViewById(R.id.textViewDetailDepartment);
        TextView textViewStatus = view.findViewById(R.id.textViewDetailStatus);
        ImageView imageViewPhoto = view.findViewById(R.id.imageViewDetailPhoto);
        
        textViewName.setText(student.getStudentName());
        textViewId.setText("ID: " + student.getStudentId());
        textViewDate.setText("Enrollment Date: " + student.getEnrollmentDate());
        textViewAge.setText("Age: " + student.getAge());
        textViewEmail.setText("Email: " + (student.getEmail() != null ? student.getEmail() : "N/A"));
        textViewPhone.setText("Phone: " + (student.getPhone() != null ? student.getPhone() : "N/A"));
        textViewDepartment.setText("Department: " + student.getDepartment());
        textViewStatus.setText("Status: " + (student.getStatus() != null ? student.getStatus() : "N/A"));
        
        if (student.getPhoto() != null) {
            imageViewPhoto.setImageBitmap(student.getPhoto());
            imageViewPhoto.setVisibility(View.VISIBLE);
        }
    }

    private void loadCourses(View view) {
        recyclerViewCourses = view.findViewById(R.id.recyclerViewCourses);
        List<Course> courses = dbHelper.getCoursesByStudentId(student.getStudentId());
        
        CourseAdapter adapter = new CourseAdapter(courses);
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCourses.setAdapter(adapter);
    }
}

