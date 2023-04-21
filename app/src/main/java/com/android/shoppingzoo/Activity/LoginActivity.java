package com.android.shoppingzoo.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.shoppingzoo.Admin.AdminLoginActivity;
import com.android.shoppingzoo.Model.Utils;
import com.android.shoppingzoo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "signupTag";
    String userEmail, userPass;
    private TextInputEditText email, pass;
    private TextInputLayout emailError, passwordError;
    private CheckBox rememberCheckBox;
    private TextView forgetPass,loginResultTv, didNotHaveAcc;
    private Button login_btn;
    private ImageView adminLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference myRootRef;

    SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initAll();
        settingUpListeners();
        mPrefs = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        getPreferencesData();
    }

    private void getPreferencesData() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        if(sp.contains("pref_email")){
            String spEmail = sp.getString("pref_email","not found");
            email.setText(spEmail.toString());
        }
        if(sp.contains("pref_password")){
            String spPassword = sp.getString("pref_password","not found");
            pass.setText(spPassword.toString());
        }
        if(sp.contains("pref_check")){
            Boolean spCheck = sp.getBoolean("pref_check",false);
            rememberCheckBox.setChecked(spCheck);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void settingUpListeners() {
        didNotHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupUserActivity.class);
                startActivity(intent);
            }
        });
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final Admin admin = new Admin();
//                admin.setEmail("admin@gmail.com");
//                admin.setName("Admin");
//                mAuth.createUserWithEmailAndPassword("admin@gmail.com", "admin123")
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                                    admin.setId(currentUserId);
//                                    myRootRef.child("Admin").child(currentUserId).setValue(admin).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Intent intent = new Intent(LoginActivity.this, AdminHome.class);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Log.d(TAG, e.toString());
//                                        }
//                                    });
//                                } else {
//
//                                }
//                            }
//                        });
                Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
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
                    login_btn.setVisibility(View.INVISIBLE);

                    mAuth.signInWithEmailAndPassword(userEmail, userPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if(rememberCheckBox.isChecked()){
                                            Boolean isChecked = rememberCheckBox.isChecked();
                                            SharedPreferences.Editor editor = mPrefs.edit();
                                            editor.putString("pref_email",userEmail);
                                            editor.putString("pref_password",userPass);
                                            editor.putBoolean("pref_check",isChecked);
                                            editor.apply();
                                        }else{
                                            mPrefs.edit().clear().apply();
                                        }
                                        loginResultTv.setText("pass");
                                        //sign in Success
                                        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        myRootRef.child("Users").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //place data in datasnapshot that we can show
                                                if (dataSnapshot.exists()) {
                                                    progressBar.setVisibility(View.GONE);
                                                    login_btn.setEnabled(true);
                                                    login_btn.setVisibility(View.VISIBLE);
                                                    Paper.book().write("active", "user");
                                                    Toast.makeText(LoginActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    mAuth.signOut();
                                                    Toast.makeText(LoginActivity.this, "This is not User login details", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    login_btn.setEnabled(true);
                                                    login_btn.setVisibility(View.VISIBLE);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                                        });
                                    } else {
                                        loginResultTv.setText("fail");
                                        progressBar.setVisibility(View.GONE);
                                        login_btn.setEnabled(true);
                                        login_btn.setVisibility(View.VISIBLE);
                                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }

            private void getPreferencesData() {
                SharedPreferences sp = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                if(sp.contains("pref_email")){
                    String spEmail = sp.getString("pref_email","not found");
                    email.setText(spEmail.toString());
                }
                if(sp.contains("pref_password")){
                    String spPassword = sp.getString("pref_password","not found");
                    pass.setText(spPassword.toString());
                }
                if(sp.contains("pref_check")){
                    Boolean spCheck = sp.getBoolean("pref_check",false);
                    rememberCheckBox.setChecked(spCheck);
                }
            }
        });


        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomAlertDialog);
                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.update_password_layout, viewGroup, false);

                final EditText emailImput = (EditText) dialogView.findViewById(R.id.loginEmailEditText);
                final Button verifyBtn = (Button) dialogView.findViewById(R.id.loginBtnLinLayout);
                final ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.spin_progress_bar);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                verifyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String email = emailImput.getText().toString().trim();

                        if (!TextUtils.isEmpty(email)) {
                            progressBar.setVisibility(View.VISIBLE);
                            verifyBtn.setVisibility(View.GONE);

                            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //when email has been successfully send
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        verifyBtn.setVisibility(View.VISIBLE);
                                        Log.d("testPassrest", "successful");
                                        Toast.makeText(LoginActivity.this, "Email Has Been Sent", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    } else {
                                        Log.d("testPassrest", "fail");
                                        Toast.makeText(LoginActivity.this, "Email Failed, Please Try Again", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //if email failed
                                    progressBar.setVisibility(View.GONE);
                                    verifyBtn.setVisibility(View.VISIBLE);
                                    Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                alertDialog.show();
            }
        });
    }


    private void initAll() {
        email = findViewById(R.id.login_email);
        forgetPass = findViewById(R.id.forget_pass);
        pass = findViewById(R.id.login_pass);
        login_btn = findViewById(R.id.login_btn);
        adminLogin = findViewById(R.id.manager_portal_btn);
        loginResultTv = findViewById(R.id.login_results);
        emailError = findViewById(R.id.emailInputLayoutUser);
        passwordError = findViewById(R.id.passwordInputLayoutUser);
        rememberCheckBox = findViewById(R.id.rememberCheckBox);
        didNotHaveAcc = findViewById(R.id.registerText);
        progressBar = findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        myRootRef = FirebaseDatabase.getInstance().getReference();

        Utils.statusBarColor(LoginActivity.this);

    }
}