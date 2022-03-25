package com.example.coursemanagerment.DAO;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.coursemanagerment.CourseRVModal;
import com.example.coursemanagerment.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseDAO {
    private DatabaseReference databaseReference;

    public CourseDAO() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Courses");
    }

    private static CourseDAO instance;

    static {
        try {
            instance = new CourseDAO();
        } catch (Exception e) {
            throw new RuntimeException("Exception occured in creating singleton instance");
        }
    }

    public static CourseDAO getInstance() {
        return instance;
    }

    public void create(CourseRVModal courseRVModal) {
        courseRVModal.setCourseID(databaseReference.push().getKey());
        databaseReference.child(courseRVModal.getCourseID()).setValue(courseRVModal);
    }

    public void update(CourseRVModal courseRVModal) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String key = courseRVModal.getCourseID();
        hashMap.put(key, courseRVModal);
        databaseReference.updateChildren(hashMap);
    }

    public void deleteByID(String courseID) {
        databaseReference.child(courseID).removeValue();
    }

    public ArrayList<CourseRVModal> getAllByUserID(String userID) {
        ArrayList<CourseRVModal> ar = new ArrayList<>();

        Query query = databaseReference.orderByChild("userID").equalTo(userID);
        query.get().onSuccessTask(snapshot -> {
            for (DataSnapshot ds : snapshot.getChildren()) {
                ar.add(new CourseRVModal(
                        ds.child("userID").getValue(String.class),
                        ds.child("courseName").getValue(String.class),
                        ds.child("courseDescription").getValue(String.class),
                        ds.child("slot").getValue(String.class),
                        ds.child("semester").getValue(String.class),
                        ds.child("courseImg").getValue(String.class),
                        ds.child("courseLink").getValue(String.class),
                        ds.child("courseID").getValue(String.class)
                ));
            }

            return null;
        });
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    ar.add(new CourseRVModal(
//                            ds.child("userID").getValue(String.class),
//                            ds.child("courseName").getValue(String.class),
//                            ds.child("courseDescription").getValue(String.class),
//                            ds.child("slot").getValue(String.class),
//                            ds.child("semester").getValue(String.class),
//                            ds.child("courseImg").getValue(String.class),
//                            ds.child("courseLink").getValue(String.class),
//                            ds.child("courseID").getValue(String.class)
//                    ));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//
//        });

        return ar;
    }
}

