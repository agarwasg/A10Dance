package com.pp.a10dance.adapter;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.couchbase.lite.Document;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.android.AndroidContext;
import com.pp.a10dance.R;
import com.pp.a10dance.document.StudentAttendanceRepository;
import com.pp.a10dance.document.StudentRepository;
import com.pp.a10dance.model.Student;
import com.pp.a10dance.view.AttendanceCard;

/**
 * Created by saketagarwal on 5/16/15.
 */
public class AttendanceAdapter extends LiveQueryBaseAdapter implements
        AttendanceCard.OnAttendanceClickListener {

    public final Map<String, Boolean> attendanceMap;
    private final Context mContext;
    private final StudentRepository studentRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;
    private String attendanceId;

    public AttendanceAdapter(Context context, LiveQuery liveQuery,
            String attendanceId) {
        super(context, liveQuery);
        this.mContext = context;
        this.attendanceId = attendanceId;
        studentRepository = new StudentRepository(new AndroidContext(context));
        studentAttendanceRepository = new StudentAttendanceRepository(
                new AndroidContext(context));
        attendanceMap = new HashMap<>();
    }

    public Map<String, Boolean> getAttendanceMap() {
        return attendanceMap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        AttendanceCard attendanceCard;
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(
                    R.layout.attendance_list_item, parent, false);
        }
        Student student = studentRepository.get(((Document) getItem(position))
                .getId());
        attendanceCard = (AttendanceCard) itemView
                .findViewById(R.id.attendance_card);
        attendanceCard.setDetails(
                student,
                studentAttendanceRepository.getCurrentAttendanceState(
                        student.get_id(), attendanceId), this);
        return itemView;
    }

    @Override
    public void onAttendanceClick(String studentId, boolean isPresent) {
        attendanceMap.put(studentId, isPresent);
    }
}
