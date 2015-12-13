package com.pp.a10dance.fragments;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.couchbase.lite.Database;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.android.AndroidContext;
import com.pp.a10dance.A10danceDB;
import com.pp.a10dance.R;
import com.pp.a10dance.activity.AttendanceActivity;
import com.pp.a10dance.adapter.AttendanceAdapter;
import com.pp.a10dance.document.AttendanceRepository;
import com.pp.a10dance.document.StudentRepository;
import com.pp.a10dance.helper.Utils;
import com.pp.a10dance.model.Attendance;
import com.pp.a10dance.model.Student;

/**
 * Created by saketagarwal on 4/22/15.
 */
public class AttendanceFragment extends Fragment {

    private String mClassId;
    private String mAttendanceId;
    private Activity mContext;
    private AttendanceAdapter mAttendanceAdapter;
    private AttendanceRepository mAttendanceRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassId = getActivity().getIntent().getStringExtra(
                AttendanceActivity.CLASS_ID_ARGS);
        mAttendanceId = getActivity().getIntent().getStringExtra(
                AttendanceActivity.ATTENDANCE_ID_ARGS);
        mAttendanceRepository = new AttendanceRepository(new AndroidContext(
                getActivity()));
        setHasOptionsMenu(true);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.attendance, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_attendance) {
            return saveAttendance();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean saveAttendance() {
        Attendance attendance;

        // create new attendance if we are not editing
        if (Utils.StringUtils.isBlank(mAttendanceId)) {
            attendance = mAttendanceRepository.save(new Attendance(mClassId));
        } else {
            attendance = mAttendanceRepository.get(mAttendanceId);
        }
        if (attendance == null) {
            Toast.makeText(getActivity(), "invalid attendance Id",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if (mAttendanceAdapter != null) {
            int count = mAttendanceAdapter.getCount();
            Map<Student, Boolean> attendanceMap = mAttendanceAdapter.attendanceMap;

        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.attendance_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.attendance_list);
        LiveQuery liveQuery = StudentRepository.getQuery(getDatabase(),
                mClassId).toLiveQuery();
        mAttendanceAdapter = new AttendanceAdapter(mContext, liveQuery,
                mAttendanceId);
        listView.setAdapter(mAttendanceAdapter);
        return view;

    }

    private Database getDatabase() {
        return A10danceDB.getInstance(new AndroidContext(getActivity()))
                .getDatabase();
    }

}
