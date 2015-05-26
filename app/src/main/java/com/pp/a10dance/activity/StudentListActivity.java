package com.pp.a10dance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.pp.a10dance.fragments.StudentListFragment;

/**
 * Created by saketagarwal on 4/7/15.
 */
public class StudentListActivity extends BaseActivity {
    public static final String CLASS_ID_ARGS = "class_id";

    public static Intent createInstance(Context context, String classId) {
        Intent intent = new Intent(context, StudentListActivity.class);
        intent.putExtra(CLASS_ID_ARGS, classId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public Fragment onCreatePane() {
        return new StudentListFragment();
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
