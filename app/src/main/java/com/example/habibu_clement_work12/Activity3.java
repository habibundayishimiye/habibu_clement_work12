package com.example.habibu_clement_work12;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Activity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false); // We'll handle back button manually
                getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide default title since we have custom layout
            }
        }

        // Setup Back button
        ImageButton btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Setup Add Student button in toolbar
        Button btnAddStudentToolbar = findViewById(R.id.btnAddStudentToolbar);
        if (btnAddStudentToolbar != null) {
            btnAddStudentToolbar.setOnClickListener(v -> loadFragment(new StudentFormFragment()));
        }

        // Load StudentListFragment by default
        if (savedInstanceState == null) {
            loadFragment(new StudentListFragment());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity3_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.menu_add_student) {
            loadFragment(new StudentFormFragment());
            return true;
        } else if (id == R.id.menu_view_students) {
            loadFragment(new StudentListFragment());
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void loadStudentDetailFragment(String studentId) {
        StudentDetailFragment fragment = new StudentDetailFragment();
        Bundle args = new Bundle();
        args.putString("studentId", studentId);
        fragment.setArguments(args);
        loadFragment(fragment);
    }

    public void loadStudentFormFragment(String studentId) {
        StudentFormFragment fragment = new StudentFormFragment();
        Bundle args = new Bundle();
        args.putString("studentId", studentId);
        fragment.setArguments(args);
        loadFragment(fragment);
    }
}
