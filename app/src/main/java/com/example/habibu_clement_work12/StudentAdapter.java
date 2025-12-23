package com.example.habibu_clement_work12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Student> students;
    private OnStudentActionListener listener;

    public interface OnStudentActionListener {
        void onViewClick(String studentId);
        void onEditClick(String studentId);
        void onDeleteClick(String studentId);
    }

    public StudentAdapter(List<Student> students) {
        this.students = students;
    }

    public void setOnStudentActionListener(OnStudentActionListener listener) {
        this.listener = listener;
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
        holder.textViewStudentName.setText(student.getStudentName());
        holder.textViewStudentId.setText("ID: " + student.getStudentId());
        holder.textViewDepartment.setText("Department: " + student.getDepartment());
        holder.textViewStatus.setText("Status: " + (student.getStatus() != null ? student.getStatus() : "N/A"));

        if (student.getPhoto() != null) {
            holder.imageViewStudentPhoto.setImageBitmap(student.getPhoto());
        } else {
            holder.imageViewStudentPhoto.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Setup button click listeners
        holder.btnView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewClick(student.getStudentId());
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(student.getStudentId());
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(student.getStudentId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return students != null ? students.size() : 0;
    }

    public void updateStudents(List<Student> newStudents) {
        this.students = newStudents;
        notifyDataSetChanged();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewStudentPhoto;
        TextView textViewStudentName;
        TextView textViewStudentId;
        TextView textViewDepartment;
        TextView textViewStatus;
        Button btnView;
        Button btnEdit;
        Button btnDelete;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewStudentPhoto = itemView.findViewById(R.id.imageViewStudentPhoto);
            textViewStudentName = itemView.findViewById(R.id.textViewStudentName);
            textViewStudentId = itemView.findViewById(R.id.textViewStudentId);
            textViewDepartment = itemView.findViewById(R.id.textViewDepartment);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            btnView = itemView.findViewById(R.id.btnView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
