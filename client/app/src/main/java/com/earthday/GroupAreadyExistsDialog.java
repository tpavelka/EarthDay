package com.earthday;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class GroupAreadyExistsDialog extends DialogFragment {
    public static GroupAreadyExistsDialog newInstance(int title) {
        GroupAreadyExistsDialog frag = new GroupAreadyExistsDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_alreadyexists)
                .setPositiveButton(R.string.dialog_okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }
}
