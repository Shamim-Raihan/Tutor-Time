package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Student;
import com.atia.tutortime.model.Teacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfileActivity extends AppCompatActivity {


    Toolbar myToolbar;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String status;
    Student student;
    private CircleImageView profileImage;
    private TextView profileName, profileEmail, profilePhone, profileGender, profileAge, profileClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);


        myToolbar = findViewById(R.id.toolbarId);
        profileImage = findViewById(R.id.studentprofile_profileImage_id);
        profileName = findViewById(R.id.studentprofile_name_id);
        profileClass = findViewById(R.id.studentprofile_class_id);
        profileEmail = findViewById(R.id.studentprofile_email_id);
        profilePhone = findViewById(R.id.studentprofile_phone_id);
        profileAge = findViewById(R.id.studentprofile_age_id);
        profileGender = findViewById(R.id.studentprofile_gender_id);

        setSupportActionBar(myToolbar);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_profile_menu_items, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.student_profile_edit_id:
                Intent intent = new Intent(StudentProfileActivity.this, StudentEditProfileActivity.class);
                intent.putExtra("name", student.getName());
                intent.putExtra("phone", student.getPhone());
                intent.putExtra("class", student.getsClass());
                intent.putExtra("age", student.getsAge());
                startActivity(intent);
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
                if (status.equals("student")) {
                    student = snapshot.child(mAuth.getUid()).getValue(Student.class);
                    Picasso.get()
                            .load(student.getProfilePhoto())
                            .into(profileImage);
                    profileName.setText(student.getName());
                    profileClass.setText(String.valueOf(student.getsClass()));
                    profileEmail.setText(student.getEmail());
                    profilePhone.setText(student.getPhone());
                    profileAge.setText(String.valueOf(student.getsAge()));
                    profileGender.setText(student.getGender());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}