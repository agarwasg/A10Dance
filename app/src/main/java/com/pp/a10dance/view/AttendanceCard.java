package com.pp.a10dance.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.android.AndroidContext;
import com.pp.a10dance.R;
import com.pp.a10dance.document.StudentAttendance;
import com.pp.a10dance.document.StudentAttendanceRepository;
import com.pp.a10dance.document.StudentRepository;
import com.pp.a10dance.helper.LogUtils;
import com.pp.a10dance.helper.Utils;
import com.pp.a10dance.model.Student;

/**
 * Created by saketagarwal on 4/26/15.
 */
public class AttendanceCard extends CardView {
    public static final String TAG = LogUtils.getTag(AttendanceCard.class);
    private final Context mContext;
    private final ImageView mStudentImage;
    private final TextView studentName;
    private final Button attenDanceButton;
    private final StudentRepository studentRepository;

    public AttendanceCard(Context context) {
        this(context, null);
    }

    public AttendanceCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AttendanceCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setRadius(getResources().getDimension(R.dimen.card_corner_radius));
        setCardElevation(getResources().getDimension(R.dimen.card_elevation));
        View baseView = inflate(mContext, R.layout.attendance_card, this);
        mStudentImage = (ImageView) baseView
                .findViewById(R.id.student_attendance_imageView);
        studentName = (TextView) baseView
                .findViewById(R.id.student_name_textView);
        attenDanceButton = (Button) baseView.findViewById(R.id.absent_button);
        studentRepository = new StudentRepository(new AndroidContext(mContext));

    }

    public void setDetails(final Student student, String attendanceId,
            final OnAttendanceClickListener onAttendanceClickListener) {
        final boolean isPresent = getCurrentAttendanceState(student.get_id(),
                attendanceId);
        ImageViewBorder studentNameDrawable = ImageViewBorder.builder()
                .beginConfig().withBorder(8).bold().endConfig()
                .buildRound(student.getStudentFirstLetter(), Color.GREEN); // radius
                                                                           // in
                                                                           // px
        mStudentImage.setImageDrawable(studentNameDrawable);
        studentName.setText(student.getName());
        attenDanceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAttendanceClickListener != null) {
                    onAttendanceClickListener.onAttendanceClick(student,
                            isPresent);
                }
            }
        });
    }

    private boolean getCurrentAttendanceState(String studentId,
            String attendanceId) {
        if (Utils.StringUtils.isBlank(attendanceId)) {
            return true;
        }

        StudentAttendanceRepository studentAttendanceRepository = new StudentAttendanceRepository(
                new AndroidContext(mContext));
        Query query = studentAttendanceRepository.getQuery(attendanceId,
                studentId);
        query.setLimit(1);
        try {
            QueryEnumerator result = query.run();
            StudentAttendance studentAttendance = studentAttendanceRepository
                    .get(result.getRow(0).getDocumentId());
            Log.d(TAG, "Student Fragment is " + studentAttendance.toString());
            if (studentAttendance == null) {
                return true;
            }
            return studentAttendance.isPresent();
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return true;

    }

    public interface OnAttendanceClickListener {

        void onAttendanceClick(Student student, boolean isPresent);
    }
}
