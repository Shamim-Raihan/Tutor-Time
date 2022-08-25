package com.atia.tutortime.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.lang.UProperty;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Teacher;
import com.atia.tutortime.ui.activity.TeacherProfileActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.myViewHolder> implements Filterable {

    Context context;
    private ArrayList<Teacher> teacherList;
    private ArrayList<Teacher> teacherListAll;

    public TeacherListAdapter(Context context, ArrayList<Teacher> teacherList) {
        this.context = context;
        this.teacherList = teacherList;
        this.teacherListAll = new ArrayList<>(teacherList);
    }

    @NonNull
    @Override
    public TeacherListAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.teacher_list_item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherListAdapter.myViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);
        holder.teacherName.setText(teacher.getName());
        holder.teacherEducation.setText(teacher.getEducationArea());
        Picasso.get()
                .load(teacher.getProfilePhoto())
                .into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<Teacher> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filteredList.addAll(teacherListAll);
            }
            else {
                for (Teacher list : teacherListAll){
                    if (list.getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(list);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            teacherList.clear();
            teacherList.addAll((Collection<? extends Teacher>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView teacherName, teacherEducation;
        CircleImageView profileImage;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacher_list_teacher_name_id);
            teacherEducation = itemView.findViewById(R.id.teacher_list_teacher_education_id);
            profileImage = itemView.findViewById(R.id.circle_title_id);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Teacher teacher = teacherList.get(pos);
                    Intent intent = new Intent(context, TeacherProfileActivity.class);
                    intent.putExtra("teacherId", teacher.getUid());
                    intent.putExtra("from", "adapter");
                    context.startActivity(intent);
                }
            });
        }
    }
}
