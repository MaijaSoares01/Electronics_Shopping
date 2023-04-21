package com.android.shoppingzoo.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.shoppingzoo.Adapter.CustomerAdapter;
import com.android.shoppingzoo.Model.User;
import com.android.shoppingzoo.Model.Utils;
import com.android.shoppingzoo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllCustomersActivity extends AppCompatActivity {

    private CustomerAdapter customerAdapter;
    private RecyclerView recyclerView;
    private ArrayList<User> customerArrayList;

    DatabaseReference myRootRef;
    private ProgressBar progressBar;
    private TextView noJokeText;
    private EditText nameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_customers);

        customerArrayList = new ArrayList<User>();
        recyclerView = findViewById(R.id.customer_list);
        progressBar = findViewById(R.id.spin_progress_bar);
        noJokeText = findViewById(R.id.no_customer);
        nameInput = findViewById(R.id.name_input);
        myRootRef = FirebaseDatabase.getInstance().getReference();
        Utils.statusBarColor(ViewAllCustomersActivity.this);

        customerAdapter = new CustomerAdapter(customerArrayList, ViewAllCustomersActivity.this, true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setAdapter(customerAdapter);
        customerAdapter.notifyDataSetChanged();

        getDataFromFirebase();

        searchFunc();

    }

    private void searchFunc() {
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    if (customerArrayList.size() != 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noJokeText.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noJokeText.setVisibility(View.VISIBLE);
                    }

                    customerAdapter = new CustomerAdapter(customerArrayList, ViewAllCustomersActivity.this, true);
                    recyclerView.setAdapter(customerAdapter);
                    customerAdapter.notifyDataSetChanged();
                } else {
                    ArrayList<User> clone = new ArrayList<>();
                    for (User element : customerArrayList) {
                        if (element.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            clone.add(element);
                        }
                    }
                    if (clone.size() != 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noJokeText.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noJokeText.setVisibility(View.VISIBLE);
                    }

                    customerAdapter = new CustomerAdapter(clone, ViewAllCustomersActivity.this, true);
                    recyclerView.setAdapter(customerAdapter);
                    customerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getDataFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        final int[] counter = {0};
        myRootRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        User customer = new User();
                        customer = child.getValue(User.class);
                        customerArrayList.add(customer);
                        counter[0]++;
                        if (counter[0] == dataSnapshot.getChildrenCount()) {
                            setData();
                            progressBar.setVisibility(View.GONE);
                        }
                        Log.d("ShowEventInfo:", customer.toString());
                    }
                } else {
                    noJokeText.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void setData() {
        if (customerArrayList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noJokeText.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noJokeText.setVisibility(View.VISIBLE);
            customerAdapter.notifyDataSetChanged();
        }
    }

    public void goBack(View view) {
        finish();
    }
}