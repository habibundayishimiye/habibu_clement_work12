package com.example.habibu_clement_work12;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class StudentFormFragment extends Fragment {
    private EditText editStudentId, editStudentName, editAge, editEmail, editPhone;
    private Spinner spinnerDepartment;
    private RadioGroup radioGroupStatus;
    private CheckBox checkBoxActive;
    private ImageView imageViewPhoto;
    private Button btnSelectDate, btnSave, btnSelectPhoto;
    private DatabaseHelper dbHelper;
    private String enrollmentDate = "";
    private Bitmap selectedPhoto;
    private Student studentToEdit;
    private boolean isEditMode = false;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());

        // Get student ID from arguments if editing
        Bundle args = getArguments();
        if (args != null && args.containsKey("studentId")) {
            String studentId = args.getString("studentId");
            studentToEdit = dbHelper.getStudent(studentId);
            isEditMode = studentToEdit != null;
        }

        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                        try {
                            selectedPhoto = MediaStore.Images.Media.getBitmap(
                                requireContext().getContentResolver(),
                                result.getData().getData()
                            );
                            imageViewPhoto.setImageBitmap(selectedPhoto);
                            imageViewPhoto.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_form, container, false);

        initializeViews(view);
        setupSpinners();
        setupDatePicker();
        setupPhotoPicker();
        setupSaveButton();

        if (isEditMode && studentToEdit != null) {
            populateForm(studentToEdit);
        }

        return view;
    }

    private void initializeViews(View view) {
        editStudentId = view.findViewById(R.id.editStudentId);
        editStudentName = view.findViewById(R.id.editStudentName);
        editAge = view.findViewById(R.id.editAge);
        editEmail = view.findViewById(R.id.editEmail);
        editPhone = view.findViewById(R.id.editPhone);
        spinnerDepartment = view.findViewById(R.id.spinnerDepartment);
        radioGroupStatus = view.findViewById(R.id.radioGroupStatus);
        checkBoxActive = view.findViewById(R.id.checkBoxActive);
        imageViewPhoto = view.findViewById(R.id.imageViewPhoto);
        btnSelectDate = view.findViewById(R.id.btnSelectDate);
        btnSave = view.findViewById(R.id.btnSave);
        btnSelectPhoto = view.findViewById(R.id.btnSelectPhoto);

        if (isEditMode) {
            editStudentId.setEnabled(false); // Don't allow editing ID
        }
    }

    private void setupSpinners() {
        String[] departments = getResources().getStringArray(R.array.departments_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            departments
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(adapter);
    }

    private void setupDatePicker() {
        btnSelectDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    enrollmentDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    btnSelectDate.setText(enrollmentDate);
                },
                year, month, day
            );
            datePickerDialog.show();
        });
    }

    private void setupPhotoPicker() {
        btnSelectPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });
    }

    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> saveStudent());
    }

    private void populateForm(Student student) {
        editStudentId.setText(student.getStudentId());
        editStudentName.setText(student.getStudentName());
        editAge.setText(String.valueOf(student.getAge()));
        editEmail.setText(student.getEmail());
        editPhone.setText(student.getPhone());
        enrollmentDate = student.getEnrollmentDate();
        btnSelectDate.setText(enrollmentDate);

        // Set department spinner
        String[] departments = getResources().getStringArray(R.array.departments_array);
        for (int i = 0; i < departments.length; i++) {
            if (departments[i].equals(student.getDepartment())) {
                spinnerDepartment.setSelection(i);
                break;
            }
        }

        // Set status radio button
        String status = student.getStatus();
        if (status != null) {
            if (status.equals(getString(R.string.status_active))) {
                radioGroupStatus.check(R.id.radioActive);
            } else if (status.equals(getString(R.string.status_inactive))) {
                radioGroupStatus.check(R.id.radioInactive);
            } else if (status.equals(getString(R.string.status_graduated))) {
                radioGroupStatus.check(R.id.radioGraduated);
            }
        }

        // Set photo if available
        if (student.getPhoto() != null) {
            selectedPhoto = student.getPhoto();
            imageViewPhoto.setImageBitmap(selectedPhoto);
            imageViewPhoto.setVisibility(View.VISIBLE);
        }
    }

    private void saveStudent() {
        String studentId = editStudentId.getText().toString().trim();
        String studentName = editStudentName.getText().toString().trim();
        String ageStr = editAge.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        // Validation
        if (studentId.isEmpty()) {
            editStudentId.setError(getString(R.string.required_field));
            return;
        }
        if (studentName.isEmpty()) {
            editStudentName.setError(getString(R.string.required_field));
            return;
        }
        if (ageStr.isEmpty()) {
            editAge.setError(getString(R.string.required_field));
            return;
        }
        if (enrollmentDate.isEmpty()) {
            Toast.makeText(getContext(), "Please select enrollment date", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            editAge.setError("Please enter a valid age");
            return;
        }

        String department = spinnerDepartment.getSelectedItem().toString();
        String status = "";
        int selectedRadioId = radioGroupStatus.getCheckedRadioButtonId();
        if (selectedRadioId == R.id.radioActive) {
            status = getString(R.string.status_active);
        } else if (selectedRadioId == R.id.radioInactive) {
            status = getString(R.string.status_inactive);
        } else if (selectedRadioId == R.id.radioGraduated) {
            status = getString(R.string.status_graduated);
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
        if (isEditMode) {
            result = dbHelper.updateStudent(student);
        } else {
            result = dbHelper.insertStudent(student);
        }

        if (result > 0) {
            Toast.makeText(getContext(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();
            // Go back to list
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        } else {
            Toast.makeText(getContext(), "Error saving student", Toast.LENGTH_SHORT).show();
        }
    }
}

