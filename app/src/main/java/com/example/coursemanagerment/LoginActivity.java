package com.example.coursemanagerment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText userNameEdt, pwdEdt;
    private Button loginBtn;
    private ProgressBar loadingPB;
    private TextView registerTV;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEdt = findViewById(R.id.idEdtUserName);
        pwdEdt = findViewById(R.id.idEdtPassword);
        loginBtn = findViewById(R.id.idBtnLogin);
        loadingPB = findViewById(R.id.idPBLoading);
        registerTV = findViewById(R.id.idTVRegister);
        mAuth = FirebaseAuth.getInstance();

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userNameEdt.getText().toString();
                String pwd = pwdEdt.getText().toString();

                if (TextUtils.isEmpty(username) && TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this, "required", Toast.LENGTH_SHORT);
                    return;
                } else {

                    mAuth.signInWithEmailAndPassword(username.toString(), pwd.toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                loadingPB.setVisibility(view.GONE);
                                                Toast.makeText(LoginActivity.this, "Login successful..", Toast.LENGTH_SHORT);

                                                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }else {
                                                loadingPB.setVisibility(View.GONE);
                                                Toast.makeText(LoginActivity.this, "Login fail..", Toast.LENGTH_SHORT);
                                            }
                                        }
                                    });
//                    mAuth.signInWithEmailAndPassword(username, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                loadingPB.setVisibility(view.GONE);
//                                Toast.makeText(LoginActivity.this, "Login successful..", Toast.LENGTH_SHORT);
//
//                                Intent i = new Intent(LoginActivity.this,MainActivity.class);
//                                startActivity(i);
//                                finish();
//                            }else {
//                                loadingPB.setVisibility(View.GONE);
//                                Toast.makeText(LoginActivity.this, "Login fail..", Toast.LENGTH_SHORT);
//                            }
//                        }
//                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            this.finish();
        }
    }
}