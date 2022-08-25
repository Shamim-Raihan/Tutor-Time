package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Courses;
import com.atia.tutortime.ui.adapter.MainCourseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EveryTeacherCourseListActivity extends AppCompatActivity {

    private String teacherId;
    private DatabaseReference courseRef;
    private ArrayList<Courses> courseList;
    private MainCourseListAdapter mainCourseListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_teacher_course_list);

        recyclerView = findViewById(R.id.racycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(EveryTeacherCourseListActivity.this));

        courseRef = FirebaseDatabase.getInstance().getReference("course");
        courseList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            teacherId = bundle.getString("teacherId");
        }
        courseRef.orderByChild("teacherId").equalTo(teacherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Courses courses = dataSnapshot.getValue(Courses.class);
                    courseList.add(courses);
                }

                mainCourseListAdapter = new MainCourseListAdapter(courseList, EveryTeacherCourseListActivity.this);
                recyclerView.setAdapter(mainCourseListAdapter);
//                mainCourseListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}