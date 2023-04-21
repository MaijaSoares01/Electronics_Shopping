package com.android.shoppingzoo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.shoppingzoo.Admin.ViewAllDiscountsActivity;
import com.android.shoppingzoo.Admin.ViewAllProductsActivity;
import com.android.shoppingzoo.Model.Product;
import com.android.shoppingzoo.Model.Utils;
import com.android.shoppingzoo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EditProductActivity extends AppCompatActivity {
    String[] categoriesList = {"Mobile Devices", "Wearables", "Laptops", "Tv sets", "Monitors", "Game Consoles", "Appliances", "Printers", "Computers", "Other"};
    Spinner categorySpinner;
    String category = "";
    StorageReference storageRef;
    DatabaseReference myRootRef;
    ImageView productImg;
    Button removeBtn,changeBtn;
    private EditText name,manufacturer,colour,stock,price,description;
    private ProgressBar progressBar;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        initAll();

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(EditProductActivity.this, android.R.layout.simple_list_item_1, categoriesList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        SettingClickListeners();
    }

    private void SettingClickListeners() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent i = getIntent();
                                Product productObject = (Product) i.getSerializableExtra("product");
                                myRootRef.child("Products").child(productObject.getProductId()).removeValue();
                                Toast.makeText(v.getContext(),"Successfully Deleted Product", Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameSt=name.getText().toString().trim();
                String manufacturerSt=manufacturer.getText().toString().trim();
                String colourSt=colour.getText().toString().trim();
                String stockSt=stock.getText().toString().trim();
                String priceSt=price.getText().toString().trim();
                String descriptionSt=description.getText().toString().trim();
                if(TextUtils.isEmpty(nameSt)){
                    name.setError("Enter Product Name");
                    name.requestFocus();
                }
                else if(category.equals("Select Category")){
                    Toast.makeText(EditProductActivity.this, "Select Category", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(manufacturerSt)){
                    manufacturer.setError("Enter Manufacturer");
                    manufacturer.requestFocus();
                }
                else if(TextUtils.isEmpty(colourSt)){
                    colour.setError("Enter Product Colour");
                    colour.requestFocus();
                }
                else if(TextUtils.isEmpty(stockSt)){
                    stock.setError("Enter Product Stock");
                    stock.requestFocus();
                }
                else if(TextUtils.isEmpty(priceSt)){
                    price.setError("Enter Product Price");
                    price.requestFocus();
                }
                else if(TextUtils.isEmpty(descriptionSt)){
                    description.setError("Enter Product Description");
                    description.requestFocus();
                }
                else{
                    Intent i = getIntent();
                    Product productObject = (Product) i.getSerializableExtra("product");
                    myRootRef = FirebaseDatabase.getInstance().getReference("Products");
                    myRootRef.child(productObject.getProductId()).child("name").setValue(nameSt);
                    myRootRef.child(productObject.getProductId()).child("category").setValue(category);
                    myRootRef.child(productObject.getProductId()).child("manufacturer").setValue(manufacturerSt);
                    myRootRef.child(productObject.getProductId()).child("color").setValue(colourSt);
                    myRootRef.child(productObject.getProductId()).child("stock").setValue(stockSt);
                    myRootRef.child(productObject.getProductId()).child("description").setValue(descriptionSt);
                    myRootRef.child(productObject.getProductId()).child("price").setValue(Double.parseDouble(priceSt));
                    Toast.makeText(v.getContext(),"Successfully Updated Product", Toast.LENGTH_SHORT).show();
                    finish();
                    i = new Intent(v.getContext(), ViewAllProductsActivity.class);
                    i.putExtra("type", "Admin");
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }
            }
        });
    }

    private void initAll() {
        categorySpinner = findViewById(R.id.product_category_Spinner);
        progressBar = findViewById(R.id.progress_bar);

        productImg = findViewById(R.id.product_image);
        removeBtn = findViewById(R.id.remove_btn);
        changeBtn = findViewById(R.id.change_btn);

        name=findViewById(R.id.product_name);
        manufacturer=findViewById(R.id.manufacturer);
        colour=findViewById(R.id.colour);
        stock=findViewById(R.id.stock);
        price=findViewById(R.id.price);
        description=findViewById(R.id.description);

        storageRef = FirebaseStorage.getInstance().getReference();
        myRootRef = FirebaseDatabase.getInstance().getReference();
        product=new Product();
        Utils.statusBarColor(EditProductActivity.this);
        Intent i = getIntent();
        Product productObject = (Product) i.getSerializableExtra("product");

        try {
            if (productObject.getPhotoUrl() != null && !productObject.getPhotoUrl().equals("")) {
                Picasso.get().load(productObject.getPhotoUrl()).placeholder(R.drawable.ic_upload_photo).into(productImg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        name.setText(productObject.getName());
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(EditProductActivity.this, android.R.layout.simple_list_item_1, categoriesList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(categoryAdapter.getPosition(productObject.getCategory()));
        manufacturer.setText(productObject.getManufacturer());
        colour.setText(productObject.getColor());
        stock.setText(productObject.getStock());
        price.setText(Double.toString(productObject.getPrice()));
        description.setText(productObject.getDescription());

    }

    public void goBack(View view) {
        finish();
    }

    private void SaveInfoToDatabase() {
        Intent i = getIntent();
        Product productObject = (Product) i.getSerializableExtra("product");
        product.setProductId(productObject.getProductId());
        myRootRef.child("Products").child(productObject.getProductId()).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProductActivity.this, "Product Changed Successfully", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(EditProductActivity.this, ViewAllProductsActivity.class);
                intent.putExtra("type", "Admin");
                startActivity(intent);
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