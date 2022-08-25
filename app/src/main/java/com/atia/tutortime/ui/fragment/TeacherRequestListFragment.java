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
import com.atia.tutortime.model.Student;
import com.atia.tutortime.ui.adapter.RequestedCourseListAdapter;
import com.atia.tutortime.ui.adapter.TeacherRequestListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TeacherRequestListFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference enrollmentRef, courseRef, userRef;
    private FirebaseAuth mAuth;
    private ArrayList<Enrollment> requestedCourses;
    private ArrayList<Enrollment> requestedCoursesList;
    private ArrayList<Courses> requestedCoursesDetails;
    private ArrayList<Student> requestedStudentDetails;

    String courseId, studentId, teacherId;
    TeacherRequestListAdapter teacherRequestListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_request_list, container, false);

        recyclerView = view.findViewById(R.id.teacher_requested_course_list_recycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        requestedCourses = new ArrayList<>();
        requestedCoursesList = new ArrayList<>();
        requestedCoursesDetails = new ArrayList<>();
        requestedStudentDetails = new ArrayList<>();
        enrollmentRef = FirebaseDatabase.getInstance().getReference("enrollment");
        courseRef = FirebaseDatabase.getInstance().getReference("course");
        userRef = FirebaseDatabase.getInstance().getReference("users");

        enrollmentRef.orderByChild("approveStatus").equalTo("pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                    requestedCourses.add(enrollment);
                }
                for(int i = 0; i < requestedCourses.size(); i++){
                    Enrollment enrollment = requestedCourses.get(i);
                    courseId = enrollment.getCourseId();
                    studentId = enrollment.getStudentId();
                    teacherId = enrollment.getTeacherId();
                    if(teacherId.equals(mAuth.getUid())){
                        courseRef.orderByChild("cId").equalTo(courseId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Courses courses = dataSnapshot.getValue(Courses.class);
                                    requestedCoursesDetails.add(courses);
                                }
                                userRef.orderByChild("uid").equalTo(studentId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            Student student = dataSnapshot.getValue(Student.class);
                                            requestedStudentDetails.add(student);
                                        }
                                        teacherRequestListAdapter = new TeacherRequestListAdapter(requestedCoursesDetails, requestedStudentDetails, getContext(), requestedCourses);
                                        recyclerView.setAdapter(teacherRequestListAdapter);
                                        teacherRequestListAdapter.notifyDataSetChanged();
                                        Toast.makeText(getContext(), "size : "+requestedCoursesDetails.size(), Toast.LENGTH_SHORT).show();
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
                    else {
                        continue;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }
}