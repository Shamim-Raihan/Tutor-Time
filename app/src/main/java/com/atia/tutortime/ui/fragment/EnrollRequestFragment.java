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
import com.atia.tutortime.model.Courses;
import com.atia.tutortime.model.Enrollment;
import com.atia.tutortime.ui.adapter.RequestedCourseListAdapter;
import com.atia.tutortime.ui.adapter.StudentCourseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class EnrollRequestFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference enrollmentRef, courseRef;
    private FirebaseAuth mAuth;
    private ArrayList<Enrollment> enrolledCourses;
    private ArrayList<Courses> enrolledCoursesDetails;

    private RequestedCourseListAdapter requestedCourseListAdapter;

    String courseId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enroll_request, container, false);

        recyclerView = view.findViewById(R.id.student_enroll_request_list_recycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        enrolledCourses  = new ArrayList<>();
        enrolledCoursesDetails  = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        enrollmentRef = FirebaseDatabase.getInstance().getReference("enrollment");
        courseRef = FirebaseDatabase.getInstance().getReference("course");

        enrollmentRef.orderByChild("approveStatus").equalTo("pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                    enrolledCourses.add(enrollment);

                }
                for(int i = 0; i < enrolledCourses.size(); i++){
                    Enrollment enrollment = enrolledCourses.get(i);
                    courseId = enrollment.getCourseId();

                    courseRef.orderByChild("cId").equalTo(courseId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Courses courses = dataSnapshot.getValue(Courses.class);
                                enrolledCoursesDetails.add(courses);

                            }
                            requestedCourseListAdapter = new RequestedCourseListAdapter(enrolledCoursesDetails, getContext(), "pending");
                            recyclerView.setAdapter(requestedCourseListAdapter);
                            requestedCourseListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}