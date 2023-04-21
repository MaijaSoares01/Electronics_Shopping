package com.android.shoppingzoo.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.android.shoppingzoo.Activity.SplashScreen;
import com.android.shoppingzoo.Model.Product;
import com.android.shoppingzoo.Model.Utils;
import com.android.shoppingzoo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewProductActivity extends AppCompatActivity {
    String[] categoriesList = {"Mobile Devices", "Wearables", "Laptops", "Tv sets", "Monitors", "Game Consoles", "Appliances", "Printers", "Computers", "Other"};
    Spinner categorySpinner;
    String category = "";

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath=null;
    StorageReference storageRef;
    DatabaseReference myRootRef;
    ImageView uploadPhotoBtn, productImg;
    Button addBtn;
    private String downloadImageUrl = "";
    private EditText name,manufacturer,colour,stock,price,description;
    private ProgressBar progressBar;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        initAll();

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_list_item_1, categoriesList);
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

        uploadPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameSt=name.getText().toString().trim();
                String manufacturerSt=manufacturer.getText().toString().trim();
                String colourSt=colour.getText().toString().trim();
                String stockSt=stock.getText().toString().trim();
                String priceSt=price.getText().toString().trim();
                String descriptionSt=description.getText().toString().trim();
                if(filePath==null){
                    Toast.makeText(NewProductActivity.this, "Please select Product Image", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(nameSt)){
                    name.setError("Enter Product Name");
                    name.requestFocus();
                }
                else if(category.equals("Select Category")){
                    Toast.makeText(NewProductActivity.this, "Select Category", Toast.LENGTH_SHORT).show();
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
                    product.setName(nameSt);
                    product.setCategory(category);
                    product.setManufacturer(manufacturerSt);
                    product.setColor(colourSt);
                    product.setStock(stockSt);
                    product.setDescription(descriptionSt);
                    product.setPrice(Double.parseDouble(priceSt));
                    UploadImage();
                }
            }
        });
    }

    private void initAll() {
        categorySpinner = findViewById(R.id.product_category_Spinner);
        progressBar = findViewById(R.id.progress_bar);

        uploadPhotoBtn = findViewById(R.id.upload_image_btn);
        productImg = findViewById(R.id.product_image);
        addBtn = findViewById(R.id.add_btn);

        name=findViewById(R.id.product_name);
        manufacturer=findViewById(R.id.manufacturer);
        colour=findViewById(R.id.colour);
        stock=findViewById(R.id.stock);
        price=findViewById(R.id.price);
        description=findViewById(R.id.description);

        storageRef = FirebaseStorage.getInstance().getReference();
        myRootRef = FirebaseDatabase.getInstance().getReference();
        product=new Product();
        Utils.statusBarColor(NewProductActivity.this);
    }

    public void goBack(View view) {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(filePath));
                productImg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void UploadImage() {
        if (filePath != null) {
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference childRef = storageRef.child("product_images").child(System.currentTimeMillis() + ".jpg");
            final UploadTask uploadTask = childRef.putFile(filePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(NewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(NewProductActivity.this, "Photo uploaded...", Toast.LENGTH_SHORT).show();
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            downloadImageUrl = childRef.getDownloadUrl().toString();
                            return childRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadImageUrl = task.getResult().toString();
                                Log.d("imageUrl", downloadImageUrl);
                                product.setPhotoUrl(downloadImageUrl);
                                SaveInfoToDatabase();
                            }
                        }
                    });
                }
            });

        } else {
            Toast.makeText(NewProductActivity.this, "Select an Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveInfoToDatabase() {
        String key=myRootRef.push().getKey();
        product.setProductId(key);
        myRootRef.child("Products").child(key).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NewProductActivity.this, "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();
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