package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.atia.tutortime.R;
import com.atia.tutortime.ui.fragment.EnrollRequestFragment;
import com.atia.tutortime.ui.fragment.MyCoursesFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class StudentCourseListActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_list);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);


        ViewPagertAdapter viewPagertAdapter = new ViewPagertAdapter(getSupportFragmentManager());
        viewPagertAdapter.addFragment(new MyCoursesFragment(), "My Courses");
        viewPagertAdapter.addFragment(new EnrollRequestFragment(), "Request courses");

        viewPager.setAdapter(viewPagertAdapter);
        tabLayout.setupWithViewPager(viewPager);

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