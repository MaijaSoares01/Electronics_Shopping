package com.android.shoppingzoo.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shoppingzoo.Admin.AdminHome;
import com.android.shoppingzoo.Admin.ViewAllDiscountsActivity;
import com.android.shoppingzoo.Model.Discount;
import com.android.shoppingzoo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.VHolder>{
    ViewAllDiscountsActivity viewDiscounts = new ViewAllDiscountsActivity();
    List<Discount> myDisList;
    Activity context;
    boolean isAdmin;
    DatabaseReference reference;

    public DiscountAdapter(List<Discount> disList, Activity context,boolean isAdmin) {
        this.myDisList = disList;
        this.context = context;
        this.isAdmin=isAdmin;
    }

    @NonNull
    @Override
    public DiscountAdapter.VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View disView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_discount_layout, parent, false);

        return new DiscountAdapter.VHolder(disView);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountAdapter.VHolder holder, int position) {

        Discount discount=myDisList.get(position);

        holder.name.setText(discount.getName());
        holder.discountPercent.setText(discount.getDiscount() + "%");
        holder.date.setText(discount.getDateCreated());

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAdmin){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    reference = FirebaseDatabase.getInstance().getReference("Discounts");
                                    reference.child(discount.getDiscountId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(v.getContext(),"Successfully Deleted Discount", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(v.getContext(), ViewAllDiscountsActivity.class);
                                                context.finish();
                                                context.overridePendingTransition(0, 0);
                                                context.startActivity(i);
                                                context.overridePendingTransition(0, 0);
                                            }else{
                                                Toast.makeText(v.getContext(), "Couldn't Delete Discount", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();


                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDisList.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {

        TextView name,discountPercent,date;
        Button delete_btn;

        public VHolder(@NonNull View disView) {
            super(disView);
            name = disView.findViewById(R.id.discount_name);
            discountPercent = disView.findViewById(R.id.discount_amount);
            date = disView.findViewById(R.id.discount_date_created);
            delete_btn = disView.findViewById(R.id.remove_discount);
        }
    }
}
