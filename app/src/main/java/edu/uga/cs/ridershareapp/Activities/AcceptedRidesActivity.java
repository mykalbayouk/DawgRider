package edu.uga.cs.ridershareapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.ridershareapp.ConfirmRideDialogFragment;
import edu.uga.cs.ridershareapp.MainActivity;
import edu.uga.cs.ridershareapp.R;
import edu.uga.cs.ridershareapp.RideObject;
import edu.uga.cs.ridershareapp.RideRecyclerAdapter;

public class AcceptedRidesActivity extends AppCompatActivity implements ConfirmRideDialogFragment.ConfirmRideDialogListener {

    private FirebaseAuth mAuth;

    private List<RideObject> acceptedRides;
    private FirebaseDatabase database;

    private RecyclerView recyclerView;
    private RideRecyclerAdapter rideRecyclerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_rides);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        toolbar.setTitle("Accepted Rides");
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                mAuth.signOut();
                HomePageActivity.isDriver = false;
                HomePageActivity.onAccepted = false;
                Intent intent = new Intent(AcceptedRidesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.past_rides) {
                HomePageActivity.onAccepted = false;
                Intent intent = new Intent(AcceptedRidesActivity.this, PastRidesActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.accepted_rides) {
                Toast toast = Toast.makeText(getApplicationContext(), "You are already in Accepted Rides", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            } else if (item.getItemId() == R.id.home_screen) {
                HomePageActivity.onAccepted = false;
                Intent intent = new Intent(AcceptedRidesActivity.this, HomePageActivity.class);
                startActivity(intent);
                return true;
            }


            return false;
        });

        database = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.main_recycler_view);

        acceptedRides = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rideRecyclerAdapter = new RideRecyclerAdapter(acceptedRides, this);
        recyclerView.setAdapter(rideRecyclerAdapter);

        DatabaseReference ref = database.getReference("rides");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                acceptedRides.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RideObject ride = snapshot.getValue(RideObject.class);
                    ride.setKey(snapshot.getKey());
                    if (ride.getAccepted() && (ride.getCreator().equals(mAuth.getUid()) || ride.getAcceptedBy().equals(mAuth.getUid()))) {
                        acceptedRides.add(ride);
                    }
                }
                rideRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println( "ValueEventListener: reading failed: " + error.getMessage() );
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navbar, menu);
        return true;
    }

    @Override
    public void confirmRide(int position, RideObject ride) {
        acceptedRides.remove(position);
        rideRecyclerAdapter.notifyItemRemoved(position);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rides").child(ride.getKey());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue()
                        .addOnSuccessListener(aVoid -> {
                            // close the dialog
                            Toast.makeText(getApplicationContext(), "Ride Confirmed", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // close the dialog
                            Toast.makeText(getApplicationContext(), "Failed to confirm ride", Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("ValueEventListener: reading failed: " + error.getMessage());
            }
        });
    }
}