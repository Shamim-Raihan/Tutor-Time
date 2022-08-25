package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.atia.tutortime.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class StudentEditProfileActivity extends AppCompatActivity {

    private EditText studentNameEd, studentPhoneEd;
    private NumberPicker classNumberPicker, ageNumberPicker;
    private Button saveButton;

    private String getName, getPhone, getClass, getAge;
    int newClass, newAge;
    private AlertDialog alertDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_profile);


        studentNameEd = findViewById(R.id.student_edit_name_ed_id);
        studentPhoneEd = findViewById(R.id.student_edit_phone_ed_id);
        classNumberPicker  = findViewById(R.id.student_edit_class_picker_id);
        ageNumberPicker  = findViewById(R.id.student_edit_age_picker_id);
        saveButton = findViewById(R.id.student_register_edit_btn_id);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog_box, null));
        builder.setCancelable(false);
        alertDialog = builder.create();

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            getName = bundle.getString("name");
            getPhone = bundle.getString("phone");
            getClass = bundle.getString("class");
            getAge = bundle.getString("age");
        }

        studentNameEd.setText(getName);
        studentPhoneEd.setText(getPhone);

        if(classNumberPicker != null){
            classNumberPicker.setMinValue(5);
            classNumberPicker.setMaxValue(12);
            classNumberPicker.setWrapSelectorWheel(true);
            classNumberPicker.setValue(Integer.parseInt(getClass));
            classNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                    newClass = newVal;
                }
            });
        }

        if(ageNumberPicker != null){
            ageNumberPicker.setMinValue(8);
            ageNumberPicker.setMaxValue(22);
            ageNumberPicker.setWrapSelectorWheel(true);
            ageNumberPicker.setValue(Integer.parseInt(getAge));
            ageNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                    newAge = newVal;
                }
            });
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentEditProfileActivity.this);
                builder.setCancelable(false);
                builder.setMessage("Are Your sure?");
                builder.setTitle("Edit Your Profile");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.show();
                        String name = studentNameEd.getText().toString();
                        String phone = studentPhoneEd.getText().toString();

                        HashMap updateStudentProfile = new HashMap();
                        updateStudentProfile.put("name", name);
                        updateStudentProfile.put("phone", phone);
                        updateStudentProfile.put("sClass", String.valueOf(newClass));
                        updateStudentProfile.put("aAge", String.valueOf(newAge));

                        userRef.child(mAuth.getUid()).updateChildren(updateStudentProfile).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                alertDialog.dismiss();
                                Intent intent = new Intent(StudentEditProfileActivity.this, StudentProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
    }
}