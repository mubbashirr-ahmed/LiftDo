package com.example.liftdo.Recycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftdo.AllActivities.MapActivities.RiderTrackRide;
import com.example.liftdo.Model.AcceptedRequests;
import com.example.liftdo.Model.PassengerRequest;
import com.example.liftdo.Model.PutRequest;
import com.example.liftdo.Model.Users;
import com.example.liftdo.Notifications.FCMSend;
import com.example.liftdo.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    ArrayList<PassengerRequest> arrayList;
    Context context;
    Users users;
    LatLng myPos;

    public RequestAdapter(Context context, ArrayList<PassengerRequest> arrayList, Users users, LatLng myPos) {
        this.arrayList = arrayList;
        this.context = context;
        this.users = users;
        this.myPos = myPos;
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position) {


        holder.pLoc.setText(arrayList.get(0).getAddressLineP());
        holder.dLoc.setText(arrayList.get(0).getAddressLineD());
        holder.name.setText(arrayList.get(position).getName());
        holder.phone.setText(arrayList.get(position).getPhNo());
        //destination();
        holder.accept.setOnClickListener(view -> verify(arrayList.get(position)));
    }

    private void verify(PassengerRequest putRequest) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("PutRequests").child(putRequest.getID());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ref.removeEventListener(this);
                    //acceptRequest(putRequest);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void acceptRequest(PutRequest putRequest) {

        AcceptedRequests acceptedRequests = new AcceptedRequests(users.getID(),
                putRequest.getUID(),
                users.getVehicleNum(),
                users.getUsername(),
                putRequest.getAddressLineP(),
                putRequest.getAddressLineD(),
                users.getPhNo(),
                putRequest.getPhno(),
                putRequest.getPickPointLat(),
                putRequest.getPickPointLong());

        FirebaseDatabase.getInstance().getReference()
                .child("AcceptedRequests")
                .child(putRequest.getUID())
                .setValue(acceptedRequests);
        //////////////////////////////////
        FirebaseDatabase.getInstance().getReference()
                .child("PutRequests")
                .child(putRequest.getUID())
                .removeValue();
        //////////////////////////////////
        Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();

        Thread t1 = new Thread(() -> sendNotifications(putRequest.getUID()));
        t1.start();

        Gson gson = new Gson();
        String myJson = gson.toJson(acceptedRequests);

        Intent intent = new Intent(context, RiderTrackRide.class);
        intent.putExtra("passengerID", putRequest.getUID());
        intent.putExtra("acceptedRequest", myJson);
        context.startActivity(intent);
    }
    private void sendNotifications(String ID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(ID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reference.removeEventListener(this);
                    Users users = snapshot.getValue(Users.class);
                    assert users != null;
                    String token = users.getToken();
                    FCMSend.pushNotification(context,
                            token,
                            "Request Accepted",
                            "Driver is on the way!");
                    Toast.makeText(context, "Notification Sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button accept;
        TextView phone;
        TextView pLoc, dLoc;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.tv_phoneno);
            pLoc = itemView.findViewById(R.id.tv_pLocation);
            dLoc = itemView.findViewById(R.id.tv_dLocation);
            accept = itemView.findViewById(R.id.b_acceptRequest);
            name = itemView.findViewById(R.id.requestName);
        }
    }
}
