package com.example.hotstuffpizza.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hotstuffpizza.R;
import com.example.hotstuffpizza.models.CartOrderMap;

import java.util.List;

public class CartOrderMapAdapter extends RecyclerView.Adapter<CartOrderMapAdapter.MyViewHolder> {

    private List<CartOrderMap> cartOrderMapList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView qty;
        public TextView price;
        public ImageButton add;
        public ImageButton minus;
        public ImageButton remove;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.itemCartName);
            qty = view.findViewById(R.id.itemCartQty);
            price = view.findViewById(R.id.itemCartPrice);
            add =  view.findViewById(R.id.itemCartPlusButton);
            minus = view.findViewById(R.id.itemCartMinusButton);
            remove = view.findViewById(R.id.itemCartRemoveButton);
        }
    }

    public CartOrderMapAdapter(List<CartOrderMap> cartOrderMapList) {
        this.cartOrderMapList = cartOrderMapList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CartOrderMap cartOrderMap = cartOrderMapList.get(position);

        holder.name.setText(cartOrderMap.getName());
        holder.qty.setText(Long.toString(cartOrderMap.getQuantity()) + " pcs");
        holder.price.setText("Rs. " + String.format("%.2f", cartOrderMap.getPrice()) + "/=");
        holder.add.setVisibility(View.GONE);
        holder.minus.setVisibility(View.GONE);
        holder.remove.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return cartOrderMapList.size();
    }
}
