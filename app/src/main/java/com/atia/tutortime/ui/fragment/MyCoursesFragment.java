package com.atia.tutortime.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Enrollment;
import com.atia.tutortime.ui.activity.StudentCourseListActivity;
import com.atia.tutortime.ui.adapter.StudentCourseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyCoursesFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference enrollmentRef;
    private FirebaseAuth mAuth;
    private ArrayList<Enrollment> enrolledCourses;
    private StudentCourseListAdapter studentCourseListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_courses, container, false);


        recyclerView = view.findViewById(R.id.student_course_list_recycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        enrolledCourses  = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        enrollmentRef = FirebaseDatabase.getInstance().getReference("enrollment");

        enrollmentRef.orderByChild("approveStatus").equalTo("approved").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                    enrolledCourses.add(enrollment);
                }
                studentCourseListAdapter = new StudentCourseListAdapter(enrolledCourses, getContext());
                recyclerView.setAdapter(studentCourseListAdapter);
                studentCourseListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}