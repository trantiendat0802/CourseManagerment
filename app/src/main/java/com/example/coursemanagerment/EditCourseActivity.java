package com.example.coursemanagerment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditCourseActivity extends AppCompatActivity {

    private TextInputEditText courseNameEdt, coursePriceEdt, courseSuitedForEdt, courseImgEdt, courseLinkEdt, courseDescEdt;
    private Button updateCourseBtn, deleteCourseBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private CourseRVModal courseRVModal;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        firebaseDatabase = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        courseNameEdt = findViewById(R.id.idEdtCourseName);
        coursePriceEdt = findViewById(R.id.idEdtCoursePrice);
        courseSuitedForEdt = findViewById(R.id.idEdtCourseSuitedFor);
        courseImgEdt = findViewById(R.id.idEdtCourseImageLink);
        courseLinkEdt = findViewById(R.id.idEdtCourseLink);
        courseDescEdt = findViewById(R.id.idEdtCourseDesc);
        updateCourseBtn = findViewById(R.id.idBtnUpdateCourse);
        deleteCourseBtn = findViewById(R.id.idBtnDeleteCourse);
        loadingPB = findViewById(R.id.idPBLoading);
        courseRVModal = getIntent().getParcelableExtra("course");

        if (courseRVModal != null) {
            courseNameEdt.setText(courseRVModal.getCourseName());
            coursePriceEdt.setText(courseRVModal.getCourseSlot());
            courseSuitedForEdt.setText(courseRVModal.getSemester());
            courseImgEdt.setText(courseRVModal.getCourseImg());
            courseLinkEdt.setText(courseRVModal.getCourseLink());
            courseDescEdt.setText(courseRVModal.getCourseDescription());
        }


        databaseReference = firebaseDatabase.getReference("Courses");
        updateCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
//
                String courseName = courseNameEdt.getText().toString();
                String courseSlot = coursePriceEdt.getText().toString();
                String semester = courseSuitedForEdt.getText().toString();
                String courseImg = courseImgEdt.getText().toString();
                String courseLink = courseLinkEdt.getText().toString();
                String courseDesc = courseDescEdt.getText().toString();

                courseRVModal.setCourseName(courseName);
                courseRVModal.setCourseSlot(courseSlot);
                courseRVModal.setSemester(semester);
                courseRVModal.setCourseImg(courseImg);
                courseRVModal.setCourseLink(courseLink);
                courseRVModal.setCourseDescription(courseDesc);

//                Map<String, Object> map = new HashMap<>();
//                map.put("userID", currentUser.getUid());
//                map.put("courseName", courseName);
//                map.put("courseDescription", courseDesc);
//                map.put("coursePrice", coursePrice);
//                map.put("semester", suitedFor);
//                map.put("courseImg", courseImg);
//                map.put("courseLink", courseLink);
//                map.put("courseID", courseID);

                CourseDAO.getInstance().update(courseRVModal);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        startActivity(new Intent(EditCourseActivity.this, MainActivity.class));
                        Toast.makeText(EditCourseActivity.this, "Course Updated...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Query query = databaseReference.orderByChild("courseID").equalTo(courseID);

//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        loadingPB.setVisibility(View.GONE);
//                        //databaseReference.child(courseID).updateChildren(map);
//                        try {
//                            databaseReference.updateChildren(map);
//                            if (snapshot.hasChild(courseID)) {
//                                Toast.makeText(EditCourseActivity.this, "Course Updated...", Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(EditCourseActivity.this, "Course Updated fail...", Toast.LENGTH_SHORT).show();
//                            }
//                        }catch (Exception e){
//                            Toast.makeText(EditCourseActivity.this, "Invalid data...", Toast.LENGTH_SHORT).show();
//                        }
//
//                        startActivity(new Intent(EditCourseActivity.this, MainActivity.class));
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(EditCourseActivity.this, "Fail to update course...", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
            }
        });

        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCourse();
            }
        });

    }

    private void deleteCourse() {
//        databaseReference.child("userID").removeValue();
        CourseDAO.getInstance().deleteByID(courseRVModal.getCourseID());
        Toast.makeText(this, "Course deleted..", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditCourseActivity.this, MainActivity.class));
    }
}

