package edu.uga.cs.ridershareapp;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.uga.cs.ridershareapp.Activities.AcceptedRidesActivity;
import edu.uga.cs.ridershareapp.Activities.HomePageActivity;

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

        TextView offerType;

        CardView cardView;

        public RideHolder(View itemView) {
            super(itemView);

            destination = itemView.findViewById(R.id.destination);
            origin = itemView.findViewById(R.id.origin);
            date = itemView.findViewById(R.id.date);
            offerType = itemView.findViewById(R.id.offer_type);
            cardView = itemView.findViewById(R.id.card_view);
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
        if (ride.getOffer()) {
            holder.offerType.setText("Driver Offer");
        } else {
            holder.offerType.setText("Rider Request");
        }


        boolean isOwn = FirebaseAuth.getInstance().getUid() != null && FirebaseAuth.getInstance().getUid().equals(ride.getCreator());
        holder.itemView.setOnClickListener(v -> {
            if (!ride.getAccepted()) {
                if (HomePageActivity.isDriver && !isOwn) {
                    if (!ride.getOffer()) {
                        AcceptRideDialogFragment dialog = AcceptRideDialogFragment.newInstance(position, ride.getDestination(), ride.getOrigin(), ride.getDate(), ride.getKey(), ride.getCreator(),ride.getAcceptedBy(), ride.getAccepted(), ride.getOffer());
                        dialog.show(((HomePageActivity) context).getSupportFragmentManager(), "AcceptRideDialogFragment");
                    } else {
                        Toast toast = Toast.makeText(context, "Cannot accept Ride Request as Driver", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else if (isOwn) {
                    EditDialogFragment dialog = EditDialogFragment.newInstance(position, ride.getDestination(), ride.getOrigin(), ride.getDate(), ride.getKey(), ride.getCreator(), ride.getAcceptedBy(), ride.getAccepted(), ride.getOffer());
                    dialog.show(((HomePageActivity) context).getSupportFragmentManager(), "EditDialogFragment");
                } else {
                    if (ride.getOffer()) {
                        AcceptRideDialogFragment dialog = AcceptRideDialogFragment.newInstance(position, ride.getDestination(), ride.getOrigin(), ride.getDate(), ride.getKey(), ride.getCreator(), ride.getAcceptedBy(), ride.getAccepted(), ride.getOffer());
                        dialog.show(((HomePageActivity) context).getSupportFragmentManager(), "AcceptRideDialogFragment");
                    } else {
                        Toast toast = Toast.makeText(context, "Cannot accept Ride Request as Rider", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            } else {
                if (HomePageActivity.onAccepted) {
                    ConfirmRideDialogFragment dialog = ConfirmRideDialogFragment.newInstance(position, ride.getDestination(), ride.getOrigin(), ride.getDate(), ride.getKey(), ride.getCreator(),ride.getAcceptedBy(), ride.getAccepted(), ride.getOffer());
                    dialog.show(((AcceptedRidesActivity) context).getSupportFragmentManager(), "ConfirmRideDialogFragment");
                } else {
                    Toast toast = Toast.makeText(context, "Cannot accept multiple rides", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return rideList.size();
    }
}
