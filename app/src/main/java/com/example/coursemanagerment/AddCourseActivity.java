package com.example.coursemanagerment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.coursemanagerment.DAO.CourseDAO;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class AddCourseActivity extends AppCompatActivity {

    private TextInputEditText courseNameEdt, coursePriceEdt, courseSuitedForEdt, courseImgEdt, courseLinkEdt, courseDescEdt;
    private Button addCourseBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String courseID;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        courseNameEdt = findViewById(R.id.idEdtCourseName);
        coursePriceEdt = findViewById(R.id.idEdtCoursePrice);
        courseSuitedForEdt = findViewById(R.id.idEdtCourseSuitedFor);
        courseImgEdt = findViewById(R.id.idEdtCourseImageLink);
        courseLinkEdt = findViewById(R.id.idEdtCourseLink);
        courseDescEdt = findViewById(R.id.idEdtCourseDesc);
        addCourseBtn = findViewById(R.id.idBtnAddCourse);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = firebaseDatabase.getReference("Courses");


        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                String userID = currentUser.getUid();
                String courseName = courseNameEdt.getText().toString();
                String coursePrice = coursePriceEdt.getText().toString();
                String semester = courseSuitedForEdt.getText().toString();
                String courseImg = courseImgEdt.getText().toString();
                String courseLink = courseLinkEdt.getText().toString();
                String courseDesc = courseDescEdt.getText().toString();


                CourseRVModal courseRVModal = new CourseRVModal(userID, courseName, courseDesc, coursePrice, semester, courseImg, courseLink, courseID);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        CourseDAO.getInstance().create(courseRVModal);


                        Toast.makeText(AddCourseActivity.this, " Course added..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddCourseActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddCourseActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public static String UniqueString() {
        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm";
        Random RANDOM = new Random();
        int len = 30;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();
    }

}