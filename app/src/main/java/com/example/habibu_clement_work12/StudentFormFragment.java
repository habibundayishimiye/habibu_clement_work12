package com.example.habibu_clement_work12;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.InputStream;
import java.util.Calendar;

public class StudentFormFragment extends Fragment {
    private EditText editStudentId, editStudentName, editEnrollmentDate, editAge, editEmail, editPhone;
    private Spinner spinnerDepartment;
    private RadioGroup radioGroupStatus;
    private RadioButton radioActive, radioInactive, radioGraduated;
    private CheckBox checkBoxCurrentlyActive;
    private ImageView imgStudentPhoto;
    private Button btnSelectPhoto, btnSave, btnCancel;
    private DatabaseHelper databaseHelper;
    private String studentIdToEdit;
    private Bitmap selectedPhoto;
    private static final int PICK_IMAGE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        databaseHelper = new DatabaseHelper(getContext());
        
        // Get student ID if editing
        Bundle args = getArguments();
        if (args != null) {
            studentIdToEdit = args.getString("studentId");
        }
        
        initializeViews(view);
        setupDepartmentSpinner();
        setupDatePicker();
        setupPhotoPicker();
        setupButtons();
        
        if (studentIdToEdit != null) {
            loadStudentData();
        }
    }

    private void initializeViews(View view) {
        editStudentId = view.findViewById(R.id.editStudentId);
        editStudentName = view.findViewById(R.id.editStudentName);
        editEnrollmentDate = view.findViewById(R.id.editEnrollmentDate);
        editAge = view.findViewById(R.id.editAge);
        editEmail = view.findViewById(R.id.editEmail);
        editPhone = view.findViewById(R.id.editPhone);
        spinnerDepartment = view.findViewById(R.id.spinnerDepartment);
        radioGroupStatus = view.findViewById(R.id.radioGroupStatus);
        radioActive = view.findViewById(R.id.radioActive);
        radioInactive = view.findViewById(R.id.radioInactive);
        radioGraduated = view.findViewById(R.id.radioGraduated);
        checkBoxCurrentlyActive = view.findViewById(R.id.checkBoxCurrentlyActive);
        imgStudentPhoto = view.findViewById(R.id.imgStudentPhoto);
        btnSelectPhoto = view.findViewById(R.id.btnSelectPhoto);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
        
        if (studentIdToEdit != null) {
            editStudentId.setEnabled(false); // Cannot edit ID
        }
    }

    private void setupDepartmentSpinner() {
        String[] departments = {"Computer Science", "Engineering", "Business", "Arts", "Science", "Medicine"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), 
                android.R.layout.simple_spinner_item, departments);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(adapter);
    }

    private void setupDatePicker() {
        editEnrollmentDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view, year, month, dayOfMonth) -> {
                        String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        editEnrollmentDate.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private void setupPhotoPicker() {
        btnSelectPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                selectedPhoto = BitmapFactory.decodeStream(inputStream);
                imgStudentPhoto.setImageBitmap(selectedPhoto);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupButtons() {
        btnSave.setOnClickListener(v -> saveStudent());
        btnCancel.setOnClickListener(v -> {
            if (getActivity() instanceof Activity3) {
                ((Activity3) getActivity()).loadFragment(new StudentListFragment());
            }
        });
    }

    private void loadStudentData() {
        Student student = databaseHelper.getStudentById(studentIdToEdit);
        if (student != null) {
            editStudentId.setText(student.getStudentId());
            editStudentName.setText(student.getStudentName());
            editEnrollmentDate.setText(student.getEnrollmentDate());
            editAge.setText(String.valueOf(student.getAge()));
            editEmail.setText(student.getEmail());
            editPhone.setText(student.getPhone());
            
            // Set department spinner
            ArrayAdapter adapter = (ArrayAdapter) spinnerDepartment.getAdapter();
            int position = adapter.getPosition(student.getDepartment());
            if (position >= 0) {
                spinnerDepartment.setSelection(position);
            }
            
            // Set status radio button
            if (student.getStatus() != null) {
                switch (student.getStatus()) {
                    case "Active":
                        radioActive.setChecked(true);
                        break;
                    case "Inactive":
                        radioInactive.setChecked(true);
                        break;
                    case "Graduated":
                        radioGraduated.setChecked(true);
                        break;
                }
            }
            
            // Set photo
            if (student.getPhoto() != null) {
                selectedPhoto = student.getPhoto();
                imgStudentPhoto.setImageBitmap(selectedPhoto);
            }
        }
    }

    private void saveStudent() {
        String studentId = editStudentId.getText().toString().trim();
        String studentName = editStudentName.getText().toString().trim();
        String enrollmentDate = editEnrollmentDate.getText().toString().trim();
        String ageStr = editAge.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String department = spinnerDepartment.getSelectedItem().toString();
        
        // Validate required fields
        if (studentId.isEmpty() || studentName.isEmpty() || enrollmentDate.isEmpty() || 
            ageStr.isEmpty() || department.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid age", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Get selected status
        String status = null;
        int selectedRadioId = radioGroupStatus.getCheckedRadioButtonId();
        if (selectedRadioId == R.id.radioActive) {
            status = "Active";
        } else if (selectedRadioId == R.id.radioInactive) {
            status = "Inactive";
        } else if (selectedRadioId == R.id.radioGraduated) {
            status = "Graduated";
        }
        
        Student student = new Student();
        student.setStudentId(studentId);
        student.setStudentName(studentName);
        student.setEnrollmentDate(enrollmentDate);
        student.setAge(age);
        student.setEmail(email);
        student.setPhone(phone);
        student.setDepartment(department);
        student.setStatus(status);
        student.setPhoto(selectedPhoto);
        
        long result;
        if (studentIdToEdit != null) {
            result = databaseHelper.updateStudent(student);
            Toast.makeText(getContext(), "Student updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            result = databaseHelper.insertStudent(student);
            Toast.makeText(getContext(), "Student added successfully", Toast.LENGTH_SHORT).show();
        }
        
        if (result > 0) {
            if (getActivity() instanceof Activity3) {
                ((Activity3) getActivity()).loadFragment(new StudentListFragment());
            }
        } else {
            Toast.makeText(getContext(), "Error saving student", Toast.LENGTH_SHORT).show();
        }
    }
}
