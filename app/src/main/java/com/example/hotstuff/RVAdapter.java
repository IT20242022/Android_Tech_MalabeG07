package com.example.hotstuff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
private Context context;
ArrayList<add> list = new ArrayList<>();
public RVAdapter (Context ctx)
{
    this.context=ctx;
}
public void setItems(ArrayList<add> emp)
{
    list.addAll(emp);
}
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false);
        return new EmployeeVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    EmployeeVH vh = (EmployeeVH)  holder;
    add emp =list.get(position);
    vh.text1.setText(emp.getMaincategory());
    vh.text02.setText(emp.getSubcategory());
    vh.text03.setText(emp.getPrice());
    vh.text04.setText(emp.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
