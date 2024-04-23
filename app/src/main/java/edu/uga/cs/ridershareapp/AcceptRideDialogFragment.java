package edu.uga.cs.ridershareapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class AcceptRideDialogFragment extends DialogFragment {

    private TextView dest_text;
    private TextView orig_text;
    private TextView date_text;

    String destination;
    String origin;
    String date;
    String key;

    String user;
    boolean offer;


    public interface AcceptRideDialogListener {
        void acceptRide(int position, RideObject ride);
    }

    public static AcceptRideDialogFragment newInstance(int position, String destination, String origin, String date, String key, String user, boolean accepted, boolean offer) {
        AcceptRideDialogFragment dialog = new AcceptRideDialogFragment();

        // Supply ride values as an argument.
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("key", key);
        args.putString("user", user);
        args.putString("destination", destination);
        args.putString("origin", origin);
        args.putString("date", date);
        args.putBoolean("offer", offer);
        dialog.setArguments(args);

        return dialog;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        destination = getArguments().getString("destination");
        origin = getArguments().getString("origin");
        date = getArguments().getString("date");
        key = getArguments().getString("key");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_accept_ride_dialog,
                getActivity().findViewById(R.id.root_accept));

        layout.setPadding(40, 40, 40, 40);
        // get the view objects Fin the AlertDialog
        dest_text = layout.findViewById( R.id.acc_dest );
        orig_text = layout.findViewById( R.id.acc_origin);
        date_text = layout.findViewById( R.id.acc_date);

        dest_text.setText(destination);
        orig_text.setText(origin);
        date_text.setText(date);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setView(layout);

        builder.setTitle( "Accept Ride" );
        builder.setNegativeButton( android.R.string.cancel, (dialog, whichButton) -> dialog.dismiss());

        builder.setPositiveButton( android.R.string.ok, new AcceptRideListener() );

        return builder.create();

    }

    private class AcceptRideListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String destination = dest_text.getText().toString();
            String origin = orig_text.getText().toString();
            String date = date_text.getText().toString();

            RideObject ride = new RideObject(destination, origin, date, user, true, offer);
            ride.setKey(key);
            AcceptRideDialogListener listener = (AcceptRideDialogListener) getActivity();

            listener.acceptRide(getArguments().getInt("position"), ride);

            dialog.dismiss();
        }
    }
}