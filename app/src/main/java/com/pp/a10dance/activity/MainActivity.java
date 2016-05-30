package com.pp.a10dance.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.pp.a10dance.R;
import com.pp.a10dance.fragments.ClassDetailsFragment;
import com.pp.a10dance.fragments.NavigationDrawerFragment;
import com.pp.a10dance.fragments.StudentListFragment;

public class MainActivity extends AppCompatActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        if (viewPager != null) {
            setupViewPager(viewPager);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(String classId) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.container,
                        ClassDetailsFragment.createInstance(classId + ""))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // if (!mNavigationDrawerFragment.isDrawerOpen()) {
        // // Only show items in the action bar relevant to this screen
        // // if the drawer is not showing. Otherwise, let the drawer
        // // decide what to show in the action bar.
        // // getMenuInflater().inflate(R.menu.main, menu);
        // restoreActionBar();
        // return true;
        // }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ClassDetailsViewPagerFragmentAdapter adapter = new ClassDetailsViewPagerFragmentAdapter(
                getSupportFragmentManager(),
                "64b02538-23d4-4b6a-bd4b-1994cd3fb0f3");
        viewPager.setAdapter(adapter);

    }

    private static class ClassDetailsViewPagerFragmentAdapter extends
            FragmentPagerAdapter {

        private String classId;

        public ClassDetailsViewPagerFragmentAdapter(FragmentManager fm,
                String classId) {
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