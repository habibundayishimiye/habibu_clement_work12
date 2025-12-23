package com.example.habibu_clement_work12;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentDetailFragment extends Fragment {
    private TextView txtStudentName, txtStudentId, txtEnrollmentDate, txtAge, 
                     txtEmail, txtPhone, txtDepartment, txtStatus;
    private ImageView imgStudentPhoto;
    private RecyclerView recyclerViewCourses;
    private CourseAdapter courseAdapter;
    private DatabaseHelper databaseHelper;
    private String studentId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        databaseHelper = new DatabaseHelper(getContext());
        
        Bundle args = getArguments();
        if (args != null) {
            studentId = args.getString("studentId");
        }
        
        initializeViews(view);
        loadStudentData();
        loadCourses();
    }

    private void initializeViews(View view) {
        imgStudentPhoto = view.findViewById(R.id.imgStudentPhoto);
        txtStudentName = view.findViewById(R.id.txtStudentName);
        txtStudentId = view.findViewById(R.id.txtStudentId);
        txtEnrollmentDate = view.findViewById(R.id.txtEnrollmentDate);
        txtAge = view.findViewById(R.id.txtAge);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtDepartment = view.findViewById(R.id.txtDepartment);
        txtStatus = view.findViewById(R.id.txtStatus);
        recyclerViewCourses = view.findViewById(R.id.recyclerViewCourses);
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Setup Edit and Delete buttons
        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnDelete = view.findViewById(R.id.btnDelete);
        
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                // Navigate to edit form
                if (getActivity() instanceof Activity3) {
                    ((Activity3) getActivity()).loadStudentFormFragment(studentId);
                }
            });
        }
        
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> showDeleteConfirmation());
        }
    }

    private void loadStudentData() {
        if (studentId == null) return;
        
        Student student = databaseHelper.getStudentById(studentId);
        if (student != null) {
            // Display student photo
            if (student.getPhoto() != null) {
                imgStudentPhoto.setImageBitmap(student.getPhoto());
            } else {
                imgStudentPhoto.setImageResource(android.R.drawable.ic_menu_gallery);
            }
            
            txtStudentName.setText(student.getStudentName());
            txtStudentId.setText("Student ID: " + student.getStudentId());
            txtEnrollmentDate.setText("Enrollment Date: " + student.getEnrollmentDate());
            txtAge.setText("Age: " + student.getAge());
            txtEmail.setText("Email: " + (student.getEmail() != null && !student.getEmail().isEmpty() ? student.getEmail() : "N/A"));
            txtPhone.setText("Phone: " + (student.getPhone() != null && !student.getPhone().isEmpty() ? student.getPhone() : "N/A"));
            txtDepartment.setText("Department: " + student.getDepartment());
            txtStatus.setText("Status: " + (student.getStatus() != null ? student.getStatus() : "N/A"));
        }
    }

    private void loadCourses() {
        if (studentId == null) return;
        
        List<Course> courses = databaseHelper.getCoursesByStudentId(studentId);
        courseAdapter = new CourseAdapter(courses);
        recyclerViewCourses.setAdapter(courseAdapter);
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to delete this student? This will also delete all associated courses.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    int result = databaseHelper.deleteStudent(studentId);
                    if (result > 0) {
                        Toast.makeText(getContext(), "Student deleted successfully", Toast.LENGTH_SHORT).show();
                        if (getActivity() instanceof Activity3) {
                            ((Activity3) getActivity()).loadFragment(new StudentListFragment());
                        }
                    } else {
                        Toast.makeText(getContext(), "Error deleting student", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
