package a10dence.pp.com.a10dance.fragments;

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
import com.getbase.floatingactionbutton.FloatingActionButton;

import a10dence.pp.com.a10dance.A10danceApplication;
import a10dence.pp.com.a10dance.R;
import a10dence.pp.com.a10dance.activity.StudentListActivity;
import a10dence.pp.com.a10dance.adapter.StudentAdapter;
import a10dence.pp.com.a10dance.document.Student;

public class StudentListFragment extends Fragment {

    private Context context;
    private StudentAdapter mStudentAdapter;
    private String mClassId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassId = getActivity().getIntent().getStringExtra(StudentListActivity.CLASS_ID_ARGS);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_list, container, false);
        ListView studentListView = (ListView) view.findViewById(R.id.student_listView);
        LiveQuery liveQuery = Student.getQuery(getDatabase(), mClassId).toLiveQuery();
        mStudentAdapter = new StudentAdapter(context, liveQuery);
        studentListView.setAdapter(mStudentAdapter);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_student_actionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStudent();
            }
        });

        final FloatingActionButton importStudentButton = (FloatingActionButton) view.findViewById(R.id.import_student_actionButton);
        importStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importStudent();
            }
        });
        //todo: listen for remote changes
        return view;
    }

    private void importStudent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //intent.setType("file/*");
//        intent.setType("application/vnd.google-apps.spreadsheet");
        startActivityForResult(intent, 123);
    }

    private Database getDatabase() {
        A10danceApplication application = (A10danceApplication) getActivity()
                .getApplication();
        return application.getDatabase();
    }

    private void createStudent() {
        NewStudentDialogFragment newStudentDialogFragment = NewStudentDialogFragment.createNewStudentDialogFragment(mClassId);
        newStudentDialogFragment.setDatabase(getDatabase());
        newStudentDialogFragment.show(getActivity().getSupportFragmentManager(), "newStudent");

    }

}
