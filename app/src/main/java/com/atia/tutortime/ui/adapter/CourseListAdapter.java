package com.atia.tutortime.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Courses;
import com.atia.tutortime.ui.activity.EditCourseActivity;
import com.atia.tutortime.ui.activity.TeacherCourseListDetailsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.myViewHolder> {

    int pos;

    private ArrayList<Courses> courseList;
    private Context context;

    public CourseListAdapter(ArrayList<Courses> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.course_list_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        pos = holder.getAdapterPosition();
        Courses courses = courseList.get(position);
        holder.courseName.setText(courses.getCourseName());
        holder.courseClass.setText(courses.getcClass());
        holder.title.setText(String.valueOf(courses.getCourseName().charAt(0)) + String.valueOf(courses.getCourseName().charAt(1)));


    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView courseName, courseClass, title;
        ImageView edit, delete;
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("course");




        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.course_name_id);
            courseClass = itemView.findViewById(R.id.class_id);
            edit = itemView.findViewById(R.id.edit_id);
//            delete = itemView.findViewById(R.id.delete_id);
            title = itemView.findViewById(R.id.course_titile_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Courses courses = courseList.get(pos);
                    Intent intent = new Intent(context, TeacherCourseListDetailsActivity.class);
                    intent.putExtra("courseDetails", courses);
                    context.startActivity(intent);
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Courses courses = courseList.get(pos);
                    Intent intent = new Intent(context, EditCourseActivity.class);
                    intent.putExtra("courseName", courses.getCourseName());
                    intent.putExtra("startTime", courses.getStartDate());
                    intent.putExtra("endTime", courses.getEndDate());
                    intent.putExtra("address", courses.getPlatformAddress());
                    intent.putExtra("seat", courses.getTotalSeat());
                    intent.putExtra("media", courses.getMedia());
                    intent.putExtra("class", courses.getcClass());
                    intent.putExtra("courseId", courses.getcId());
                    intent.putExtra("courseFee", courses.getCourseFee());
                    intent.putExtra("availableSeat", courses.getAvailableSeat());


                    context.startActivity(intent);
                }
            });

//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Course Delete!!!");
//                    builder.setMessage("Are you sure?");
//                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Courses courses = courseList.get(pos);
//                            Query deleteQuery = courseRef.orderByChild("cId").equalTo(courses.getcId());
//                            courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                                        dataSnapshot.getRef().removeValue();
//                                    }
//                                }
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                }
//                            });
//                        }
//                    });
//                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                        }
//                    });
//                    builder.show();
//                }
//            });

        }
    }
}




















