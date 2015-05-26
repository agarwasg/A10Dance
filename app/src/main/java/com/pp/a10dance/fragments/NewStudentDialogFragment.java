package com.pp.a10dance.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.couchbase.lite.android.AndroidContext;
import com.pp.a10dance.R;
import com.pp.a10dance.activity.StudentListActivity;
import com.pp.a10dance.document.StudentRepository;
import com.pp.a10dance.model.Student;

public class NewStudentDialogFragment extends DialogFragment {

    public static NewStudentDialogFragment createNewStudentDialogFragment(
            String classID) {
        NewStudentDialogFragment newStudentDialogFragment = new NewStudentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(StudentListActivity.CLASS_ID_ARGS, classID);
        newStudentDialogFragment.setArguments(bundle);
        return newStudentDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String classId = getArguments().getString(
                StudentListActivity.CLASS_ID_ARGS);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Student");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View newClassView = inflater.inflate(R.layout.new_student_layout,
                null);
        final TextView studentNameTextView = (TextView) newClassView
                .findViewById(R.id.student_name_editText);
        final TextView rollNumberTextView = (TextView) newClassView
                .findViewById(R.id.student_roll_number_editText);
        final TextView emailTextView = (TextView) newClassView
                .findViewById(R.id.student_email_editText);
        final TextView phoneTextView = (TextView) newClassView
                .findViewById(R.id.student_phone_editText);

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
                        NewStudentDialogFragment.this.getDialog().cancel();
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
                        if (studentNameTextView.getText() == null
                                || studentNameTextView.getText().toString()
                                        .isEmpty()) {
                            studentNameTextView
                                    .setError("Student Name is required");
                        } else {
                            createNewStudent(classId, studentNameTextView
                                    .getText().toString(), rollNumberTextView
                                    .getText().toString(), emailTextView
                                    .getText().toString(), phoneTextView
                                    .getText().toString());

                            d.cancel();
                        }
                    }
                });
            }
        });
        return d;

    }

    private void createNewStudent(String classId, String name,
            String rollNumber, String email, String phoneNumber) {
        Student student = new Student(classId, phoneNumber, email, rollNumber,
                name);
        StudentRepository studentRepository = new StudentRepository(
                new AndroidContext(getActivity()));
        studentRepository.save(student);
    }

}
