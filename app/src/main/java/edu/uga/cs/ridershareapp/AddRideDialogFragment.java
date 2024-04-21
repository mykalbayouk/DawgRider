package edu.uga.cs.ridershareapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;


public class AddRideDialogFragment extends DialogFragment {

    private EditText destinationView;
    private EditText originView;
    private EditText dateView;

    public interface AddRideDialogListener {
        void addRide(RideObject ride);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create the AlertDialog view
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_add_ride_dialog,
                                             getActivity().findViewById(R.id.root));
        layout.setPadding(40, 40, 40, 40);
        // get the view objects Fin the AlertDialog
        destinationView = layout.findViewById( R.id.dialog_dest );
        originView = layout.findViewById( R.id.dialog_origin );
        dateView = layout.findViewById( R.id.dialog_date );

        // create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        // Set its view (inflated above).
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle( "New Ride" );
        // Provide the negative button listener
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });

        // Provide the positive button listener
        builder.setPositiveButton( android.R.string.ok, new AddRideListener() );

        return builder.create();
    }

    private class AddRideListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // get the new job lead data from the user
            String destination = destinationView.getText().toString();
            String origin = originView.getText().toString();
            String date = dateView.getText().toString();

            // create a new JobLead object
            RideObject ride = new RideObject(destination, origin, date, FirebaseAuth.getInstance().getUid());

            // get the Activity's listener to add the new job lead
            AddRideDialogListener listener = (AddRideDialogListener) getActivity();


            // add the new job lead
            listener.addRide(ride);

            // close the dialog
            dismiss();
        }
    }


}