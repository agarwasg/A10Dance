package com.pp.a10dance.fragments;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;
import com.pp.a10dance.R;
import com.pp.a10dance.activity.AttendanceActivity;
import com.pp.a10dance.adapter.AttendanceAdapter;
import com.pp.a10dance.document.AttendanceRepository;
import com.pp.a10dance.document.StudentAttendanceRepository;
import com.pp.a10dance.document.StudentRepository;
import com.pp.a10dance.helper.Utils;
import com.pp.a10dance.model.Attendance;
import com.pp.a10dance.model.Student;
import com.pp.a10dance.model.StudentAttendance;

public class AttendanceFragment extends Fragment {

    private String mClassId;
    private String mAttendanceId;
    private Activity mContext;
    private AttendanceAdapter mAttendanceAdapter;
    private AttendanceRepository mAttendanceRepository;
    private StudentAttendanceRepository mStudentAttendanceRepository;
    private StudentRepository mStudentRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassId = getActivity().getIntent().getStringExtra(
                AttendanceActivity.CLASS_ID_ARGS);
        mAttendanceId = getActivity().getIntent().getStringExtra(
                AttendanceActivity.ATTENDANCE_ID_ARGS);
        mAttendanceRepository = new AttendanceRepository(new AndroidContext(
                getActivity()));
        mStudentAttendanceRepository = new StudentAttendanceRepository(
                new AndroidContext(getActivity()));
        mStudentRepository = new StudentRepository(new AndroidContext(
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.attendance_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.attendance_list);
        LiveQuery liveQuery = mStudentRepository.getQuery(mClassId)
                .toLiveQuery();
        mAttendanceAdapter = new AttendanceAdapter(mContext, liveQuery,
                mAttendanceId);
        listView.setAdapter(mAttendanceAdapter);
        return view;

    }

    private boolean saveAttendance() {

        Attendance attendance;
        boolean isNewAttendance = false;
        // create new attendance if we are not editing
        if (Utils.StringUtils.isBlank(mAttendanceId)) {
            attendance = mAttendanceRepository.save(new Attendance(mClassId));
            isNewAttendance = true;
        } else {
            attendance = mAttendanceRepository.get(mAttendanceId);
        }
        if (attendance == null) {
            Toast.makeText(getActivity(), "invalid attendance Id",
                    Toast.LENGTH_SHORT).show();
            return true;
        }

        if (mAttendanceAdapter != null) {
            Map<String, Boolean> attendanceMap = new HashMap<>();
            // get all students for the class
            try {
                QueryEnumerator result = mStudentRepository.getQuery(mClassId)
                        .run();
                Student student;
                QueryRow row;
                StudentAttendance studentAttendance;
                boolean isPresent = true;
                for (Iterator<QueryRow> it = result; it.hasNext();) {
                    row = it.next();
                    student = mStudentRepository.documentToObject(
                            row.getDocument(), Student.class);
                    // if new attendance mark everyone as present
                    if (!isNewAttendance) {
                        isPresent = mStudentAttendanceRepository
                                .getCurrentAttendanceState(student.get_id(),
                                        mAttendanceId);
                    }
                    attendanceMap.put(student.get_id(), isPresent);
                }
                // super impose by latest update from user
                attendanceMap.putAll(mAttendanceAdapter.getAttendanceMap());
                countFinalValues(attendanceMap);
                // TODO: to show conformation dialog
                for (Map.Entry<String, Boolean> entry : attendanceMap
                        .entrySet()) {
                    // TODO: Handle scenariooes where it is in update for
                    // studentattendance
                    mStudentAttendanceRepository.save(new StudentAttendance(
                            attendance.get_id(), entry.getKey(), mClassId,
                            entry.getValue()));
                    getActivity().finish();
                }

            } catch (CouchbaseLiteException e) {
                Log.e("AttendanceFragment", e.getMessage(), e);
                return false;
            }
        }
        return true;
    }

    private void countFinalValues(Map<String, Boolean> valueMap) {
        Map<Boolean, Integer> result = new TreeMap<Boolean, Integer>();
        for (Map.Entry<String, Boolean> entry : valueMap.entrySet()) {
            boolean value = entry.getValue();
            Integer count = result.get(value);
            if (count == null)
                result.put(value, new Integer(1));
            else
                result.put(value, new Integer(count + 1));
        }
        Log.d("A10dance", "values changed " + result);
    }
}
