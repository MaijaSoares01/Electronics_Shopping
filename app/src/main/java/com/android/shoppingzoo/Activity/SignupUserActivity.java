package com.android.shoppingzoo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.shoppingzoo.Model.User;
import com.android.shoppingzoo.Model.Utils;
import com.android.shoppingzoo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class SignupUserActivity extends AppCompatActivity {
    private static final String TAG = "signupTag";
    User user;
    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference myRootRef;
    String userName, userEmail, userPass,userAddress;
    private TextInputEditText name, email, pass,address;
    private Button SignUPBtn, GoToLoginBtn;
    private TextInputLayout nameError, emailError, passwordError, addressError;
    private ProgressBar progressBar;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private int selectedGender=1;
    RadioGroup radioGroupGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_user);

        initAll();
        settingUpListeners();
    }

    private void settingUpListeners() {
        GoToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupUserActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        SignUPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = name.getText().toString().trim();
                userEmail = email.getText().toString().trim();
                userPass = pass.getText().toString().trim();
                userAddress = address.getText().toString().trim();

                boolean error = false;

                if (userName.isEmpty()) {
                    nameError.setError("Full Name required");
                    error = true;
                } else if (!userName.isEmpty()) {
                    nameError.setError(null);
                }
                if (userPass.isEmpty()) {
                    passwordError.setError("Password required");
                    error = true;
                } else if (!userPass.isEmpty()) {
                    passwordError.setError(null);
                }
                if (userEmail.isEmpty()) {
                    emailError.setError("Email required");
                    error = true;
                } else if (!userEmail.isEmpty()) {
                    emailError.setError(null);
                    if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                        emailError.setError("Email not valid e.g. example@mail.com");
                        error = true;
                    } else if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                        emailError.setError(null);
                    }
                }
                if (userAddress.isEmpty()) {
                    addressError.setError("Address required");
                    error = true;
                } else if (!userAddress.isEmpty()) {
                    addressError.setError(null);
                }
                if(!error){
                    //signup code goes here
                    RegisterNewAccount();
                }
            }
        });
    }

    private void RegisterNewAccount() {
        //creating new account on firebase for user
        progressBar.setVisibility(View.VISIBLE);
        SignUPBtn.setVisibility(View.GONE);

        user.setName(userName);
        user.setEmail(userEmail);
        user.setPass(userPass);
        user.setAddress(userAddress);
        user.setUserType("user");
        user.setPhotoUrl("");

        //creating account
        mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            user.setUserId(currentUserId);
                            myRootRef.child("Users").child(currentUserId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //show message
                                    Toast.makeText(SignupUserActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                                    Paper.book().write("user", user);
                                    Paper.book().write("active", "user");

                                    Intent intent=new Intent(SignupUserActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, e.toString());
                                }
                            });
                        } else {
                            Toast.makeText(SignupUserActivity.this, "Failed to Create Account..", Toast.LENGTH_SHORT).show();
                            Toast.makeText(SignupUserActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void initAll() {
        name = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        pass = findViewById(R.id.signup_pass);
        nameError = (TextInputLayout)findViewById(R.id.nameInputLayoutReg);
        passwordError = (TextInputLayout)findViewById(R.id.passwordInputLayoutReg);
        emailError = (TextInputLayout)findViewById(R.id.emailInputLayoutReg);
        addressError = (TextInputLayout)findViewById(R.id.addressInputLayoutReg);

        progressBar = findViewById(R.id.signup_progressbar);
        SignUPBtn = findViewById(R.id.signup_btn);
        GoToLoginBtn = findViewById(R.id.already_have_account_btn);
        address = findViewById(R.id.location_et);
        mAuth = FirebaseAuth.getInstance();
        myRootRef = FirebaseDatabase.getInstance().getReference();
        user = new User();

        Utils.statusBarColor(SignupUserActivity.this);
    }
}