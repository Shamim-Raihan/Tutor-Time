package com.atia.tutortime.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Teacher;
import com.atia.tutortime.ui.adapter.TeacherListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private TeacherListAdapter teacherListAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private ArrayList<Teacher> teacherList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_list, container, false);

        recyclerView = view.findViewById(R.id.RecyclerViewID);
        searchView = view.findViewById(R.id.teacher_search_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        teacherList = new ArrayList<>();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                teacherListAdapter.getFilter().filter(s);
                return false;
            }
        });



        userRef.orderByChild("status").equalTo("teacher").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Teacher teacher = dataSnapshot.getValue(Teacher.class);
                    teacherList.add(teacher);
                }

                teacherListAdapter = new TeacherListAdapter(getContext(), teacherList);
                recyclerView.setAdapter(teacherListAdapter);
                teacherListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}