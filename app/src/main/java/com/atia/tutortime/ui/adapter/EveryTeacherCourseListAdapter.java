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
import com.atia.tutortime.ui.activity.MainCourseDetailsActivity;

import java.util.ArrayList;

public class EveryTeacherCourseListAdapter extends RecyclerView.Adapter<EveryTeacherCourseListAdapter.myViewholder> {

    private ArrayList<Courses> courseList;
    private Context context;

    public EveryTeacherCourseListAdapter(ArrayList<Courses> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.main_course_list_items, parent, false);
        return new EveryTeacherCourseListAdapter.myViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewholder holder, int position) {
        Courses courses = courseList.get(position);
        holder.courseName.setText(courses.getCourseName());
        holder.courseClass.setText(courses.getcClass());
        String availabeSeat = courses.getAvailableSeat();
        if(Integer.parseInt(availabeSeat) > 0){
            holder.courseAvailableSeat.setText("Available");
        }
        else {
            holder.courseAvailableSeat.setText("Not available");
        }

        holder.title.setText(String.valueOf(courses.getCourseName().charAt(0)) + String.valueOf(courses.getCourseName().charAt(1)));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class myViewholder extends RecyclerView.ViewHolder {
        TextView courseName, courseClass, courseAvailableSeat, title;
        public myViewholder(@NonNull View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.main_course_name_id);
            courseClass = itemView.findViewById(R.id.main_class_id);
            courseAvailableSeat = itemView.findViewById(R.id.main_available_seat_id);
            title = itemView.findViewById(R.id.main_course_title_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Courses courses = courseList.get(pos);
                    Intent intent = new Intent(context, MainCourseDetailsActivity.class);
                    intent.putExtra("availableSeat", courses.getAvailableSeat());
                    intent.putExtra("class", courses.getcClass());
                    intent.putExtra("courseName", courses.getCourseName());
                    intent.putExtra("endTime", courses.getEndDate());
                    intent.putExtra("media", courses.getMedia());
                    intent.putExtra("startTime", courses.getStartDate());
                    intent.putExtra("totalSeat", courses.getTotalSeat());
                    intent.putExtra("teacherId", courses.getTeacherId());
                    intent.putExtra("courseId", courses.getcId());
                    intent.putExtra("requestList", courses.getRequestList());
                    context.startActivity(intent);
                }
            });
        }
    }
}
