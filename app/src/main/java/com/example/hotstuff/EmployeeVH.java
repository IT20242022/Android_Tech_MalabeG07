package com.example.hotstuff;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeVH extends RecyclerView.ViewHolder {
public TextView text1,text02,text03,text04;
    public EmployeeVH(@NonNull View itemView) {
        super(itemView);
        text1=itemView.findViewById(R.id.text1);
        text02=itemView.findViewById(R.id.text02);
        text03=itemView.findViewById(R.id.text03);
        text04=itemView.findViewById(R.id.text04);

    }
}
