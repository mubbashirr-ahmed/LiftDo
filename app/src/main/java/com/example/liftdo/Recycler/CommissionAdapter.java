package com.example.liftdo.Recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftdo.Model.CommissionModel;
import com.example.liftdo.R;

import java.util.ArrayList;

public class CommissionAdapter extends RecyclerView.Adapter<CommissionAdapter.ViewHolder> {

    ArrayList<CommissionModel> commissionModels;
    Context context;
    public CommissionAdapter(Context context, ArrayList<CommissionModel> commissionModels)
    {
        this.commissionModels = commissionModels;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view =  LayoutInflater.from(context).inflate(R.layout.item_commission, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_price.setText(commissionModels.get(position).getPrice() + " Pkr Paid on");
        holder.tv_date.setText(commissionModels.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return commissionModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_price, tv_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_price = itemView.findViewById(R.id.tv_cprice);
            tv_date = itemView.findViewById(R.id.tv_cdate);
        }
    }
}
