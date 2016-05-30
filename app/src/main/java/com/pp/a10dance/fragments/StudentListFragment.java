package com.pp.a10dance.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.couchbase.lite.Database;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.android.AndroidContext;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.pp.a10dance.R;
import com.pp.a10dance.activity.DriveStudentImportActivity;
import com.pp.a10dance.activity.StudentListActivity;
import com.pp.a10dance.adapter.StudentAdapter;
import com.pp.a10dance.document.StudentRepository;

public class StudentListFragment extends Fragment {

    private Context context;
    private StudentAdapter mStudentAdapter;
    private StudentRepository mStudentRepository;
    private String mClassId;
    private Database mDatabase;

    public static Fragment createInstance(String classId) {
        StudentListFragment studentListFragment = new StudentListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(StudentListActivity.CLASS_ID_ARGS, classId);
        studentListFragment.setArguments(bundle);
        return studentListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassId = getArguments().getString(StudentListActivity.CLASS_ID_ARGS);
        mStudentRepository = new StudentRepository(new AndroidContext(
                getActivity()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_list, container, false);
        ListView studentListView = (ListView) view
                .findViewById(R.id.student_listView);
        LiveQuery liveQuery = mStudentRepository.getStudentInClassQuery(
                mClassId).toLiveQuery();
        mStudentAdapter = new StudentAdapter(context, liveQuery);
        studentListView.setAdapter(mStudentAdapter);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view
                .findViewById(R.id.add_student_actionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStudent();
            }
        });

        final FloatingActionButton importStudentButton = (FloatingActionButton) view
                .findViewById(R.id.import_student_actionButton);
        importStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importStudent();
            }
        });
        // todo: listen for remote changes
        return view;
    }

    private void importStudent() {
        // TODO: create a dialog to import from file system
        Intent intent = new Intent(getActivity(),
                DriveStudentImportActivity.class);
        startActivity(intent);

        // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // intent.setType("image/*");
        // //intent.setType("file/*");
        // // intent.setType("application/vnd.google-apps.spreadsheet");
        // startActivityForResult(intent, 123);
    }

    private void createStudent() {
        NewStudentDialogFragment newStudentDialogFragment = NewStudentDialogFragment
                .createNewStudentDialogFragment(mClassId);
        newStudentDialogFragment.show(
                getActivity().getSupportFragmentManager(), "newStudent");

    }

}
