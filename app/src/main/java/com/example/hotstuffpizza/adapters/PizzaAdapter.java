package com.example.hotstuffpizza.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hotstuffpizza.R;
import com.example.hotstuffpizza.models.Pizza;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.MyViewHolder> {

    private List<Pizza> pizzaList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name, description, price;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.itemPizzaImage);
            name = view.findViewById(R.id.itemPizzaName);
            description = view.findViewById(R.id.itemPizzaDesc);
            price = view.findViewById(R.id.itemPizzaPrice);
        }
    }

    public PizzaAdapter(List<Pizza> pizzaList) {
        this.pizzaList = pizzaList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pizza, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Pizza pizza = pizzaList.get(position);
        if (pizza.getImageUrl() != null || !pizza.getImageUrl().equals("")) {
            Picasso.get().load(pizza.getImageUrl()).fit().centerCrop().into(holder.image);
        }
        holder.name.setText(pizza.getName());
        holder.description.setText(pizza.getDescription());
        holder.price.setText("Rs. "+ String.format("%.2f", pizza.getPrice()) +"/=");
    }

    @Override
    public int getItemCount() {
        return pizzaList.size();
    }
}
