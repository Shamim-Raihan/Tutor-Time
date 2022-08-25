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

import com.atia.tutortime.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TeacherEditProfileActivity extends AppCompatActivity {

    private String getName, getPhone, getDegree, getEducation, getJobInstitution;
    private EditText teacherNameEd, teacherPhoneEd, teacherDegreeEd, teacherEducationEd, teacherJobEd;
    private Button savebutton;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private AlertDialog alertDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_edit_profile);


        teacherNameEd = findViewById(R.id.teacher_edit_name_id);
        teacherPhoneEd = findViewById(R.id.teacher_edit_phone_id);
        teacherDegreeEd = findViewById(R.id.teacher_edit_degree_id);
        teacherEducationEd = findViewById(R.id.teacher_edit_education_id);
        teacherJobEd = findViewById(R.id.teacher_edit_job_id);
        savebutton = findViewById(R.id.teacher_register_edit_button_id);

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
            getDegree = bundle.getString("degree");
            getEducation = bundle.getString("education");
            getJobInstitution = bundle.getString("jonInstitution");
        }

        teacherNameEd.setText(getName);
        teacherPhoneEd.setText(getPhone);
        teacherDegreeEd.setText(getDegree);
        teacherEducationEd.setText(getEducation);
        teacherJobEd.setText(getJobInstitution);

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TeacherEditProfileActivity.this);
                builder.setCancelable(false);
                builder.setMessage("Are Your sure?");
                builder.setTitle("Edit Your Profile");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        alertDialog.show();

                        String name = teacherNameEd.getText().toString();
                        String phone = teacherPhoneEd.getText().toString();
                        String degree = teacherDegreeEd.getText().toString();
                        String education = teacherEducationEd.getText().toString();
                        String jobInstitution = teacherJobEd.getText().toString();

                        HashMap updateTeacherProfile = new HashMap();
                        updateTeacherProfile.put("name", name);
                        updateTeacherProfile.put("phone", phone);
                        updateTeacherProfile.put("degree", degree);
                        updateTeacherProfile.put("educationArea", education);
                        updateTeacherProfile.put("jonInstitution", jobInstitution);

                        userRef.child(mAuth.getUid()).updateChildren(updateTeacherProfile).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                alertDialog.dismiss();
                                Intent intent = new Intent(TeacherEditProfileActivity.this, TeacherProfileActivity.class);
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