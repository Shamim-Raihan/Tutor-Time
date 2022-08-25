package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Student;
import com.atia.tutortime.model.Teacher;
import com.atia.tutortime.ui.fragment.OfflineCoursesFragment;
import com.atia.tutortime.ui.fragment.TeacherListFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String status = "";
    private Teacher teacher;
    private Student student;

    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar myToolbar;
    View view;

    ImageView profileImage;
    TextView username;
    TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationId);
        drawerLayout = findViewById(R.id.drawerLayoutId);
        navigationView = findViewById(R.id.navigationViewId);
        myToolbar = findViewById(R.id.toolbarId);
        setSupportActionBar(myToolbar);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        bottomNavigationView.setSelectedItemId(R.id.home);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutId, new OfflineCoursesFragment()).commit();
        }

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        view = navigationView.getHeaderView(0);
        profileImage = view.findViewById(R.id.header_profileImage_id);
        username = view.findViewById(R.id.header_username_id);
        email = view.findViewById(R.id.header_email_id);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.equals("teacher")){
                    Intent intent = new Intent(HomeActivity.this, TeacherProfileActivity.class);
                    intent.putExtra("teacherId", mAuth.getUid());
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(HomeActivity.this, StudentProfileActivity.class);
                    startActivity(intent);
                }
            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("teacher")){

                    Toast.makeText(HomeActivity.this, ""+teacher.getName(), Toast.LENGTH_SHORT).show();
                }
                else if(status.equals("student")){
                    Toast.makeText(HomeActivity.this, ""+student.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                if (item.getItemId() == R.id.offline_id) {
                    fragment = new OfflineCoursesFragment();
                }

                if (item.getItemId() == R.id.teacher_list_id) {
                    fragment = new TeacherListFragment();
                }

                assert fragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutId, fragment).commit();
                return true;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                Intent intent1 = null;
                Intent intent2 = null;

                switch (item.getItemId()){
                    case R.id.my_profile_id:
                        if (status.equals("student")){
                            intent = new Intent(HomeActivity.this, StudentProfileActivity.class);
                            startActivity(intent);
                        }
                        else {
                            intent = new Intent(HomeActivity.this, TeacherProfileActivity.class);
                            intent.putExtra("teacherId", mAuth.getUid());
                            intent.putExtra("from", "home");
                            startActivity(intent);
                        }

                        break;
                    case R.id.courses_id:
                        if (status.equals("student")){
                            intent1 = new Intent(HomeActivity.this, StudentCourseListActivity.class);
                            startActivity(intent1);
                        }
                        else {
                            intent1 = new Intent(HomeActivity.this, TeacherCourseListActivity.class);
                            startActivity(intent1);
                        }
                        break;
                    case R.id.log_out_id:
                        mAuth.signOut();
                        intent2 = new Intent(HomeActivity.this, SignInActivity.class);
                        startActivity(intent2);
                        finish();
                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                    username.setText(teacher.getName());
                    email.setText(teacher.getEmail());
                }
                if (status.equals("student")) {
                    student = snapshot.child(mAuth.getUid()).getValue(Student.class);
                    Picasso.get()
                            .load(student.getProfilePhoto())
                            .into(profileImage);
                    username.setText(student.getName());
                    email.setText(student.getEmail());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}