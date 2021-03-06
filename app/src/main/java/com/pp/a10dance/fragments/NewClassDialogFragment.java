package com.pp.a10dance.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.android.AndroidContext;
import com.pp.a10dance.R;
import com.pp.a10dance.document.ProfClassRepository;
import com.pp.a10dance.helper.LogUtils;
import com.pp.a10dance.model.ProfClass;

public class NewClassDialogFragment extends DialogFragment {

    private ProfClassRepository profClassRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profClassRepository = new ProfClassRepository(new AndroidContext(
                getActivity()));

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Class");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View newClassView = inflater.inflate(R.layout.new_class_layout,
                null);
        final TextView classTitleTextView = (TextView) newClassView
                .findViewById(R.id.class_name_editText);
        final TextView classDescriptionTextView = (TextView) newClassView
                .findViewById(R.id.class_description_editText);

        builder.setView(newClassView).setPositiveButton("Create",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewClassDialogFragment.this.getDialog().cancel();
                    }
                });

        final AlertDialog d = builder.create();
        // Don't dismiss the dialog. So use a show listener which takes care of
        // this
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = d.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (classTitleTextView.getText() == null
                                || classTitleTextView.getText().toString()
                                        .isEmpty()) {
                            classTitleTextView.setError("Title is required");
                        } else {
                            createNewClass(classTitleTextView.getText()
                                    .toString(), classDescriptionTextView
                                    .getText().toString());

                            d.cancel();
                        }
                    }
                });
            }
        });
        return d;
    }

    private void createNewClass(String title, String description) {
        try {
            ProfClass profClass = profClassRepository
                    .createNewProfClass(new ProfClass(title, description));

            FragmentManager fragmentManager = getActivity()
                    .getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.container,
                            ClassDetailsFragment.createInstance(profClass
                                    .get_id())).commit();
        } catch (CouchbaseLiteException e) {
            Log.e(LogUtils.getTag(NewClassDialogFragment.class),
                    e.getMessage(), e);
            Toast.makeText(getActivity(), "Failed to create class",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
