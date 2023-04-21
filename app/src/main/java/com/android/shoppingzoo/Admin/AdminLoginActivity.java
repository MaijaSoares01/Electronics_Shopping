package com.android.shoppingzoo.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.shoppingzoo.Activity.LoginActivity;
import com.android.shoppingzoo.Activity.SignupUserActivity;
import com.android.shoppingzoo.Activity.SplashScreen;
import com.android.shoppingzoo.Model.Utils;
import com.android.shoppingzoo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class AdminLoginActivity extends AppCompatActivity {

    private static final String TAG = "AdminsigninTag";
    private TextInputEditText email, pass;
    private TextInputLayout emailError, passwordError;
    String userEmail, userPass;
    private Button loginBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference myRootRef;
    private ImageView backimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        Paper.init(AdminLoginActivity.this);
        initAll();
        settingUpListeners();
    }

    private void settingUpListeners() {

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = email.getText().toString().trim();
                userPass = pass.getText().toString().trim();
                boolean error = false;
                if (userEmail.isEmpty()) {
                    emailError.setError("Email Required");
                    error = true;
                }else if(!userEmail.isEmpty()) {
                    emailError.setError(null);
                    if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                        emailError.setError("Email not valid e.g. example@mail.com");
                        error = true;
                    }else if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                        emailError.setError(null);
                    }
                }
                if(userPass.isEmpty()){
                    passwordError.setError("Password Required");
                    error = true;
                }else if(!userPass.isEmpty()){
                    passwordError.setError(null);
                }
                if(error == false){
                    progressBar.setVisibility(View.VISIBLE);
                    loginBtn.setVisibility(View.INVISIBLE);

                    mAuth.signInWithEmailAndPassword(userEmail, userPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        myRootRef.child("Admin").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    progressBar.setVisibility(View.GONE);
                                                    loginBtn.setEnabled(true);
                                                    loginBtn.setVisibility(View.VISIBLE);
                                                    Paper.book().write("active","admin");
                                                    Intent intent = new Intent(AdminLoginActivity.this, AdminHome.class);
                                                    Toast.makeText(AdminLoginActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    mAuth.signOut();
                                                    Toast.makeText(AdminLoginActivity.this, "This is not Admin login details", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    loginBtn.setEnabled(true);
                                                    loginBtn.setVisibility(View.VISIBLE);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        loginBtn.setEnabled(true);
                                        loginBtn.setVisibility(View.VISIBLE);
                                        Toast.makeText(AdminLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(AdminLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initAll() {
        email = findViewById(R.id.login_email);
        pass = findViewById(R.id.login_pass);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(View.GONE);
        backimage = findViewById(R.id.backimage);
        emailError = findViewById(R.id.emailInputLayoutAdmin);
        passwordError = findViewById(R.id.passwordInputLayoutAdmin);

        mAuth = FirebaseAuth.getInstance();
        myRootRef = FirebaseDatabase.getInstance().getReference();
        Utils.statusBarColor(AdminLoginActivity.this);
    }
}