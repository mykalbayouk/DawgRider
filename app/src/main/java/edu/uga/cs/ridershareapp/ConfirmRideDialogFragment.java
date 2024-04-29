package edu.uga.cs.ridershareapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConfirmRideDialogFragment extends DialogFragment {
    private TextView dest_text;
    private TextView orig_text;
    private TextView date_text;

    String destination;
    String origin;
    String date;
    String key;

    String user;

    String acceptedBy;
    boolean offer;

    public interface ConfirmRideDialogListener {
        void confirmRide(int position, RideObject ride);
    }

    public static ConfirmRideDialogFragment newInstance(int position, String destination, String origin, String date, String key, String user, String acceptedBy, boolean accepted, boolean offer) {
        ConfirmRideDialogFragment dialog = new ConfirmRideDialogFragment();

        // Supply ride values as an argument.
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("key", key);
        args.putString("user", user);
        args.putString("destination", destination);
        args.putString("origin", origin);
        args.putString("date", date);
        args.putBoolean("offer", offer);
        args.putString("acceptedBy", acceptedBy);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        destination = getArguments().getString("destination");
        origin = getArguments().getString("origin");
        date = getArguments().getString("date");
        key = getArguments().getString("key");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_accept_ride_dialog,
                getActivity().findViewById(R.id.root_accept));

        dest_text = layout.findViewById(R.id.acc_dest);
        orig_text = layout.findViewById(R.id.acc_origin);
        date_text = layout.findViewById(R.id.acc_date);

        dest_text.setText(destination);
        orig_text.setText(origin);
        date_text.setText(date);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);

        builder.setTitle( "Confirm Ride" );

        builder.setNegativeButton( android.R.string.cancel, (dialog, whichButton) -> dialog.dismiss());

        builder.setPositiveButton( android.R.string.ok, new ConfirmRideListener() );

        return builder.create();
    }

    private class ConfirmRideListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            ConfirmRideDialogListener listener = (ConfirmRideDialogListener) getActivity();
            RideObject ride = new RideObject(destination, origin, date, user, acceptedBy, true, offer);
            ride.setKey(key);
            listener.confirmRide(getArguments().getInt("position"), ride);
            dialog.dismiss();

        }
    }

}