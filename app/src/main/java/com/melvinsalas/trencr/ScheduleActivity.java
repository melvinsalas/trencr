package com.melvinsalas.trencr;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    // =============================================================================================
    // VARIABLES
    // =============================================================================================

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String mJson, mName;
    private int mPosition;
    private StationManager mStationManager;

    // =============================================================================================
    // SCHEDULE
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // CONSTRUCTOR
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // BUNDLE
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            setJson(bundle.getString("JSON"));
            setName(bundle.getString("NAME"));
            setPosition(bundle.getInt("POSITION"));
        }

        // PAGE ADAPTER
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getJson());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // =============================================================================================
    // PLACEHOLDER FRAGMENT
    // =============================================================================================

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_JSON = "section_json";
        private StationManager mStationManager;

        public static PlaceholderFragment newInstance(int sectionNumber, String sectionJson) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_JSON, sectionJson);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

            mStationManager = new StationManager();

            // STATION MANAGER
            try {
                JSONObject routes = new JSONObject(getArguments().getString(ARG_SECTION_JSON));
                JSONArray routesArray = routes.optJSONArray("stations");
                for (int i = 0; i < routesArray.length(); i++) {
                    mStationManager.addStation(routesArray.getJSONObject(i));
                }
            } catch (Exception e) {

            }

            int position = getArguments().getInt(ARG_SECTION_NUMBER) - 1;

            ListView listView = (ListView) rootView.findViewById(R.id.time_list);

            //ArrayList<String> data = mStationManager.getStations().get(position).getListHour();
            //mListViewAdapter = new ListViewAdapter(this, data);
            //getRoutesList().setAdapter(mListViewAdapter);
            return rootView;
        }
    }

    // =============================================================================================
    // SECTION PAGER ADAPTER
    // =============================================================================================

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String mJson;

        public SectionsPagerAdapter(FragmentManager fragmentManager, String json) {
            super(fragmentManager);
            mJson = json;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1, mJson);
        }

        @Override
        public int getCount() {
            return getStationManager().getCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getStationManager().getName(position);
        }
    }

    // =============================================================================================
    // SETS & GETS
    // =============================================================================================

    public String getJson() {
        return mJson;
    }

    public void setJson(String mJson) {
        this.mJson = mJson;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public StationManager getStationManager() {
        if(mStationManager == null) {
            mStationManager = new StationManager();
        }
        return mStationManager;
    }

    public void setStationManager(StationManager mStationManager) {
        this.mStationManager = mStationManager;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
