package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Courses;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherCourseListDetailsActivity extends AppCompatActivity {

    private Courses courses;

    private TextView courseNameTv, courseTeacherTv, courseTotalSeatTv, courseAvailableSeatTv, courseStartTimeTv, courseEndTimeTv, courseClassTv, courseMediaTv, teacherContactTv;
    private TextView platformLink;

    FirebaseAuth mAuth;
    DatabaseReference teacherRef;
    String teacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_list_details);


        courseNameTv = findViewById(R.id.booked_course_name_id);
        courseTeacherTv = findViewById(R.id.booked_course_teacher_name_id);
        courseTotalSeatTv = findViewById(R.id.booked_total_seat_id);
        courseAvailableSeatTv = findViewById(R.id.booked_available_seat_id);
        courseStartTimeTv = findViewById(R.id.booked_start_time_id);
        courseEndTimeTv = findViewById(R.id.booked_end_time_id);
        courseClassTv = findViewById(R.id.booked_class_id);
        courseMediaTv = findViewById(R.id.booked_media_id);
        teacherContactTv = findViewById(R.id.booked_teacher_contact_id);
        teacherContactTv = findViewById(R.id.booked_teacher_contact_id);
        platformLink = findViewById(R.id.booked_platform_link_id);

        mAuth = FirebaseAuth.getInstance();
        teacherRef = FirebaseDatabase.getInstance().getReference("users");



        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            courses = (Courses) bundle.getSerializable("courseDetails");
        }

        courseNameTv.setText(courses.getCourseName());
        courseTotalSeatTv.setText(courses.getTotalSeat());
        courseAvailableSeatTv.setText(courses.getAvailableSeat());
        courseStartTimeTv.setText(courses.getStartDate());
        courseEndTimeTv.setText(courses.getEndDate());
        courseClassTv.setText(courses.getcClass());
        courseMediaTv.setText(courses.getMedia());
        platformLink.setText(courses.getPlatformAddress());

        teacherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherName = snapshot.child(courses.getTeacherId()).child("name").getValue(String.class);
                courseTeacherTv.setText(teacherName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}