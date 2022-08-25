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
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atia.tutortime.R;
import com.atia.tutortime.model.NetworkChangeListener;
import com.atia.tutortime.model.Student;
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

public class StudentRegisterActivity extends AppCompatActivity {

    private EditText studentName, studentEmail, studentPassword, studentConfirmPassword, studentPhone;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private NumberPicker classNumberPicker, ageNumberPicker;
    private Button registerButton;
    private TextView profileTextView, navigateToSignIn, passwordCheckAlert;
    private ImageView addPhotoImageView;

    private static final int REQUEST_CAMERA_CODE = 100;
    private String gender = "null";
    private Uri imageUri;
    private int classNumber, age;

    private AlertDialog alertDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String uid;
    private StorageTask storageTask;
    private String status = "student";
    FirebaseUser user;
    private String imageUrl;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        studentName = findViewById(R.id.student_name_ed_id);
        studentEmail = findViewById(R.id.student_email_ed_id);
        studentPassword = findViewById(R.id.student_password_ed_id);
        studentConfirmPassword = findViewById(R.id.student_confirm_password_ed_id);
        genderRadioGroup = findViewById(R.id.student_radio_group_id);
        maleRadioButton = findViewById(R.id.student_male_radio_btn_id);
        femaleRadioButton = findViewById(R.id.student_female_radio_btn_id);
        classNumberPicker  = findViewById(R.id.student_class_picker_id);
        ageNumberPicker  = findViewById(R.id.student_age_picker_id);
        passwordCheckAlert = findViewById(R.id.student_passwordChecker_id);
        registerButton  = findViewById(R.id.student_register_btn_id);
        profileTextView  = findViewById(R.id.student_profile_picture_tv_id);
        navigateToSignIn  = findViewById(R.id.student_navigate_to_sogn_in_tv_id);
        addPhotoImageView  = findViewById(R.id.student_add_photo_img_id);
        studentPhone = findViewById(R.id.student_phone_ed_id);

        mAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog_box, null));
        builder.setCancelable(false);
        alertDialog = builder.create();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference("image");




        if (ContextCompat.checkSelfPermission(StudentRegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(StudentRegisterActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.student_male_radio_btn_id:
                        gender = "male";
                        break;
                    case R.id.student_female_radio_btn_id:
                        gender = "female";
                        break;
                }
            }
        });

        addPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(StudentRegisterActivity.this);
            }
        });

        if(classNumberPicker != null){
            classNumberPicker.setMinValue(5);
            classNumberPicker.setMaxValue(12);
            classNumberPicker.setWrapSelectorWheel(true);
            classNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    classNumber = newVal;
                }
            });
        }

        if(ageNumberPicker != null){
            ageNumberPicker.setMinValue(5);
            ageNumberPicker.setMaxValue(25);
            ageNumberPicker.setWrapSelectorWheel(true);
            ageNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    age = newVal;
                }
            });
        }


        navigateToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(StudentRegisterActivity.this, SignInActivity.class);
                startActivity(intent);

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = studentName.getText().toString();
                String email = studentEmail.getText().toString();
                String phone = studentPhone.getText().toString();
                String password = studentPassword.getText().toString();
                String confirmPassword = studentConfirmPassword.getText().toString();


                if (name.isEmpty()){
                    studentName.setError("Name is required");
                    studentName.requestFocus();
                    return;
                }
                if (email.isEmpty()){
                    studentEmail.setError("Email is required");
                    studentEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    studentEmail.setError("Invalid email address");
                    studentEmail.requestFocus();
                    return;
                }
                if (phone.isEmpty()){
                    studentPhone.setError("Phone is required");
                    studentPhone.requestFocus();
                    return;
                }
                if (gender.equals("null")){
                    Toast.makeText(StudentRegisterActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageUri == null){
                    Toast.makeText(StudentRegisterActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()){
                    studentPassword.setError("Password is required");
                    studentPassword.requestFocus();
                    return;
                }
                if (password.length() < 6){
                    studentPassword.setError("Password length should be 6 character");
                    studentPassword.requestFocus();
                    return;
                }
                if (confirmPassword.isEmpty()){
                    studentConfirmPassword.setError("Confirm password is required");
                    studentConfirmPassword.requestFocus();
                    return;
                }
                if(!password.equals(confirmPassword)){
                    passwordCheckAlert.setVisibility(View.VISIBLE);
                    return;
                }
                StudentRegistration(name, email, phone, password);

            }
        });
    }

    private void StudentRegistration(String name, String email, String phone, String password) {
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
                                Toast.makeText(StudentRegisterActivity.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
                                if (storageTask != null && storageTask.isInProgress()) {
                                    Toast.makeText(StudentRegisterActivity.this, "Uploading is progress", Toast.LENGTH_SHORT).show();
                                } else {
                                    saveData(name, email, phone, password);
                                }
                            }
                            else {
                                Toast.makeText(StudentRegisterActivity.this, "Verification Email is not sent", Toast.LENGTH_SHORT).show();
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

    private void saveData(String name, String email, String phone, String password) {
        final StorageReference ref = storageReference.child("image" + imageUri.getLastPathSegment() + "." + getFileExtension(imageUri));
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl = String.valueOf(uri);
                        Student student = new Student(name, email, phone, gender, imageUrl, password, status, uid, String.valueOf(classNumber), String.valueOf(age));
                        databaseReference.child(uid).setValue(student);
                        mAuth.signOut();
                        alertDialog.dismiss();
                        Intent intent = new Intent(StudentRegisterActivity.this, EmailVerificationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentRegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
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
                profileTextView.setText("Image " + imageUri.getLastPathSegment());
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











