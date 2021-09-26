package com.example.hotstuffpizza.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hotstuffpizza.R;
import com.example.hotstuffpizza.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private List<Order> orderList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView email;
        public TextView price;
        public ImageView done;

        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.itemOrderName);
            email = view.findViewById(R.id.itemOrderEmail);
            price = view.findViewById(R.id.itemOrderPrice);
            done = view.findViewById(R.id.itemOrderDone);
        }
    }

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.name.setText(order.getName());
        holder.email.setText(order.getEmail());
        holder.price.setText("Rs. " + String.format("%.2f", order.getTotal()) + "/=");
        holder.done.setVisibility(order.getIsComplete() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
