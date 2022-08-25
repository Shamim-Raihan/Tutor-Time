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
import com.atia.tutortime.ui.activity.RequestCourseDetailsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Reader;
import java.util.ArrayList;

public class RequestedCourseListAdapter extends RecyclerView.Adapter<RequestedCourseListAdapter.myViewHolder> {
    private ArrayList<Courses> studentCourseList;
    private Context context;
    private String status;

    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("course");

    private String teacherName, teacherContact;

    public RequestedCourseListAdapter(ArrayList<Courses> studentCourseList, Context context, String status) {
        this.studentCourseList = studentCourseList;
        this.context = context;
        this.status = status;
    }

    @NonNull
    @Override
    public RequestedCourseListAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.student_course_list_item, parent, false);
        return new RequestedCourseListAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Courses courses = studentCourseList.get(position);
        holder.courseNameTv.setText(courses.getCourseName());
        holder.courseClassTv.setText(courses.getcClass());
        holder.title.setText(String.valueOf(courses.getCourseName().charAt(0)) + String.valueOf(courses.getCourseName().charAt(1)));


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
                    Courses courses = studentCourseList.get(pos);


                    String teacherId = courses.getTeacherId();
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            teacherName = snapshot.child(teacherId).child("name").getValue(String.class);
                            teacherContact = snapshot.child(teacherId).child("phone").getValue(String.class);

                            Intent intent = new Intent(context, RequestCourseDetailsActivity.class);
                            intent.putExtra("availableSeat", courses.getAvailableSeat());
                            intent.putExtra("class", courses.getcClass());
                            intent.putExtra("courseName", courses.getCourseName());
                            intent.putExtra("endTime", courses.getEndDate());
                            intent.putExtra("media", courses.getMedia());
                            intent.putExtra("startTime", courses.getStartDate());
                            intent.putExtra("totalSeat", courses.getTotalSeat());
                            intent.putExtra("teacherName", teacherName);
                            intent.putExtra("teacherContact", teacherContact);
                            intent.putExtra("status", status);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            });

        }
    }
}
