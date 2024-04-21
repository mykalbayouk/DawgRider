package edu.uga.cs.ridershareapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;

import java.util.List;

public class RideRecyclerAdapter extends  RecyclerView.Adapter<RideRecyclerAdapter.RideHolder>{
    private List<RideObject> rideList;
    private Context context;

    public RideRecyclerAdapter(List<RideObject> rideList, Context context) {
        this.rideList = rideList;
        this.context = context;
    }

    class RideHolder extends RecyclerView.ViewHolder {
        TextView destination;
        TextView origin;
        TextView date;

        public RideHolder(View itemView) {
            super(itemView);

            destination = itemView.findViewById(R.id.destination);
            origin = itemView.findViewById(R.id.origin);
            date = itemView.findViewById(R.id.date);
        }
    }

    @NonNull
    @Override
    public RideHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_object, parent, false);
        return new RideHolder(view);
    }

    @Override
    public void onBindViewHolder(RideHolder holder, int position) {
        RideObject ride = rideList.get(position);

        holder.destination.setText(ride.getDestination());
        holder.origin.setText(ride.getOrigin());
        holder.date.setText(ride.getDate());

        holder.itemView.setOnClickListener(v -> {
            EditDialogFragment dialog = EditDialogFragment.newInstance(position, ride.getDestination(), ride.getOrigin(), ride.getDate(), ride.getKey(), ride.getCreator());
            dialog.show(((HomePageActivity) context).getSupportFragmentManager(), "EditDialogFragment");
        });
    }



    @Override
    public int getItemCount() {
        return rideList.size();
    }
}
