package com.android.shoppingzoo.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.shoppingzoo.Adapter.DiscountAdapter;
import com.android.shoppingzoo.Model.Discount;
import com.android.shoppingzoo.Model.Utils;
import com.android.shoppingzoo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllDiscountsActivity extends AppCompatActivity {

    private DiscountAdapter disAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Discount> discountArrayList;

    DatabaseReference myRootRef;
    private ProgressBar progressBar;
    private TextView noJokeText;
    private EditText nameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_discounts);


        discountArrayList = new ArrayList<Discount>();
        recyclerView = findViewById(R.id.discount_list);
        progressBar = findViewById(R.id.spin_progress_bar);
        noJokeText = findViewById(R.id.no_discount);
        nameInput = findViewById(R.id.name_input);
        myRootRef = FirebaseDatabase.getInstance().getReference();
        Utils.statusBarColor(ViewAllDiscountsActivity.this);

        disAdapter = new DiscountAdapter(discountArrayList, ViewAllDiscountsActivity.this, true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.setAdapter(disAdapter);
        disAdapter.notifyDataSetChanged();

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
                    if (discountArrayList.size() != 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noJokeText.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noJokeText.setVisibility(View.VISIBLE);
                    }

                    disAdapter = new DiscountAdapter(discountArrayList, ViewAllDiscountsActivity.this, true);
                    recyclerView.setAdapter(disAdapter);
                    disAdapter.notifyDataSetChanged();
                } else {
                    ArrayList<Discount> clone = new ArrayList<>();
                    for (Discount element : discountArrayList) {
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

                    disAdapter = new DiscountAdapter(clone, ViewAllDiscountsActivity.this, true);
                    recyclerView.setAdapter(disAdapter);
                    disAdapter.notifyDataSetChanged();
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
        myRootRef.child("Discounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Discount discount = new Discount();
                        discount = child.getValue(Discount.class);
                        discountArrayList.add(discount);
                        counter[0]++;
                        if (counter[0] == dataSnapshot.getChildrenCount()) {
                            setData();
                            progressBar.setVisibility(View.GONE);
                        }
                        Log.d("ShowEventInfo:", discount.toString());
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
        if (discountArrayList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noJokeText.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noJokeText.setVisibility(View.VISIBLE);
            disAdapter.notifyDataSetChanged();
        }
    }

    public void goBack(View view) {
        finish();
    }
}