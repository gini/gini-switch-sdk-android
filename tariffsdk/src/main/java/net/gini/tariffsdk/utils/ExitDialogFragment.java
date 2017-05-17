package net.gini.tariffsdk.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import net.gini.tariffsdk.R;

public class ExitDialogFragment extends DialogFragment {

    private static final String BUNDLE_KEY_TITLE = "BUNDLE_KEY_TITLE";
    private ExitDialogListener mListener;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        try {
            mListener = (ExitDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ExitDialogListener");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ExitDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ExitDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int title = getArguments().getInt(BUNDLE_KEY_TITLE);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(title)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onPositive();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onNegative();
                    }
                });
        return builder.create();
    }

    public static ExitDialogFragment newInstance(final int title) {

        Bundle args = new Bundle();
        args.putInt(BUNDLE_KEY_TITLE, title);

        ExitDialogFragment fragment = new ExitDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface ExitDialogListener {

        void onNegative();

        void onPositive();
    }
}