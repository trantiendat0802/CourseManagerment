package com.example.coursemanagerment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursemanagerment.DAO.CourseDAO;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CourseRVAdapter.CourseClickInterface {

    private RecyclerView courseRV;
    private ProgressBar loadingPB;
    private FloatingActionButton addFAB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<CourseRVModal> courseRVModalArrayList;
    private RelativeLayout bottomSheetRL;
    private CourseRVAdapter courseRVAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        courseRV = findViewById(R.id.idRVCourses);
        loadingPB = findViewById(R.id.idPBLoading);
        addFAB = findViewById(R.id.idAddFAB);
        bottomSheetRL = findViewById(R.id.LBSheet);
        firebaseDatabase = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = firebaseDatabase.getReference("Courses");


        mAuth = FirebaseAuth.getInstance();
        courseRVModalArrayList = new ArrayList<>();
        courseRVAdapter = new CourseRVAdapter(courseRVModalArrayList, this, this);

        courseRV.setLayoutManager(new LinearLayoutManager(this));
        courseRV.setAdapter(courseRVAdapter);

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddCourseActivity.class));
            }
        });
        loadCourses();
    }

    private void loadCourses() {
        courseRVModalArrayList.clear();
        //courseRVModalArrayList = CourseDAO.getInstance().getAllByUserID(mAuth.getUid());

        Query query = databaseReference.orderByChild("userID").equalTo(currentUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadingPB.setVisibility(View.GONE);
//                courseRVModalArrayList.add(snapshot.getValue(CourseRVModal.class));
                for (DataSnapshot ds : snapshot.getChildren()) {
                    courseRVModalArrayList.add(new CourseRVModal(
                            ds.child("userID").getValue(String.class),
                            ds.child("courseName").getValue(String.class),
                            ds.child("courseDescription").getValue(String.class),
                            ds.child("CourseSlot").getValue(String.class),
                            ds.child("semester").getValue(String.class),
                            ds.child("courseImg").getValue(String.class),
                            ds.child("courseLink").getValue(String.class),
                            ds.child("courseID").getValue(String.class)
                    ));
                }
                courseRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "cancelled..", Toast.LENGTH_SHORT).show();
            }

        });


    }

    @Override
    public void onCourseClick(int position) {
        displayBottomSheet(courseRVModalArrayList.get(position));
    }

    private void displayBottomSheet(CourseRVModal courseRVModal) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_diaglog, bottomSheetRL);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView courseNameTV = layout.findViewById(R.id.idTVCourseName);
        TextView courseDescTV = layout.findViewById(R.id.idTVDescription);
        TextView courseSuitedForTV = layout.findViewById(R.id.idTVSuitedFor);
        TextView coursePriceTV = layout.findViewById(R.id.idTVPrice);
        ImageView courseIV = layout.findViewById(R.id.idIVCourse);
        Button editBtn = layout.findViewById(R.id.idBtnEdit);
        Button viewDetailsBtn = layout.findViewById(R.id.idBtnViewDetails);

        courseNameTV.setText(courseRVModal.getCourseName());
        courseDescTV.setText(courseRVModal.getCourseDescription());
        courseSuitedForTV.setText(courseRVModal.getSemester());
        coursePriceTV.setText("number of slot: " + courseRVModal.getCourseSlot());
        Picasso.get().load(courseRVModal.getCourseImg()).into(courseIV);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EditCourseActivity.class);
                i.putExtra("course", courseRVModal);
                startActivity(i);
//                finish();
            }
        });

        viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(courseRVModal.getCourseLink()));
                startActivity(i);
//                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:
                Toast.makeText(this, "User Logout...", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //return super.onOptionsItemSelected(item);
    }
}