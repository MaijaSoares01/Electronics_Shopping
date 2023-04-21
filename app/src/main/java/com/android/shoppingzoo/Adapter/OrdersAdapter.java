package com.android.shoppingzoo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shoppingzoo.Admin.CustomersOrders;
import com.android.shoppingzoo.Model.Order;
import com.android.shoppingzoo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<Order> orderArrayList;
    private Order order;
    boolean isAdmin;
    DatabaseReference reference;
    private String strType;

    public OrdersAdapter(Activity context, ArrayList<Order> orderArrayList, boolean isAdmin) {
        this.context = context;
        this.orderArrayList = orderArrayList;
        this.order = order;
        this.isAdmin = isAdmin;
    }

    public OrdersAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Order order = orderArrayList.get(position);
        holder.status.setText(order.getStatus());
        if (order.getStatus().equals("Processing")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.orange));
        } else if (order.getStatus().equals("Processed")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
        }
        holder.date.setText(order.getDateOfOrder());
        holder.totalPrice.setText("€" + order.getTotalPrice());
        if (!isAdmin) {
            holder.stage_btn.setVisibility(View.GONE);
            strType = "User";
        } else {
            holder.stage_btn.setVisibility(View.VISIBLE);
            strType = "Admin";
            holder.stage_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("Order").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String customerId = snapshot.toString();
                            customerId = customerId.replace("DataSnapshot { key = Order, value = {","").substring(0,28);
                            //Log.d("TAG", "CUSTOMER ID :" + customerId);
                            String finalCustomerId = customerId;
                            reference.child("Order").child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String orderId = snapshot.getValue().toString();
                                    orderId = orderId.replace(":{","").substring(1,21);
                                    //Log.d("TAG", "ORDER ID :" + orderId);
                                    if (order.getId().equals(orderId)) {
                                        if (order.getStatus().equals("Pending")) {
                                            reference.child("Order").child(finalCustomerId).child(order.getId()).child("status").setValue("Processing");
                                            Toast.makeText(v.getContext(), "Successfully Updated Status to Processing", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(v.getContext(), CustomersOrders.class);
                                            i.putExtra("type", strType);
                                            context.finish();
                                            context.overridePendingTransition(0, 0);
                                            context.startActivity(i);
                                            context.overridePendingTransition(0, 0);
                                        } else if (order.getStatus().equals("Processing")) {
                                            reference.child("Order").child(finalCustomerId).child(order.getId()).child("status").setValue("Processed");
                                            Toast.makeText(v.getContext(), "Successfully Updated Status to Processed", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(v.getContext(), CustomersOrders.class);
                                            i.putExtra("type", strType);
                                            context.finish();
                                            context.overridePendingTransition(0, 0);
                                            context.startActivity(i);
                                            context.overridePendingTransition(0, 0);
                                        } else {
                                            Toast.makeText(context, "Order Already Processed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView status, date, totalPrice, quantity;
        Button stage_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stage_btn = itemView.findViewById(R.id.stage_btn);
            status = itemView.findViewById(R.id.order_status);
            date = itemView.findViewById(R.id.date_of_order);
            totalPrice = itemView.findViewById(R.id.order_total_price);
        }
    }


}
