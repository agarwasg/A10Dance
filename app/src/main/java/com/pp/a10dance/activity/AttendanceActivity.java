package com.pp.a10dance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.pp.a10dance.fragments.AttendanceFragment;

/**
 * Created by saketagarwal on 4/22/15.
 */
public class AttendanceActivity extends BaseActivity {

    public static final String CLASS_ID_ARGS = "class_id";
    public static final String ATTENDANCE_ID_ARGS = "attendance_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static Intent createInstance(Context context, String classId) {
        Intent intent = new Intent(context, AttendanceActivity.class);
        intent.putExtra(StudentListActivity.CLASS_ID_ARGS, classId);
        return intent;
    }

    public static Intent createInstance(Context context, String classId,
            String attendanceId) {
        Intent intent = new Intent(context, AttendanceActivity.class);
        intent.putExtra(CLASS_ID_ARGS, classId);
        intent.putExtra(ATTENDANCE_ID_ARGS, attendanceId);
        return intent;
    }

    @Override
    public Fragment onCreatePane() {
        return new AttendanceFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
