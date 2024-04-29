package edu.uga.cs.ridershareapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import edu.uga.cs.ridershareapp.ConfirmRideDialogFragment;
import edu.uga.cs.ridershareapp.MainActivity;
import edu.uga.cs.ridershareapp.R;
import edu.uga.cs.ridershareapp.RideObject;
import edu.uga.cs.ridershareapp.RideRecyclerAdapter;
import edu.uga.cs.ridershareapp.UserObject;

public class AcceptedRidesActivity extends AppCompatActivity implements ConfirmRideDialogFragment.ConfirmRideDialogListener {

    private FirebaseAuth mAuth;

    private List<RideObject> acceptedRides;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
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
                Collections.sort(acceptedRides, new Comparator<RideObject>() {
                    @Override
                    public int compare(RideObject o1, RideObject o2) {
                        try {
                            Log.d("SortDate", "Comparing: " + o1.getDate() + " with " + o2.getDate());
                            Date date1 = dateFormat.parse(o1.getDate());
                            Date date2 = dateFormat.parse(o2.getDate());
                            int result = date2.compareTo(date1);
                            Log.d("SortDate", "Result: " + result);
                            return result;
                        } catch (ParseException e) {
                            Log.e("RideRecyclerAdapter", "Error parsing date", e);
                            return 0;
                        }
                    }
                });
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
        // acceptedRides.remove(position);
        // rideRecyclerAdapter.notifyItemRemoved(position);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rides").child(ride.getKey());
        DatabaseReference usersRef = database.getReference("users");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RideObject updatedRide = dataSnapshot.getValue(RideObject.class);
                if (updatedRide == null) return;

                // Check if the current user is the driver or the rider
                boolean isDriver = FirebaseAuth.getInstance().getUid().equals(updatedRide.getCreator());

                // Update the corresponding confirmation field
                if (isDriver) {
                    updatedRide.setDriverConfirmed(true);
                } else {
                    updatedRide.setRiderConfirmed(true);
                }

                if (updatedRide.isDriverConfirmed() && updatedRide.isRiderConfirmed()) {
                    // driver offer
                    if (updatedRide.getOffer()) {
                        String driver = updatedRide.getCreator();
                        String rider = updatedRide.getAcceptedBy();
                        usersRef.child(driver).child("points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DataSnapshot dataSnapshot = task.getResult();
                                    if (dataSnapshot.exists()) {
                                        Integer points = dataSnapshot.getValue(Integer.class);
                                        System.out.println("Driver Points: " + points);
                                        usersRef.child(driver).child("points").setValue(points+50);
                                    } else {
                                        System.out.println("No points data available for the driver.");
                                    }
                                } else {
                                    System.out.println("Failed to get points: " + task.getException());
                                }
                            }
                        });
                        usersRef.child(rider).child("points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DataSnapshot dataSnapshot = task.getResult();
                                    if (dataSnapshot.exists()) {
                                        Integer points = dataSnapshot.getValue(Integer.class);
                                        System.out.println("Rider Points: " + points);
                                        usersRef.child(rider).child("points").setValue(points-50);
                                    } else {
                                        System.out.println("No points data available for the rider.");
                                    }
                                } else {
                                    System.out.println("Failed to get points: " + task.getException());
                                }
                            }
                        });
                    } else {
                        String rider = updatedRide.getCreator();
                        String driver = updatedRide.getAcceptedBy();
                        usersRef.child(driver).child("points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DataSnapshot dataSnapshot = task.getResult();
                                    if (dataSnapshot.exists()) {
                                        Integer points = dataSnapshot.getValue(Integer.class);
                                        System.out.println("Driver Points: " + points);
                                        usersRef.child(driver).child("points").setValue(points+50);
                                    } else {
                                        System.out.println("No points data available for the driver.");
                                    }
                                } else {
                                    System.out.println("Failed to get points: " + task.getException());
                                }
                            }
                        });
                        usersRef.child(rider).child("points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DataSnapshot dataSnapshot = task.getResult();
                                    if (dataSnapshot.exists()) {
                                        Integer points = dataSnapshot.getValue(Integer.class);
                                        System.out.println("Rider Points: " + points);
                                        usersRef.child(rider).child("points").setValue(points-50);
                                    } else {
                                        System.out.println("No points data available for the rider.");
                                    }
                                } else {
                                    System.out.println("Failed to get points: " + task.getException());
                                }
                            }
                        });
                    }

                    dataSnapshot.getRef().removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Ride Confirmed", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to confirm ride", Toast.LENGTH_SHORT).show());
                    acceptedRides.remove(position);
                    rideRecyclerAdapter.notifyItemRemoved(position);
                } else {
                    // Update the database with the new confirmation status
                    dataSnapshot.getRef().setValue(updatedRide);
                }

                /*
                dataSnapshot.getRef().removeValue()
                        .addOnSuccessListener(aVoid -> {
                            // close the dialog
                            Toast.makeText(getApplicationContext(), "Ride Confirmed", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // close the dialog
                            Toast.makeText(getApplicationContext(), "Failed to confirm ride", Toast.LENGTH_SHORT).show();
                        });
                 */
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("ValueEventListener: reading failed: " + error.getMessage());
            }
        });
    }
}