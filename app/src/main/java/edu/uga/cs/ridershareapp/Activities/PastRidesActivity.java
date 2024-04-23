package edu.uga.cs.ridershareapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import edu.uga.cs.ridershareapp.MainActivity;
import edu.uga.cs.ridershareapp.R;

public class PastRidesActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_rides);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        toolbar.setTitle("Past Rides");
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                mAuth.signOut();
                HomePageActivity.onAccepted = false;
                HomePageActivity.isDriver = false;
                Intent intent = new Intent(PastRidesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.past_rides) {
                Toast toast = Toast.makeText(getApplicationContext(), "You are already in Past Rides", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            } else if (item.getItemId() == R.id.accepted_rides) {
                HomePageActivity.onAccepted = true;
                Intent intent = new Intent(PastRidesActivity.this, AcceptedRidesActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.home_screen) {
                HomePageActivity.onAccepted = false;
                Intent intent = new Intent(PastRidesActivity.this, HomePageActivity.class);
                startActivity(intent);
                return true;
            }


            return false;
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navbar, menu);
        return true;
    }
}