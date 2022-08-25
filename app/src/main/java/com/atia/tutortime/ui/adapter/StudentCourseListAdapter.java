package com.atia.tutortime.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Courses;
import com.atia.tutortime.model.Enrollment;
import com.atia.tutortime.ui.activity.MainCourseDetailsActivity;
import com.atia.tutortime.ui.activity.MyEnrolledCourseDetailsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StudentCourseListAdapter extends RecyclerView.Adapter<StudentCourseListAdapter.myViewHolder> {

    private ArrayList<Enrollment> studentCourseList;
    private Context context;

    private String courseId, teacherId, id;
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("course");

//    String courseName, courseClass, courseTeacherName;
    int pos;
    String courseTeacherName;

    public StudentCourseListAdapter(ArrayList<Enrollment> studentCourseList, Context context) {
        this.studentCourseList = studentCourseList;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentCourseListAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.student_course_list_item, parent, false);
        return new myViewHolder(view);
    }

//    void getList(){
//        for(int i = 0; i < studentCourseList.size(); i++){
//            Enrollment enrollment = studentCourseList.get(i);
//            courseId = enrollment.getCourseId();
//
//        }
//    }



    @Override
    public void onBindViewHolder(@NonNull StudentCourseListAdapter.myViewHolder holder, int position) {
        Enrollment enrollment = studentCourseList.get(position);
        courseId = enrollment.getCourseId();
        teacherId = enrollment.getTeacherId();
        id = enrollment.getId();



        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String courseName = snapshot.child(courseId).child("courseName").getValue(String.class);
                String courseClass = snapshot.child(courseId).child("cClass").getValue(String.class);
                holder.courseNameTv.setText(courseName);
                holder.courseClassTv.setText(courseClass);
                holder.title.setText(String.valueOf(courseName.charAt(0)) + String.valueOf(courseName.charAt(1)));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseTeacherName = snapshot.child(teacherId).child("name").getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



    }

    @Override
    public int getItemCount() {
        return studentCourseList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView courseNameTv, title;
        TextView courseClassTv;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            courseNameTv = itemView.findViewById(R.id.student_course_name_id);
            courseClassTv = itemView.findViewById(R.id.student_class_id);
            title = itemView.findViewById(R.id.course_titile_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(context, MyEnrolledCourseDetailsActivity.class);
                    intent.putExtra("courseId", courseId);
                    intent.putExtra("teacherId", teacherId);
                    context.startActivity(intent);
                }
            });

        }
    }


}
