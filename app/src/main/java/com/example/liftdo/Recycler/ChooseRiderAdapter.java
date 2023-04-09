package com.example.liftdo.Recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftdo.Model.PassengerRequest;
import com.example.liftdo.Model.PutRequest;
import com.example.liftdo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChooseRiderAdapter extends RecyclerView.Adapter<ChooseRiderAdapter.VH> {

    Context context;
    ArrayList<PutRequest> putRequests;
    double pLat, pLong;
    PassengerRequest object;

    public ChooseRiderAdapter(Context context,
                              ArrayList<PutRequest> putRequests,
                              double pLat, double pLong,
                              PassengerRequest object) {
        this.context = context;
        this.putRequests = putRequests;
        this.pLat = pLat;
        this.pLong = pLong;
        this.object = object;
    }

    @NonNull
    @Override
    public ChooseRiderAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_riderinfo, parent, false);
        return new ChooseRiderAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseRiderAdapter.VH holder, int position) {
        holder.tv_name.setText(putRequests.get(position).getName());
        holder.tv_vehicle.setText(putRequests.get(position).getvName());
        holder.tv_phno.setText(putRequests.get(position).getPhno());
        distancebtwpoints(holder, position);
        holder.accept.setOnClickListener(v -> {
                    requestDriver(putRequests.get(position));
                    removeAt(position);
                });//set progressbar
    }

    private void requestDriver(PutRequest putRequest) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference()
                .child("RequestedFromPassengers")
                .child(putRequest.getUID())
                .child(uid)
                .setValue(object);


    }

    private void removeAt(int position) {
        putRequests.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, putRequests.size());
        Toast.makeText(context, "Request Sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return putRequests.size();
    }

    @SuppressLint("SetTextI18n")
    void distancebtwpoints(ChooseRiderAdapter.VH holder, int pos) {
        float[] results = new float[1];
        Location.distanceBetween(pLat, pLong,
                putRequests.get(pos).getPickPointLat(), putRequests.get(pos).getPickPointLong(), results);
        double val = results[0] / 1000;

        holder.tv_distance.setText(new DecimalFormat("###.###").format(val) + " KM away");
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView tv_name, tv_phno, tv_ratings, tv_distance, tv_vehicle;
        Button accept;

        public VH(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_dName);
            tv_phno = itemView.findViewById(R.id.tv_phoneNum);
            tv_ratings = itemView.findViewById(R.id.tv_ratings);
            tv_distance = itemView.findViewById(R.id.tv_distance);
            tv_vehicle = itemView.findViewById(R.id.tv_vName);
            accept = itemView.findViewById(R.id.b_reqDriver);

        }
    }


}