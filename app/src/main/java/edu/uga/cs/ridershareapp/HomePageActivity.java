package edu.uga.cs.ridershareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity
    implements AddRideDialogFragment.AddRideDialogListener,
                EditDialogFragment.EditDialogListener

{
    private RecyclerView recyclerView;
    private RideRecyclerAdapter rideRecyclerAdapter;
    private List<RideObject> rideList;
    private FloatingActionButton addRideButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Ride Share");

        mAuth = FirebaseAuth.getInstance();

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                mAuth.signOut();
                Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });


        database = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.main_recycler_view);
        addRideButton = findViewById(R.id.main_add_ride);
        addRideButton.setOnClickListener(v -> {
            DialogFragment dialog = new AddRideDialogFragment();
            dialog.show(getSupportFragmentManager(), "AddRideDialogFragment");
        });

        rideList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rideRecyclerAdapter = new RideRecyclerAdapter(rideList, this);
        recyclerView.setAdapter(rideRecyclerAdapter);

        DatabaseReference ref = database.getReference("rides");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rideList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RideObject ride = snapshot.getValue(RideObject.class);
                    ride.setKey(snapshot.getKey());
                    Log.println(Log.INFO, "Ride", ride.toString());
                    rideList.add(ride);
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
    public void addRide(RideObject ride) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rides");

        myRef.push().setValue(ride)
                .addOnSuccessListener( aVoid -> {
                    recyclerView.post(() -> recyclerView.smoothScrollToPosition( rideList.size()-1 ));
                    // close the dialog
                    Toast.makeText(getApplicationContext(), "Ride added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener( e -> {
                    // close the dialog
                    Toast.makeText(getApplicationContext(), "Failed to add ride", Toast.LENGTH_SHORT).show();
                });


    }

    public void editRide (int position, RideObject ride, int action) {
        if (action == EditDialogFragment.SAVE) {

            rideRecyclerAdapter.notifyItemChanged(position);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("rides").child(ride.getKey());

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().setValue(ride)
                            .addOnSuccessListener(aVoid -> {
                                // close the dialog
                                Toast.makeText(getApplicationContext(), "Ride updated", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // close the dialog
                                Toast.makeText(getApplicationContext(), "Failed to update ride", Toast.LENGTH_SHORT).show();
                            });
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.out.println("ValueEventListener: reading failed: " + error.getMessage());
                }
            });

        } else if (action == EditDialogFragment.DELETE) {

            rideList.remove(position);
            rideRecyclerAdapter.notifyItemRemoved(position);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("rides").child(ride.getKey());

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().removeValue()
                            .addOnSuccessListener(aVoid -> {
                                // close the dialog
                                Toast.makeText(getApplicationContext(), "Ride deleted", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // close the dialog
                                Toast.makeText(getApplicationContext(), "Failed to delete ride", Toast.LENGTH_SHORT).show();
                            });
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.out.println("ValueEventListener: reading failed: " + error.getMessage());
                }
            });


        }
    }
}
