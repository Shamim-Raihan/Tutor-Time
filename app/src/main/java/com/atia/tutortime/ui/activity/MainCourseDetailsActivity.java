package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Enrollment;
import com.atia.tutortime.model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainCourseDetailsActivity extends AppCompatActivity {

    private String availabeSeat, courseClass, courseName, endTime, media, startTime, totalSeat, teacherId, courseId, requestList, courseFee;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    private String teacherName;
    private Button enrollNowButton;

    private TextView courseNameTv, courseTeacherTv, courseTotalSeatTv, courseAvailableSeatTv, courseStartTimeTv, courseEndTimeTv, courseClassTv, courseMediaTv;
    private DatabaseReference enrolledCourseRef;
    private DatabaseReference courseRef;
    private DatabaseReference enrollmentRef;
    private String approveStatus = "pending";
    private ArrayList<Teacher> courseTeacher;
    private AlertDialog alertDialog;
    private TextView courseFeeTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_course_details);

        courseNameTv = findViewById(R.id.main_course_name_id);
        courseTeacherTv = findViewById(R.id.main_course_teacher_name_id);
        courseTotalSeatTv = findViewById(R.id.main_total_seat_id);
        courseAvailableSeatTv = findViewById(R.id.main_available_seat_id);
        courseStartTimeTv = findViewById(R.id.main_start_time_id);
        courseEndTimeTv = findViewById(R.id.main_end_time_id);
        courseClassTv = findViewById(R.id.main_class_id);
        courseMediaTv = findViewById(R.id.main_media_id);
        enrollNowButton = findViewById(R.id.main_enroll_now_id);
        courseFeeTv = findViewById(R.id.main_course_fee_id);

        userDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        enrolledCourseRef = FirebaseDatabase.getInstance().getReference("enrolled_courses");
        courseRef = FirebaseDatabase.getInstance().getReference("course");
        enrollmentRef = FirebaseDatabase.getInstance().getReference("enrollment");

        mAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog_box, null));
        builder.setCancelable(false);
        alertDialog = builder.create();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            availabeSeat = bundle.getString("availableSeat");
            courseClass = bundle.getString("class");
            courseName = bundle.getString("courseName");
            endTime = bundle.getString("endTime");
            media = bundle.getString("media");
            startTime = bundle.getString("startTime");
            totalSeat = bundle.getString("totalSeat");
            teacherId = bundle.getString("teacherId");
            courseId = bundle.getString("courseId");
            requestList = bundle.getString("requestList");
            courseFee = bundle.getString("courseFee");
        }

        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherName = snapshot.child(teacherId).child("name").getValue(String.class);
                courseTeacherTv.setText(teacherName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        courseNameTv.setText(courseName);
        courseTotalSeatTv.setText(totalSeat);
        courseAvailableSeatTv.setText(availabeSeat);
        courseStartTimeTv.setText(startTime);
        courseEndTimeTv.setText(endTime);
        courseClassTv.setText(courseClass);
        courseMediaTv.setText(media);
        courseFeeTv.setText(courseFee);

        enrollNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainCourseDetailsActivity.this);
                builder.setMessage("Are you sure?");
                builder.setTitle("Alert !!!");
                builder.setCancelable(false);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.show();
                        enrollmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String id = enrollmentRef.push().getKey();
                                Enrollment enrollment = new Enrollment(id, courseId, mAuth.getUid(), teacherId, approveStatus);
                                enrollmentRef.child(id).setValue(enrollment);
                                Toast.makeText(MainCourseDetailsActivity.this, "enroll course successfull", Toast.LENGTH_SHORT).show();

                                HashMap updateStudentList = new HashMap();
                                updateStudentList.put("studentList", mAuth.getUid() + ",");
                                courseRef.child(courseId).updateChildren(updateStudentList).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        alertDialog.dismiss();
                                        final Dialog dialog = new Dialog(MainCourseDetailsActivity.this);
                                        dialog.setContentView(R.layout.enrollment_message);
                                        final Button okButton;
                                        okButton = dialog.findViewById(R.id.enrollment_message_ok_button_id);

                                        okButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(MainCourseDetailsActivity.this, StudentCourseListActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        dialog.show();

                                        Toast.makeText(MainCourseDetailsActivity.this, "Student list updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

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

    @Override
    protected void onStart() {
        super.onStart();
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String studentList = snapshot.child(courseId).child("studentList").getValue(String.class);
                String[] studentListArray = studentList.split(",", 0);
                for(int i = 0; i < studentListArray.length; i++){
                    if(studentListArray[i].equals(mAuth.getUid())){
                        enrollNowButton.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.child(mAuth.getUid()).child("status").getValue(String.class);
                if(status.equals("teacher")){
                    enrollNowButton.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}