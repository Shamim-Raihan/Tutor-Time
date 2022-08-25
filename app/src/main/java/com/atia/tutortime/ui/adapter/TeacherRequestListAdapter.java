package com.atia.tutortime.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Courses;
import com.atia.tutortime.model.Enrollment;
import com.atia.tutortime.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class TeacherRequestListAdapter extends RecyclerView.Adapter<TeacherRequestListAdapter.myViewHolder> {

    private ArrayList<Courses> requestedCoursesDetails;
    private ArrayList<Student> requestedStudentDetails;
    private Context context;
    private ArrayList<Enrollment> enrollmentCourses;


    public TeacherRequestListAdapter(ArrayList<Courses> requestedCoursesDetails, ArrayList<Student> requestedStudentDetails, Context context, ArrayList<Enrollment> enrollmentCourses) {
        this.requestedCoursesDetails = requestedCoursesDetails;
        this.requestedStudentDetails = requestedStudentDetails;
        this.context = context;
        this.enrollmentCourses = enrollmentCourses;
    }

    @NonNull
    @Override
    public TeacherRequestListAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.requested_course_list_item, parent, false);
        return new TeacherRequestListAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherRequestListAdapter.myViewHolder holder, int position) {
        Courses courses = requestedCoursesDetails.get(position);
        Student student = requestedStudentDetails.get(position);

        holder.studentNameTv.setText(student.getName());
        holder.courseNameTv.setText(courses.getCourseName());
        holder.classTv.setText(courses.getcClass());
    }

    @Override
    public int getItemCount() {
        return requestedCoursesDetails.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView studentNameTv, courseNameTv, classTv, approveTv;
        DatabaseReference enrollmentRef = FirebaseDatabase.getInstance().getReference("enrollment");
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("course");

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            studentNameTv = itemView.findViewById(R.id.requested_student_name_id);
            courseNameTv = itemView.findViewById(R.id.requested_course_name_id);
            classTv = itemView.findViewById(R.id.requested_class_id);
            approveTv = itemView.findViewById(R.id.requested_approve_id);

            approveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = getAdapterPosition();
                    Enrollment enrollment = enrollmentCourses.get(pos);
                    Courses courses = requestedCoursesDetails.get(pos);

                    int availableSeat = Integer.parseInt(courses.getAvailableSeat());
                    int updateSeat = availableSeat - 1;

                    HashMap updateAvailableSeat = new HashMap();
                    updateAvailableSeat.put("availableSeat", String.valueOf(updateSeat));

                    courseRef.child(courses.getcId()).updateChildren(updateAvailableSeat).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                        }
                    });

                    HashMap updateStatus = new HashMap();
                    updateStatus.put("approveStatus", "approved");

                    enrollmentRef.child(enrollment.getId()).updateChildren(updateStatus).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Toast.makeText(context, "status is updated", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
    }
}
