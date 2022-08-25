package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atia.tutortime.R;
import com.atia.tutortime.model.Courses;
import com.atia.tutortime.ui.adapter.CourseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EditCourseActivity extends AppCompatActivity {

    private EditText editCourseNameEd, editStartTimeEd, editEndTimeEd, editPlatformLinkEd, editSeatNumberEd, editCourseFeeEd;
    private RadioGroup editRadioGroup;
    private RadioButton editOnlineRB, editOfflineRB;
    private NumberPicker editClassNumberPicker;
    private Button editCourseButton;
    private ImageView startTimePicker, endTimePicker;


    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    private String getCourseName, getStartTime, getEndTime, getPlatformAddress, getSeatNumber, getMedia, getClassNumber, getCourseId, getCourseFee, getAvailableSeat;


    private String newCourseName, newStartTime, newEndTime, newPlatformAddress, newSeatNumber, newMedia, newCourseFee;
    private int newClassNumber;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog_box, null));
        builder.setCancelable(false);
        alertDialog = builder.create();


        editCourseNameEd = findViewById(R.id.edit_course_name_id);
        editStartTimeEd = findViewById(R.id.edit_course_start_time_id);
        startTimePicker = findViewById(R.id.edit_course_star_time_picker_id);
        editEndTimeEd = findViewById(R.id.edit_course_end_time_id);
        endTimePicker = findViewById(R.id.edit_course_end_time_picker_id);
        editPlatformLinkEd = findViewById(R.id.edit_course_platform_link_id);

        editSeatNumberEd = findViewById(R.id.edit_course_seat_id);
        editRadioGroup = findViewById(R.id.edit_course_radio_group_id);
        editOnlineRB = findViewById(R.id.edit_course_oneline_id);
        editOfflineRB = findViewById(R.id.edit_course_offline_id);
        editClassNumberPicker = findViewById(R.id.edit_course_class_picker_id);
        editCourseFeeEd = findViewById(R.id.edit_course_fee_id);


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("course");

        editCourseButton = findViewById(R.id.edit_course_button_id);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            getCourseName = bundle.getString("courseName");
            getStartTime = bundle.getString("startTime");
            getEndTime = bundle.getString("endTime");
            getPlatformAddress = bundle.getString("address");
            getSeatNumber = bundle.getString("seat");
            getMedia = bundle.getString("media");
            getClassNumber = bundle.getString("class");
            getCourseId = bundle.getString("courseId");
            getCourseFee = bundle.getString("courseFee");
            getAvailableSeat = bundle.getString("availableSeat");
        }

        newCourseName = getCourseName;
        newStartTime = getStartTime;
        newEndTime = getEndTime;
        newPlatformAddress = getPlatformAddress;
        newSeatNumber = getSeatNumber;
        newMedia = getMedia;
        newClassNumber = Integer.parseInt(getClassNumber);
        newCourseFee = getCourseFee;


        editCourseNameEd.setText(getCourseName);
        editStartTimeEd.setText(getStartTime);
        editEndTimeEd.setText(getEndTime);
        editPlatformLinkEd.setText(getPlatformAddress);
        editCourseFeeEd.setText(getCourseFee);
        editSeatNumberEd.setText(getSeatNumber);

        if (getMedia.equals("online")) {
            editOnlineRB.setChecked(true);
        } else if (getMedia.equals("offline")) {
            editOfflineRB.setChecked(true);
        }

        if (editClassNumberPicker != null) {
            editClassNumberPicker.setMinValue(5);
            editClassNumberPicker.setMaxValue(12);
            editClassNumberPicker.setWrapSelectorWheel(true);
            editClassNumberPicker.setValue(Integer.parseInt(getClassNumber));
            editClassNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    newClassNumber = newVal;
                }
            });
        }

        editRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.edit_course_oneline_id:
                        newMedia = "online";
                        break;
                    case R.id.edit_course_offline_id:
                        newMedia = "offline";
                        break;
                }
            }
        });

        editCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditCourseActivity.this);
                builder.setMessage("Are you sure?");
                builder.setTitle("Add new course");

                builder.setCancelable(false);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.show();
                        newCourseName = editCourseNameEd.getText().toString();
                        newStartTime = editStartTimeEd.getText().toString();
                        newEndTime = editEndTimeEd.getText().toString();
                        newPlatformAddress = editPlatformLinkEd.getText().toString();
                        newSeatNumber = editSeatNumberEd.getText().toString();
                        newCourseFee = editCourseFeeEd.getText().toString();

                        int seatDifference = Integer.parseInt(newSeatNumber) - Integer.parseInt(getSeatNumber);
                        int updateAvailableSeat = seatDifference + Integer.parseInt(getAvailableSeat);

                        HashMap updateCourse = new HashMap();
                        updateCourse.put("courseName", newCourseName);
                        updateCourse.put("startDate", newStartTime);
                        updateCourse.put("endDate", newEndTime);
                        updateCourse.put("platformAddress", newPlatformAddress);
                        updateCourse.put("totalSeat", newSeatNumber);
                        updateCourse.put("media", newMedia);
                        updateCourse.put("courseFee", newCourseFee);
                        updateCourse.put("cClass", String.valueOf(newClassNumber));
                        updateCourse.put("availableSeat", String.valueOf(updateAvailableSeat));

                        databaseReference.child(getCourseId).updateChildren(updateCourse).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    alertDialog.dismiss();
                                    Toast.makeText(EditCourseActivity.this, "Course info updated", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EditCourseActivity.this, TeacherCourseListActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }
}
















