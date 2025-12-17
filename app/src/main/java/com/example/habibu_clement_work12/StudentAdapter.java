package com.example.habibu_clement_work12;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Student> students;
    private OnStudentClickListener listener;

    public interface OnStudentClickListener {
        void onStudentClick(Student student);
        void onStudentEdit(Student student);
        void onStudentDelete(Student student);
    }

    public StudentAdapter(List<Student> students, OnStudentClickListener listener) {
        this.students = students;
        this.listener = listener;
    }

    public void updateStudents(List<Student> newStudents) {
        this.students = newStudents;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        holder.bind(student);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName, textViewId, textViewDepartment, textViewStatus;
        private ImageView imageViewPhoto;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewDepartment = itemView.findViewById(R.id.textViewDepartment);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStudentClick(students.get(getAdapterPosition()));
                }
            });

            itemView.findViewById(R.id.buttonEdit).setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStudentEdit(students.get(getAdapterPosition()));
                }
            });

            itemView.findViewById(R.id.buttonDelete).setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStudentDelete(students.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Student student) {
            textViewName.setText(student.getStudentName());
            textViewId.setText("ID: " + student.getStudentId());
            textViewDepartment.setText(student.getDepartment());
            textViewStatus.setText(student.getStatus() != null ? student.getStatus() : "N/A");

            if (student.getPhoto() != null) {
                imageViewPhoto.setImageBitmap(student.getPhoto());
                imageViewPhoto.setVisibility(View.VISIBLE);
            } else {
                imageViewPhoto.setVisibility(View.GONE);
            }
        }
    }
}

