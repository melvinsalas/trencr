package com.melvinsalas.trencr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mRoutesList;
    private ListViewAdapter mListViewAdapter;
    private View mLayout, mCloudMessage;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        JSONParse jsonParse = new JSONParse();
        jsonParse.execute();

        clickListener();
    }

    private void clickListener() {
        getRoutesList().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Route route = (Route) mListViewAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, StationsActivity.class);
                intent.putExtra("JSON", route.getJson());
                intent.putExtra("NAME", route.getName());
                startActivityForResult(intent, 0);

            }
        });

        getSwipeRefreshLayout().setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                JSONParse jsonParse = new JSONParse(true);
                jsonParse.execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == 1) {
                Snackbar snackbar = Snackbar.make(getLayout(), "Ruta no disponible", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class JSONParse extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        private Boolean mSwipe;

        public JSONParse(Boolean swipe) {
            mSwipe = swipe;
        }

        public JSONParse() {
            this(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!mSwipe) {
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage(getResources().getString(R.string.loading));
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... args) {
            String url = getResources().getString(R.string.url) +
                    getResources().getString(R.string.url_start);
            String json = "";
            try {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response = client.execute(get);
                HttpEntity entity = response.getEntity();
                json = EntityUtils.toString(entity, HTTP.UTF_8);
            } catch (IOException e) {
                showCloudMessage(true);
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                RouteManager routeManager = new RouteManager();
                JSONObject routes = new JSONObject(json);
                JSONArray routesArray = routes.optJSONArray("routes");
                for (int i = 0; i < routesArray.length(); i++) {
                    routeManager.addRoute(routesArray.getJSONObject(i));
                }
                UpdateUI(routeManager);
            } catch (Exception e) {
                e.printStackTrace();
                showCloudMessage(true);
            }
            showCloudMessage(false);
            if(mSwipe) {
                getSwipeRefreshLayout().setRefreshing(false);
            } else {
                pDialog.dismiss();
            }
        }
    }

    private void showCloudMessage(boolean show) {
        if(show) {
            getCloudMessage().setVisibility(View.VISIBLE);
            getRoutesList().setVisibility(View.GONE);
        } else {
            getCloudMessage().setVisibility(View.GONE);
            getRoutesList().setVisibility(View.VISIBLE);
        }
    }

    private void UpdateUI(RouteManager routeManager) {
        ArrayList<Route> data = routeManager.getRoutes();
        mListViewAdapter = new ListViewAdapter(this, data);
        getRoutesList().setAdapter(mListViewAdapter);
    }

    public ListView getRoutesList() {
        if (mRoutesList == null) {
            mRoutesList = (ListView) findViewById(R.id.routesList);
        }
        return mRoutesList;
    }

    public View getLayout() {
        if (mLayout == null) {
            mLayout = findViewById(R.id.layout);
        }
        return mLayout;
    }

    public View getCloudMessage() {
        if (mCloudMessage == null) {
            mCloudMessage = findViewById(R.id.cloudMessage);
        }
        return mCloudMessage;
    }

    private SwipeRefreshLayout getSwipeRefreshLayout() {
        if (mSwipeRefreshLayout == null) {
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        }
        return mSwipeRefreshLayout;
    }
}
