package edu.uga.cs.ridershareapp;

import android.app.Dialog;
import android.widget.EditText;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;


import androidx.fragment.app.DialogFragment;

public class EditDialogFragment extends DialogFragment {

    public static final int SAVE = 1;   // update an existing ride
    public static final int DELETE = 2; // delete an existing ride
    private EditText destinationView;
    private EditText originView;
    private EditText dateView;

    int position;     // the position of the edited Ride on the list of rides
    String destination;
    String origin;
    String date;
    String key;
    String user;

    boolean offer;

    boolean accepted;

    public interface EditDialogListener {
        void editRide(int position, RideObject ride, int action);
    }

    public static EditDialogFragment newInstance(int position, String destination, String origin, String date, String key, String user, boolean accepted, boolean offer) {
        EditDialogFragment dialog = new EditDialogFragment();

        // Supply ride values as an argument.
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("key", key);
        args.putString("user", user);
        args.putString("destination", destination);
        args.putString("origin", origin);
        args.putString("date", date);
        args.putBoolean("accepted", accepted);
        args.putBoolean("offer", offer);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        position = getArguments().getInt("position");
        key = getArguments().getString("key");
        user = getArguments().getString("user");
        destination = getArguments().getString("destination");
        origin = getArguments().getString("origin");
        date = getArguments().getString("date");
        accepted = getArguments().getBoolean("accepted");
        offer = getArguments().getBoolean("offer");
        // Create the AlertDialog view
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_add_ride_dialog,
                                             getActivity().findViewById(R.id.root_accept));
        layout.setPadding(40, 40, 40, 40);
        // get the view objects Fin the AlertDialog
        destinationView = layout.findViewById( R.id.dialog_dest );
        originView = layout.findViewById( R.id.dialog_origin );
        dateView = layout.findViewById( R.id.dialog_date );

        destinationView.setText(destination);
        originView.setText(origin);
        dateView.setText(date);

        // create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        // Set its view (inflated above).
        builder.setView(layout);

        // Set the title of the AlertDialog
        builder.setTitle( "Edit Ride" );
        // Provide the negative button listener
        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });

        // Provide the positive button listener
        builder.setPositiveButton("Save", new SaveButtonClickListener() );
        builder.setNeutralButton("Delete", new DeleteButtonClickListener() );

        return builder.create();
    }

private class SaveButtonClickListener implements DialogInterface.OnClickListener {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        // get the new job lead data from the user
        String destination = destinationView.getText().toString();
        String origin = originView.getText().toString();
        String date = dateView.getText().toString();

        // create a new JobLead object
        RideObject ride = new RideObject(destination, origin, date, user, accepted, offer);
        ride.setKey(key);
        // get the Activity's listener to add the new job lead
        EditDialogListener listener = (EditDialogListener) getActivity();

        // add the new job lead
        listener.editRide(position, ride, SAVE);

        // close the dialog
        dismiss();
    }
}

        private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // get the new job lead data from the user
                RideObject ride = new RideObject(destination, origin, date, user, accepted, offer);
                ride.setKey(key);
                // get the Activity's listener to add the new job lead
                EditDialogListener listener = (EditDialogListener) getActivity();

                // add the new job lead
                listener.editRide(position, ride, DELETE);

                // close the dialog
                dismiss();
            }

        }
    }

