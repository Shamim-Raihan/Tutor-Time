package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Courses;
import com.atia.tutortime.model.Teacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyEnrolledCourseDetailsActivity extends AppCompatActivity {

    private String getCourseId, getTeacherId;
    private DatabaseReference courseRef, userRef;
    private ArrayList<Courses> coursesList;
    private ArrayList<Teacher> teacherList;
    private TextView courseNameTv, courseTeacherTv, courseTotalSeatTv, courseAvailableSeatTv, courseStartTimeTv, courseEndTimeTv, courseClassTv, courseMediaTv, courseFeeTv, addressTv;
    private Courses courses;
    private Teacher teacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_enrolled_course_details);


        courseNameTv = findViewById(R.id.my_course_name_id);
        courseTeacherTv = findViewById(R.id.my_course_teacher_name_id);
        courseTotalSeatTv = findViewById(R.id.my_total_seat_id);
        courseAvailableSeatTv = findViewById(R.id.my_available_seat_id);
        courseStartTimeTv = findViewById(R.id.my_start_time_id);
        courseEndTimeTv = findViewById(R.id.my_end_time_id);
        courseClassTv = findViewById(R.id.my_class_id);
        courseMediaTv = findViewById(R.id.my_media_id);
        courseFeeTv = findViewById(R.id.my_course_fee_id);
        addressTv = findViewById(R.id.my_course_address_id);

        courseRef = FirebaseDatabase.getInstance().getReference("course");
        userRef = FirebaseDatabase.getInstance().getReference("users");
        coursesList = new ArrayList<>();
        teacherList = new ArrayList<>();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            getCourseId = bundle.getString("courseId");
            getTeacherId = bundle.getString("teacherId");
        }

        courseRef.orderByChild("cId").equalTo(getCourseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    courses = dataSnapshot.getValue(Courses.class);

                }

                userRef.orderByChild("uid").equalTo(getTeacherId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            teacher = dataSnapshot.getValue(Teacher.class);
                        }

                        courseNameTv.setText(courses.getCourseName());
                        courseTeacherTv.setText(teacher.getName());
                        courseTotalSeatTv.setText(courses.getTotalSeat());
                        courseAvailableSeatTv.setText(courses.getAvailableSeat());
                        courseStartTimeTv.setText(courses.getStartDate());
                        courseEndTimeTv.setText(courses.getEndDate());
                        courseClassTv.setText(courses.getcClass());
                        courseMediaTv.setText(courses.getMedia());
                        courseFeeTv.setText(courses.getCourseFee());
                        addressTv.setText(courses.getPlatformAddress());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}