package com.android.shoppingzoo.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.shoppingzoo.Model.Discount;
import com.android.shoppingzoo.Model.Utils;
import com.android.shoppingzoo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class NewDiscountActivity extends AppCompatActivity {
    DatabaseReference myRootRef;
    Button addBtn;
    boolean hasChild;
    private EditText name,percent;
    private ProgressBar progressBar;
    Discount discount;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_discount);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Discounts");

        initAll();
        SettingClickListeners();
    }

    private void SettingClickListeners() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameSt=name.getText().toString().trim();
                String percentSt=percent.getText().toString().trim();

                myRootRef = FirebaseDatabase.getInstance().getReference();
                myRootRef.child("Discounts").child("name").addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(nameSt)){
                            hasChild = true;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                if(TextUtils.isEmpty(nameSt)){
                    name.setError("Enter Discount Name");
                    name.requestFocus();
                }else if(hasChild){
                    name.setError("Discount Name not unique");
                    name.requestFocus();
                }
                else if(TextUtils.isEmpty(percentSt)){
                    percent.setError("Enter Discount Percentage");
                    percent.requestFocus();
                }else if(Double.parseDouble(percentSt)>100||Double.parseDouble(percentSt)<0){
                    percent.setError("Invalid Discount Percentage Amount");
                    percent.requestFocus();
                }
                else{
                    discount.setName(nameSt);
                    discount.setDiscount(Double.parseDouble(percentSt));
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    discount.setDateCreated(currentDateTimeString);
                    SaveInfoToDatabase();
                }
            }
        });
    }

    private void initAll() {
        hasChild = false;
        progressBar = findViewById(R.id.progress_bar);
        addBtn = findViewById(R.id.add_btn);

        name=findViewById(R.id.discount_name);
        percent=findViewById(R.id.discount_percent);

        myRootRef = FirebaseDatabase.getInstance().getReference();
        discount=new Discount();
        Utils.statusBarColor(NewDiscountActivity.this);
    }

    public void goBack(View view) {
        finish();
    }

    private void SaveInfoToDatabase() {
        String key=myRootRef.push().getKey();
        discount.setDiscountId(key);
        myRootRef.child("Discounts").child(key).setValue(discount).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NewDiscountActivity.this, "Discount Added!", Toast.LENGTH_SHORT).show();
                finish();
                Log.d("TAG", "Saved to Firebase");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Log.d("test", e.toString());
            }
        });
    }
}