package com.android.shoppingzoo.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.shoppingzoo.Activity.SearchFiltersActivity;
import com.android.shoppingzoo.Adapter.ProductsAdapter;
import com.android.shoppingzoo.Model.Product;
import com.android.shoppingzoo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {
    private View view;

    private static ProductsAdapter mAdapter;
    private static RecyclerView recyclerView;
    private static ArrayList<Product> productArrayList;
    private ArrayList<Product> tempList;

    DatabaseReference myRootRef;
    private ProgressBar progressBar;
    private TextView noJokeText;
    private ImageView filtersBtn;

    public static String category = "";
    public static String sort = "";
    public static String sortBy = "";

    private static EditText nameInput;
    public static boolean isCategorySelected = false;
    public static boolean isSortSelected = false;
    public static boolean isSortBySelected = false;
    public static boolean isFiltersApplied = false;

    public static Context context;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        productArrayList = new ArrayList<Product>();
        tempList = new ArrayList<Product>();
        recyclerView = view.findViewById(R.id.product_list);
        progressBar = view.findViewById(R.id.spin_progress_bar);
        noJokeText = view.findViewById(R.id.no_product);
        nameInput = view.findViewById(R.id.name_input);
        filtersBtn = view.findViewById(R.id.filters_btn);

        context = getActivity();

        myRootRef = FirebaseDatabase.getInstance().getReference();

        mAdapter = new ProductsAdapter(productArrayList, getActivity(), false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        settingClickListners();

        getDataFromFirebase();

        searchFunc();

        return view;
    }

    private void ApplyFilters() {
        Log.d("TEStArraySize", productArrayList.size() + "");
        if (tempList.size() > 0) {
            tempList.clear();
            Log.d("listClear", tempList.size() + "");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(tempList, Comparator.comparing(Product::getName));
        }
        if (isCategorySelected) {
            for (Product element : productArrayList) {
                if (element.getCategory().equals(category)) {
                    tempList.add(element);
                }
            }
        }
        if (isSortSelected) {
            if(sort.equals("Ascending")){
                if(sortBy.equals("Name")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(tempList, Comparator.comparing(Product::getName));
                    }
                }else if(sortBy.equals("Price")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(tempList, Comparator.comparing(Product::getPrice));
                    }
                }else if(sortBy.equals("Manufacturer")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(tempList, Comparator.comparing(Product::getManufacturer));
                    }
                }else if(sortBy.equals("Star Rating")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(tempList, Comparator.comparing(Product::getAvgStarRating));
                    }
                }
            }else{
                if(sortBy.equals("Name")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(tempList, Comparator.comparing(Product::getName).reversed());
                    }
                }else if(sortBy.equals("Price")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(tempList, Comparator.comparing(Product::getPrice).reversed());
                    }
                }else if(sortBy.equals("Manufacturer")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(tempList, Comparator.comparing(Product::getManufacturer).reversed());
                    }
                }else if(sortBy.equals("Star Rating")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Collections.sort(tempList, Comparator.comparing(Product::getAvgStarRating).reversed());
                    }
                }
            }
        }


        if (tempList.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noJokeText.setVisibility(View.GONE);

            mAdapter = new ProductsAdapter(tempList, getActivity(), false);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
            noJokeText.setVisibility(View.VISIBLE);
        }


    }

    private void searchFunc() {
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    if (productArrayList.size() != 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noJokeText.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noJokeText.setVisibility(View.VISIBLE);
                    }

                    mAdapter = new ProductsAdapter(productArrayList, getActivity(), false);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ArrayList<Product> clone = new ArrayList<>();
                    for (Product element : productArrayList) {
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

                    mAdapter = new ProductsAdapter(clone, getActivity(), false);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void settingClickListners() {
        filtersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchFiltersActivity.class));
            }
        });
    }

    public void getDataFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        final int[] counter = {0};
        myRootRef.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Product product = new Product();
                        product = child.getValue(Product.class);
                        productArrayList.add(product);
                        counter[0]++;
                        if (counter[0] == dataSnapshot.getChildrenCount()) {
                            setData();
                            progressBar.setVisibility(View.GONE);
                        }
                        Log.d("ShowEventInfo:", product.toString());
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

    public static void clearClicked() {
        isCategorySelected = false;
        isSortSelected = false;
        isSortBySelected = false;
        isFiltersApplied = false;

        mAdapter = new ProductsAdapter(productArrayList, (Activity) context, false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        nameInput.setText("");
    }

    private void setData() {
        if (productArrayList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noJokeText.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noJokeText.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFiltersApplied) {
            ApplyFilters();
        }
    }
}