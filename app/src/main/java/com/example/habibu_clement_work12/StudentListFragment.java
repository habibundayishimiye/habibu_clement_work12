package com.example.habibu_clement_work12;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class StudentListFragment extends Fragment {
    private RecyclerView recyclerViewStudents;
    private StudentAdapter studentAdapter;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton fabAddStudent;
    private LinearLayout layoutEmptyState;
    private Button btnAddStudentEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerViewStudents = view.findViewById(R.id.recyclerViewStudents);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(getContext()));
        
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);
        btnAddStudentEmpty = view.findViewById(R.id.btnAddStudentEmpty);
        
        fabAddStudent = view.findViewById(R.id.fabAddStudent);
        fabAddStudent.setOnClickListener(v -> openAddStudentForm());
        
        btnAddStudentEmpty.setOnClickListener(v -> openAddStudentForm());
        
        databaseHelper = new DatabaseHelper(getContext());
        loadStudents();
    }

    private void openAddStudentForm() {
        if (getActivity() instanceof Activity3) {
            ((Activity3) getActivity()).loadFragment(new StudentFormFragment());
        }
    }

    private void loadStudents() {
        List<Student> students = databaseHelper.getAllStudents();
        
        // Show/hide empty state
        if (students == null || students.isEmpty()) {
            recyclerViewStudents.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerViewStudents.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
        }
        
        studentAdapter = new StudentAdapter(students);
        studentAdapter.setOnStudentActionListener(new StudentAdapter.OnStudentActionListener() {
            @Override
            public void onViewClick(String studentId) {
                if (getActivity() instanceof Activity3) {
                    ((Activity3) getActivity()).loadStudentDetailFragment(studentId);
                }
            }

            @Override
            public void onEditClick(String studentId) {
                if (getActivity() instanceof Activity3) {
                    ((Activity3) getActivity()).loadStudentFormFragment(studentId);
                }
            }

            @Override
            public void onDeleteClick(String studentId) {
                showDeleteConfirmation(studentId);
            }
        });
        recyclerViewStudents.setAdapter(studentAdapter);
    }

    private void showDeleteConfirmation(String studentId) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to delete this student? This will also delete all associated courses.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    int result = databaseHelper.deleteStudent(studentId);
                    if (result > 0) {
                        android.widget.Toast.makeText(getContext(), "Student deleted successfully", android.widget.Toast.LENGTH_SHORT).show();
                        loadStudents(); // Refresh the list
                    } else {
                        android.widget.Toast.makeText(getContext(), "Error deleting student", android.widget.Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStudents(); // Refresh list when fragment resumes
    }
}
