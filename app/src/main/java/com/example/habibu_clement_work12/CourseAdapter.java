package com.example.habibu_clement_work12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courses;

    public CourseAdapter(List<Course> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        if (courses != null && position < courses.size()) {
            Course course = courses.get(position);
            holder.bind(course);
        }
    }

    @Override
    public int getItemCount() {
        return courses != null ? courses.size() : 0;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName, textViewCode, textViewCredits, textViewGrade;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewCourseName);
            textViewCode = itemView.findViewById(R.id.textViewCourseCode);
            textViewCredits = itemView.findViewById(R.id.textViewCourseCredits);
            textViewGrade = itemView.findViewById(R.id.textViewCourseGrade);
        }

        public void bind(Course course) {
            textViewName.setText(course.getCourseName());
            textViewCode.setText("Code: " + course.getCourseCode());
            textViewCredits.setText("Credits: " + course.getCredits());
            textViewGrade.setText("Grade: " + (course.getGrade() != null ? course.getGrade() : "N/A"));
        }
    }
}






