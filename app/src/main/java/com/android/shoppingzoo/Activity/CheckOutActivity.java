package com.android.shoppingzoo.Activity;

import static com.android.shoppingzoo.Model.Utils.TAG_medicine_list;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.shoppingzoo.Adapter.CartCustomAdapter;
import com.android.shoppingzoo.Model.Discount;
import com.android.shoppingzoo.Model.Order;
import com.android.shoppingzoo.Model.Product;
import com.android.shoppingzoo.Model.Utils;
import com.android.shoppingzoo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import io.paperdb.Paper;

public class CheckOutActivity extends AppCompatActivity {

    private ImageView checkOutBackBtn;
    private TextView orderPrice,shipmentPrice,discountPrice, totalPayablePrice, checkOutBtn,streetAddress;
    private EditText usercomments;
    private TextInputEditText discount;
    private TextInputLayout discountError;
    private Button applyBtn;
    DatabaseReference myRootRef;

    private ProgressDialog pd;
    private AlertDialog.Builder builder;
    private Activity context;
    private Order order;
    private ArrayList<Product> productArrayList;

    private String discountSt;
    private String street;
    private String comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        initAll();
        //alert dailog
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");
        //setting Up listeners
        OnClickListeners();
        Utils.statusBarColor(CheckOutActivity.this);
    }

    private void OnClickListeners() {
        checkOutBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //checkout logic goes here
        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                street=streetAddress.getText().toString();
                comments=usercomments.getText().toString();
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        if(order.getTotalPrice()>0){
                            order.setStatus("Pending");
                            try {
                                settingDataOnServer();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(CheckOutActivity.this, "No Item in Cart", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discountSt=discount.getText().toString().trim();
                if(discountSt.equals("")||discountSt.equals(null)){
                    discountError.setError("Discount is Empty");
                }else{
                    myRootRef = FirebaseDatabase.getInstance().getReference();
                    myRootRef.child("Discounts").addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    Discount dis = new Discount();
                                    dis = child.getValue(Discount.class);
                                    if(dis.getName().equals(discountSt)){
                                        double tp = Double.parseDouble(totalPayablePrice.getText().toString().substring(1));
                                        double dper = dis.getDiscount();
                                        double dp = (tp/100)*dper;
                                        double ftp = tp - dp;
                                        discountPrice.setText("€"+String.valueOf(dp));
                                        discountPrice.setTextColor(getResources().getColor(R.color.green));
                                        totalPayablePrice.setText("€"+String.valueOf(ftp));
                                        order.setTotalPrice(ftp);
                                        applyBtn.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                if(applyBtn.getVisibility()== View.GONE){
                    discountError.setError("No Discount with this name..");
                }
            }
        });
    }

    private void settingDataOnServer() throws ParseException {
        pd.show(this,"Please Wait..","Submitting order..");


        DatabaseReference root= FirebaseDatabase.getInstance().getReference().child("Order");
        String key=root.push().getKey();
        //id set
        order.setId(key);
        //date set
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        order.setDateOfOrder(currentDateTimeString);
        //total payabel set
        order.setTotalPrice(order.getTotalPrice()+10);
        //setting address fields
        order.setStreet(street);
        order.setComments(comments);

        root.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                order.getCartProductList().clear();
                order.setTotalPrice(0.0);
                productArrayList.clear();
                Paper.book().delete("order");
                Paper.book().write("order",order);

                //cartCustomAdapter.notifyDataSetChanged();
                //totalPriceView.setText("0.0");
                pd.dismiss();
                Toast.makeText(CheckOutActivity.this,"Order Submitted",Toast.LENGTH_LONG).show();
            }
        });

        Intent intent=new Intent(CheckOutActivity.this,MainActivity.class);
        startActivity(intent);

    }

    private void initAll() {

        //view
        checkOutBackBtn=findViewById(R.id.checkout_back_btn);
        orderPrice=findViewById(R.id.checkout_order_price_view);
        shipmentPrice=findViewById(R.id.checkout_shipping_price_view);
        discountPrice=findViewById(R.id.checkout_discount_price_view);
        discountError=findViewById(R.id.discountInputLayout);
        totalPayablePrice=findViewById(R.id.checkout_total_price_view);
        streetAddress=findViewById(R.id.checkout_address_view);
        usercomments=findViewById(R.id.checkout_comment_view);
        checkOutBtn=findViewById(R.id.checkout_btn);
        discount=findViewById(R.id.discount);
        applyBtn=findViewById(R.id.apply_discount_btn);

        pd=new ProgressDialog(this);

        order=new Order();
        Intent intent=getIntent();
        order= (Order) intent.getSerializableExtra("order");

        productArrayList =new ArrayList<>();
        Intent i=getIntent();
        productArrayList = (ArrayList<Product>) i.getSerializableExtra(TAG_medicine_list);
        streetAddress.setText(order.getAddress());
        //setting values of prices
        //orderPrice.setText("Rs. "+ order.getTotalPrice());
        orderPrice.setText("€"+ new DecimalFormat("##.##").format(order.getTotalPrice()));
       // totalPayablePrice.setText("Rs."+ (order.getTotalPrice()+200));
        totalPayablePrice.setText("€"+ new DecimalFormat("##.##").format(order.getTotalPrice()+10));
    }
}
