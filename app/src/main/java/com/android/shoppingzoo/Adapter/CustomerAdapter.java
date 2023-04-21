package com.android.shoppingzoo.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.shoppingzoo.Admin.ViewAllCustomersActivity;
import com.android.shoppingzoo.Model.User;
import com.android.shoppingzoo.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.VH>{
    ViewAllCustomersActivity viewCustomers = new ViewAllCustomersActivity();
    List<User> myUserList;
    Activity context;
    boolean isAdmin;
    DatabaseReference reference;

    public CustomerAdapter(List<User> userList, Activity context,boolean isAdmin) {
        this.myUserList = userList;
        this.context = context;
        this.isAdmin=isAdmin;
    }

    @NonNull
    @Override
    public CustomerAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View custView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_customer_layout, parent, false);

        return new CustomerAdapter.VH(custView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.VH holder, int position) {
        User customer=myUserList.get(position);

        holder.customer_id.setText(customer.getUserId());
        holder.customer_name.setText(customer.getName());
        holder.customer_email.setText(customer.getEmail());
        String imageURL = customer.getPhotoUrl();
        try {
            if (imageURL != null && !imageURL.equals("")) {
                Picasso.get().load(imageURL).placeholder(R.drawable.profile).into(holder.icon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return myUserList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView customer_id,customer_name,customer_email;
        CircleImageView icon;

        public VH(@NonNull View custView) {
            super(custView);
            icon = custView.findViewById(R.id.customer_icon);
            customer_id = custView.findViewById(R.id.customer_id);
            customer_name = custView.findViewById(R.id.customer_name);
            customer_email = custView.findViewById(R.id.customer_email);
        }
    }
}
