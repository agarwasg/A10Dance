package com.pp.a10dance.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pp.a10dance.R;
import com.pp.a10dance.helper.LogUtils;
import com.pp.a10dance.model.Student;

public class AttendanceCard extends CardView {
    public static final String TAG = LogUtils.getTag(AttendanceCard.class);
    private final Context mContext;
    private final ImageView mStudentImage;
    private final TextView studentName;
    private final Button attenDanceButton;
    private boolean mIsPresent;

    public interface OnAttendanceClickListener {

        void onAttendanceClick(String studentId, boolean isPresent);
    }

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
    }

    public void setDetails(final Student student, boolean isPresent,
            final OnAttendanceClickListener onAttendanceClickListener) {
        this.mIsPresent = isPresent;
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
                attenDanceButton.setBackgroundColor(getResources().getColor(
                        R.color.accent));
                if (onAttendanceClickListener != null) {
                    mIsPresent = !mIsPresent;
                    onAttendanceClickListener.onAttendanceClick(
                            student.get_id(), mIsPresent);
                }
            }
        });
    }
}
