package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Courses;
import com.atia.tutortime.ui.adapter.CourseListAdapter;
import com.atia.tutortime.ui.fragment.EnrollRequestFragment;
import com.atia.tutortime.ui.fragment.MyCoursesFragment;
import com.atia.tutortime.ui.fragment.TeacherCourseListFragment;
import com.atia.tutortime.ui.fragment.TeacherRequestListFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherCourseListActivity extends AppCompatActivity {

    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_list);
        myToolbar = findViewById(R.id.toolbarId);

        setSupportActionBar(myToolbar);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);


        TeacherCourseListActivity.ViewPagertAdapter viewPagertAdapter = new TeacherCourseListActivity.ViewPagertAdapter(getSupportFragmentManager());
        viewPagertAdapter.addFragment(new TeacherCourseListFragment(), "My Courses");
        viewPagertAdapter.addFragment(new TeacherRequestListFragment(), "Request List");

        viewPager.setAdapter(viewPagertAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_course_menu_item, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_new_course_id:
                Intent intent = new Intent(TeacherCourseListActivity.this, AddNewCourseActivity.class);
                startActivity(intent);
        }
        return true;
    }


    class ViewPagertAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagertAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }


        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}