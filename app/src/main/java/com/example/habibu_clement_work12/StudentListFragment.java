package com.example.habibu_clement_work12;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentListFragment extends Fragment {
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextView textViewEmpty;
    private LinearLayout layoutEmptyState;
    private Button buttonAddStudent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);
        
        recyclerView = view.findViewById(R.id.recyclerViewStudents);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);
        buttonAddStudent = view.findViewById(R.id.buttonAddStudent);
        
        // Setup Add Student button
        buttonAddStudent.setOnClickListener(v -> addNewStudent());
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StudentAdapter(new ArrayList<>(), new StudentAdapter.OnStudentClickListener() {
            @Override
            public void onStudentClick(Student student) {
                viewStudentDetails(student);
            }

            @Override
            public void onStudentEdit(Student student) {
                editStudent(student);
            }

            @Override
            public void onStudentDelete(Student student) {
                deleteStudent(student);
            }
        });
        recyclerView.setAdapter(adapter);
        
        loadStudents();
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStudents(); // Refresh list when returning from form
    }

    private void loadStudents() {
        List<Student> students = dbHelper.getAllStudents();
        adapter.updateStudents(students);
        
        if (students.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    private void addNewStudent() {
        StudentFormFragment formFragment = new StudentFormFragment();
        // No arguments = new student mode
        
        if (getActivity() instanceof Activity3) {
            ((Activity3) getActivity()).loadFragment(formFragment, true);
        }
    }

    private void viewStudentDetails(Student student) {
        // Load detail fragment
        StudentDetailFragment detailFragment = new StudentDetailFragment();
        Bundle args = new Bundle();
        args.putString("studentId", student.getStudentId());
        detailFragment.setArguments(args);
        
        if (getActivity() instanceof Activity3) {
            ((Activity3) getActivity()).loadFragment(detailFragment, true);
        }
    }

    private void editStudent(Student student) {
        StudentFormFragment formFragment = new StudentFormFragment();
        Bundle args = new Bundle();
        args.putString("studentId", student.getStudentId());
        formFragment.setArguments(args);
        
        if (getActivity() instanceof Activity3) {
            ((Activity3) getActivity()).loadFragment(formFragment, true);
        }
    }

    private void deleteStudent(Student student) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage(getString(R.string.delete_confirm))
                .setPositiveButton("Delete", (dialog, which) -> {
                    int result = dbHelper.deleteStudent(student.getStudentId());
                    if (result > 0) {
                        Toast.makeText(getContext(), getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                        loadStudents();
                    } else {
                        Toast.makeText(getContext(), "Error deleting student", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

