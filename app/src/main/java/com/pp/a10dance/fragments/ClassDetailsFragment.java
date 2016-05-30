package com.pp.a10dance.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pp.a10dance.R;
import com.pp.a10dance.activity.StudentListActivity;
import com.pp.a10dance.view.GenericCard;

/**
 * Created by saketagarwal on 4/5/15.
 */
public class ClassDetailsFragment extends Fragment {

    private static final String CLASS_ID_ARGS = "class_id";
    private GenericCard mStudentCard;
    private String mClassId;
    private GenericCard mAttendanceCard;

    public static ClassDetailsFragment createInstance(String classId) {
        ClassDetailsFragment classDetailsFragment = new ClassDetailsFragment();
        Bundle args = new Bundle();
        args.putString(CLASS_ID_ARGS, classId);
        classDetailsFragment.setArguments(args);
        return classDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassId = getArguments().getString(CLASS_ID_ARGS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = StudentListActivity.createInstance(getActivity(),
                mClassId);
        startActivity(intent);
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_details, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        Toolbar toobar = (Toolbar) view.findViewById(R.id.toolbar_class_details);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toobar);

        if (viewPager != null) {
            setupViewPager(viewPager);
            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ClassDetailsViewPagerFragmentAdapter adapter = new
                ClassDetailsViewPagerFragmentAdapter(getChildFragmentManager(), mClassId);
        viewPager.setAdapter(adapter);

    }

    private static class ClassDetailsViewPagerFragmentAdapter extends FragmentPagerAdapter {


        private String classId;

        public ClassDetailsViewPagerFragmentAdapter(FragmentManager fm, String classId) {
            super(fm);
            this.classId = classId;
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public Fragment getItem(int position) {
            return StudentListFragment.createInstance(classId);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Student";
                case 1:
                    return "Lecture";
                default:
                    return "";
            }
        }
    }
}
