package edu.uga.cs.ridershareapp.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.uga.cs.ridershareapp.R;
import edu.uga.cs.ridershareapp.Activities.HomePageActivity;
import edu.uga.cs.ridershareapp.UserObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passworEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById( R.id.reg_email);
        passworEditText = findViewById( R.id.reg_password);

        Button registerButton = findViewById(R.id.reg_register);
        registerButton.setOnClickListener( new RegisterButtonClickListener() );
    }

    private class RegisterButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final String email = emailEditText.getText().toString();
            final String password = passworEditText.getText().toString();


            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usersRef = database.getReference("users");

            firebaseAuth.createUserWithEmailAndPassword( email, password )
                    .addOnCompleteListener( RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText( getApplicationContext(),
                                               "User created successfully",
                                               Toast.LENGTH_SHORT).show();
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                UserObject newUser = new UserObject(email, 50);
                                newUser.setKey(firebaseAuth.getUid());

                                usersRef.child(firebaseAuth.getUid()).setValue(newUser);

                                Intent intent = new Intent(RegisterActivity.this, HomePageActivity.class);
                                startActivity(intent);


                            } else {
                                Toast.makeText( getApplicationContext(),
                                               "User creation failed",
                                               Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}