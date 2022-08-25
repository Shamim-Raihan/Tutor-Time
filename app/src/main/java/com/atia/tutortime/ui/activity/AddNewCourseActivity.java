package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Courses;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;

public class AddNewCourseActivity extends AppCompatActivity {


    private EditText courseNameEd, startTimeEd, endTimeEd, platformLinkEd, seatNumberEd, courseFeeEd;
    private RadioGroup mediaRadioGroup;
    private RadioButton onlineRB, offlineRB;
    private NumberPicker classPicker;
    private Button addCourseButton;
    private ImageView startTimePicker, endTimePicker;
    private TextView platformLinkTv, locationAddressTv;
    private int starthour, startmin, endHour, endmin;
    private String media = "null";
    private int classNumber;

    private DatabaseReference databaseReference;
    String avialableSeat = "";
    String studentList = "";
    String requeatList = "";
    String finishedStatus = "On Going";

    FirebaseAuth mAuth;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog_box, null));
        builder.setCancelable(false);
        alertDialog = builder.create();



        courseNameEd = findViewById(R.id.add_course_name_id);
        startTimeEd = findViewById(R.id.add_course_start_time_id);
        startTimePicker = findViewById(R.id.add_course_star_time_picker_id);
        endTimeEd = findViewById(R.id.add_course_end_time_id);
        endTimePicker = findViewById(R.id.add_course_end_time_picker_id);
        platformLinkEd = findViewById(R.id.add_course_platform_link_id);
        seatNumberEd = findViewById(R.id.add_course_seat_id);
        mediaRadioGroup = findViewById(R.id.add_course_radio_group_id);
        onlineRB = findViewById(R.id.add_course_oneline_id);
        offlineRB = findViewById(R.id.add_course_offline_id);
        classPicker = findViewById(R.id.add_course_class_picker_id);
        addCourseButton = findViewById(R.id.add_course_button_id);
        platformLinkTv = findViewById(R.id.add_course_platform_link_tv_id);
        locationAddressTv = findViewById(R.id.add_course_loacation_address_tv_id);
        courseFeeEd = findViewById(R.id.add_course_fee_id);


        databaseReference = FirebaseDatabase.getInstance().getReference("course");
        mAuth = FirebaseAuth.getInstance();

        startTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNewCourseActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedmin) {
                        starthour = selectedHour;
                        startmin = selectedmin;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, starthour, startmin);
                        startTimeEd.setText(DateFormat.format("hh:mm:aa", calendar));
                    }
                }, 12, 0, false
                );

                timePickerDialog.updateTime(starthour, startmin);
                timePickerDialog.setTitle("Select start time");
                timePickerDialog.show();

            }
        });

        endTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNewCourseActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedmin) {
                        endHour = selectedHour;
                        endmin = selectedmin;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, endHour, endmin);
                        endTimeEd.setText(DateFormat.format("hh:mm:aa", calendar));
                    }
                }, 12, 0, false
                );

                timePickerDialog.updateTime(endHour, endmin);
                timePickerDialog.setTitle("Select end time");
                timePickerDialog.show();

            }
        });

        mediaRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.add_course_oneline_id:
                        media = "online";
                        platformLinkTv.setVisibility(View.VISIBLE);
                        platformLinkEd.setVisibility(View.VISIBLE);
                        locationAddressTv.setVisibility(View.GONE);
                        break;
                    case R.id.add_course_offline_id:
                        media = "offline";
                        locationAddressTv.setVisibility(View.VISIBLE);
                        platformLinkEd.setVisibility(View.VISIBLE);
                        platformLinkTv.setVisibility(View.GONE);
                        break;
                }
            }
        });


        if(classPicker != null){
            classPicker.setMinValue(5);
            classPicker.setMaxValue(12);
            classPicker.setWrapSelectorWheel(true);
            classPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    classNumber = newVal;
                }
            });
        }



        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewCourseActivity.this);
                builder.setMessage("Are you sure?");
                builder.setTitle("Add new course");

                builder.setCancelable(false);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        alertDialog.show();


                        String courseName = courseNameEd.getText().toString();
                        String startTime = startTimeEd.getText().toString();
                        String endTime = endTimeEd.getText().toString();
                        String platformLink = platformLinkEd.getText().toString();
                        String seatNumber = seatNumberEd.getText().toString();
                        String courseFee = courseFeeEd.getText().toString();

                        if(courseName.isEmpty()){
                            courseNameEd.setError("Enter course name");
                            courseNameEd.requestFocus();
                            return;
                        }
                        if(startTime.isEmpty()){
                            startTimeEd.setError("Enter course start time");
                            startTimeEd.requestFocus();
                            return;
                        }
                        if(endTime.isEmpty()){
                            endTimeEd.setError("Enter course end time");
                            endTimeEd.requestFocus();
                            return;
                        }
                        if(platformLink.equals("")){
                            platformLinkEd.setError("Enter platform link or address");
                            platformLinkEd.requestFocus();
                            return;
                        }
                        if(seatNumber.isEmpty()){
                            seatNumberEd.setError("Enter course seat number");
                            seatNumberEd.requestFocus();
                            return;
                        }
                        if(courseFee.isEmpty()){
                            courseFeeEd.setError("Enter course fee");
                            courseFeeEd.requestFocus();
                            return;
                        }

                        addnewCourse(courseName, startTime, endTime, platformLink, seatNumber, courseFee);
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

    private void addnewCourse(String courseName, String startTime, String endTime, String platformLink, String seatNumber, String courseFee) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id = databaseReference.push().getKey();
                avialableSeat = seatNumber;
                Courses courses = new Courses(courseName, String.valueOf(classNumber), startTime, endTime, media, platformLink, seatNumber, avialableSeat, studentList, requeatList, finishedStatus, id, mAuth.getUid(), courseFee);
                databaseReference.child(id).setValue(courses);
                alertDialog.dismiss();
                Toast.makeText(AddNewCourseActivity.this, "Course added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddNewCourseActivity.this, TeacherCourseListActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}