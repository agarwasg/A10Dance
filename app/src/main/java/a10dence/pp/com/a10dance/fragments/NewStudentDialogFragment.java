package a10dence.pp.com.a10dance.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;

import a10dence.pp.com.a10dance.R;
import a10dence.pp.com.a10dance.activity.StudentListActivity;
import a10dence.pp.com.a10dance.document.Student;
import a10dence.pp.com.a10dance.helper.LogUtils;

public class NewStudentDialogFragment extends DialogFragment {

    private Database mDatabase;


    public static NewStudentDialogFragment createNewStudentDialogFragment(String classID) {
        NewStudentDialogFragment newStudentDialogFragment = new NewStudentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(StudentListActivity.CLASS_ID_ARGS, classID);
        newStudentDialogFragment.setArguments(bundle);
        return newStudentDialogFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String classId = getArguments().getString(StudentListActivity.CLASS_ID_ARGS);
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
                            studentNameTextView.setError("Student Name is required");
                        } else {
                            createNewStudent(classId, studentNameTextView.getText()
                                    .toString(), rollNumberTextView
                                    .getText().toString(), emailTextView.getText().toString(), phoneTextView.getText().toString());

                            d.cancel();
                        }
                    }
                });
            }
        });
        return d;


    }

    private void createNewStudent(String classId, String name, String rollNumber, String email, String phoneNumber) {
        if (mDatabase != null) {
            try {
                Document document = Student.createNewStudent(mDatabase, classId,
                        name, rollNumber, email, phoneNumber);

            } catch (CouchbaseLiteException e) {
                Log.e(LogUtils.getTag(NewStudentDialogFragment.class),
                        e.getMessage(), e);
                Toast.makeText(getActivity(), "Failed to create new student",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Database not found",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void setDatabase(Database database) {
        this.mDatabase = database;
    }
}
