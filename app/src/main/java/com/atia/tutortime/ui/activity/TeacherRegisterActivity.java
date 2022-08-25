package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atia.tutortime.R;
import com.atia.tutortime.model.NetworkChangeListener;
import com.atia.tutortime.model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class TeacherRegisterActivity extends AppCompatActivity {

    private EditText teacherName, teacherEmail, teacherPhone, teacherDegree, teacherEducation, teacherJob, teacherPassword, teacherConfirmPassword;
    private RadioGroup teacherRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private TextView teacherPhotoTextView;
    private ImageView teacherAddPhotoImageView;
    private Button teacherRegisterButton;
    private TextView teacherNavigateToSignInTextView, passwordCheckAlert;

    private static final int REQUEST_CAMERA_CODE = 100;
    private String gender = "null";
    private Uri imageUri;
    private AlertDialog alertDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String uid;
    private StorageTask storageTask;
    private String status = "teacher";
    FirebaseUser user;
    private String imageUrl;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        teacherName = findViewById(R.id.teacher_name_id);
        teacherEmail = findViewById(R.id.teacher_email_id);
        teacherPhone = findViewById(R.id.teacher_phone_id);
        teacherDegree = findViewById(R.id.teacher_degree_id);
        teacherEducation = findViewById(R.id.teacher_education_id);
        teacherJob = findViewById(R.id.teacher_job_id);
        teacherPassword = findViewById(R.id.teacher_password_id);
        teacherConfirmPassword = findViewById(R.id.teacher_confirm_password_id);
        teacherRadioGroup = findViewById(R.id.teacher_radio_group_id);
        maleRadioButton = findViewById(R.id.teacher_male_radio_button_id);
        femaleRadioButton = findViewById(R.id.teacher_female_radio_button_id);
        teacherPhotoTextView = findViewById(R.id.teacher_photo_tv_id);
        teacherAddPhotoImageView = findViewById(R.id.teacher_add_photo_img_id);
        passwordCheckAlert = findViewById(R.id.teacher_passwordChecker_id);
        teacherRegisterButton = findViewById(R.id.teacher_register_button_id);
        teacherNavigateToSignInTextView = findViewById(R.id.teacher_navigate_to_sign_in_button_id);

        mAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog_box, null));
        builder.setCancelable(false);
        alertDialog = builder.create();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference("image");

        if (ContextCompat.checkSelfPermission(TeacherRegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(TeacherRegisterActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        teacherRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.teacher_male_radio_button_id:
                        gender = "male";
                        break;
                    case R.id.teacher_female_radio_button_id:
                        gender = "female";
                        break;
                }
            }
        });

        teacherAddPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(TeacherRegisterActivity.this);
            }
        });


        teacherRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = teacherName.getText().toString();
                String email = teacherEmail.getText().toString();
                String phone = teacherPhone.getText().toString();
                String degree = teacherDegree.getText().toString();
                String educationArea = teacherEducation.getText().toString();
                String jobInstitution = teacherJob.getText().toString();
                String password = teacherPassword.getText().toString();
                String confirmPassword = teacherConfirmPassword.getText().toString();

                if (name.isEmpty()){
                    teacherName.setError("Name is required");
                    teacherName.requestFocus();
                    return;
                }
                if (email.isEmpty()){
                    teacherEmail.setError("Email is required");
                    teacherEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    teacherEmail.setError("Invalid email address");
                    teacherEmail.requestFocus();
                    return;
                }
                if (phone.isEmpty()){
                    teacherPhone.setError("Phone is required");
                    teacherPhone.requestFocus();
                    return;
                }
                if (degree.isEmpty()){
                    teacherDegree.setError("Degree is required");
                    teacherDegree.requestFocus();
                    return;
                }
                if (educationArea.isEmpty()){
                    teacherEducation.setError("Education area is required");
                    teacherEducation.requestFocus();
                    return;
                }
                if (jobInstitution.isEmpty()){
                    teacherJob.setError("Job institutuion is required");
                    teacherJob.requestFocus();
                    return;
                }
                if (gender.equals("null")){
                    Toast.makeText(TeacherRegisterActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageUri == null){
                    Toast.makeText(TeacherRegisterActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()){
                    teacherPassword.setError("Password is required");
                    teacherPassword.requestFocus();
                    return;
                }
                if (password.length() < 6){
                    teacherPassword.setError("Password length should be 6 character");
                    teacherPassword.requestFocus();
                    return;
                }
                if (confirmPassword.isEmpty()){
                    teacherConfirmPassword.setError("Confirm password is required");
                    teacherConfirmPassword.requestFocus();
                    return;
                }
                if(!password.equals(confirmPassword)){
                    passwordCheckAlert.setVisibility(View.VISIBLE);
                    return;
                }
                teacherRegistration(name, email, phone, degree, educationArea, jobInstitution, password);
            }
        });

        teacherNavigateToSignInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(TeacherRegisterActivity.this, SignInActivity.class);
                startActivity(intent);

            }
        });
    }

    private void teacherRegistration(String name, String email, String phone, String degree, String educationArea, String jobInstitution, String password) {
        alertDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    uid = mAuth.getUid();
                    user = mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(TeacherRegisterActivity.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
                                if (storageTask != null && storageTask.isInProgress()) {
                                    Toast.makeText(TeacherRegisterActivity.this, "Uploading is progress", Toast.LENGTH_SHORT).show();
                                } else {
                                    saveData(name, email, phone, degree, educationArea, jobInstitution, password);
                                }
                            }
                            else {
                                Toast.makeText(TeacherRegisterActivity.this, "Verification Email is not sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveData(String name, String email, String phone, String degree, String educationArea, String jobInstitution, String password) {
        final StorageReference ref = storageReference.child("image" + imageUri.getLastPathSegment() + "." + getFileExtension(imageUri));
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl = String.valueOf(uri);
                        Teacher teacher = new Teacher(name, email, phone, gender, imageUrl, password, status, uid, degree, jobInstitution, educationArea);
                        databaseReference.child(uid).setValue(teacher);
                        mAuth.signOut();
                        alertDialog.dismiss();
                        Intent intent = new Intent(TeacherRegisterActivity.this, EmailVerificationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TeacherRegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                imageUri = result.getUri();
                teacherPhotoTextView.setText("Image " + imageUri.getLastPathSegment());
            }
        }
    }

    @Override
    protected void onStart() {

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}















