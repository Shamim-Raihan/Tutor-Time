package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Courses;
import com.atia.tutortime.model.Student;
import com.atia.tutortime.model.Teacher;
import com.atia.tutortime.ui.adapter.MainCourseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherProfileActivity extends AppCompatActivity {
    Toolbar myToolbar;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String status;
    Teacher teacher;
    Student student;
    private CircleImageView profileImage;
    private TextView profileName,profileQualification, profileEmail, profilePhone, profileJobPlace, profileGender;
    private Button teacherProfileCourseButton;
    private DatabaseReference courseRef;
    private ArrayList<Courses> courseList;
    private MainCourseListAdapter mainCourseListAdapter;
    private String teacherId, from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        myToolbar = findViewById(R.id.toolbarId);
        profileImage = findViewById(R.id.teacherprofile_profileImage_id);
        profileName = findViewById(R.id.teacherprofile_name_id);
        profileQualification = findViewById(R.id.teacherprofile_qualification_id);
        profileEmail = findViewById(R.id.teacherprofile_email_id);
        profilePhone = findViewById(R.id.teacherprofile_phone_id);
        profileJobPlace = findViewById(R.id.teacherprofile_job_place_id);
        profileGender = findViewById(R.id.teacherprofile_gender_id);
        teacherProfileCourseButton = findViewById(R.id.teacher_profile_course_id);
        setSupportActionBar(myToolbar);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        courseRef = FirebaseDatabase.getInstance().getReference("course");
        courseList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            teacherId = bundle.getString("teacherId");
            from = bundle.getString("from");
        }



        if(from.equals("home")){
            teacherProfileCourseButton.setVisibility(View.GONE);
        }
        if(from.equals("adapter")){
            teacherProfileCourseButton.setVisibility(View.VISIBLE);

            databaseReference.orderByChild("uid").equalTo(teacherId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Teacher teacher = dataSnapshot.getValue(Teacher.class);
                        if(teacher.getUid().equals(mAuth.getUid())){
                            teacherProfileCourseButton.setVisibility(View.GONE);
                        }
                        Picasso.get()
                                .load(teacher.getProfilePhoto())
                                .into(profileImage);
                        profileName.setText(teacher.getName());
                        profileQualification.setText(teacher.getEducationArea());
                        profileEmail.setText(teacher.getEmail());
                        profilePhone.setText(teacher.getPhone());
                        profileJobPlace.setText(teacher.getJobInstitution());
                        profileGender.setText(teacher.getGender());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        teacherProfileCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherProfileActivity.this, EveryTeacherCourseListActivity.class);
                intent.putExtra("teacherId", teacherId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu_item, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.teacher_edit_profile_id:
                if(from.equals("adapter")){
                    Toast.makeText(TeacherProfileActivity.this, "No permission", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(TeacherProfileActivity.this, TeacherEditProfileActivity.class);
                    intent.putExtra("name", teacher.getName());
                    intent.putExtra("phone", teacher.getPhone());
                    intent.putExtra("degree", teacher.getDegree());
                    intent.putExtra("education", teacher.getEducationArea());
                    intent.putExtra("jonInstitution", teacher.getJobInstitution());
                    startActivity(intent);
                }

        }
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = mAuth.getCurrentUser();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                status = snapshot.child(mAuth.getUid()).child("status").getValue(String.class);
                if (status.equals("teacher")) {
                    teacher = snapshot.child(mAuth.getUid()).getValue(Teacher.class);
                    Picasso.get()
                            .load(teacher.getProfilePhoto())
                            .into(profileImage);
                    profileName.setText(teacher.getName());
                    profileQualification.setText(teacher.getEducationArea());
                    profileEmail.setText(teacher.getEmail());
                    profilePhone.setText(teacher.getPhone());
                    profileJobPlace.setText(teacher.getJobInstitution());
                    profileGender.setText(teacher.getGender());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}