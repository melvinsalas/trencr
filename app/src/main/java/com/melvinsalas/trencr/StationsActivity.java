package com.melvinsalas.trencr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class StationsActivity extends AppCompatActivity {

    private ListView mListStation;
    private ProgressDialog mProgressDialog;
    private String mJson;
    private String mName;
    private ListViewAdapter mListViewAdapter;
    private String mJsonString;
    private String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Constructor
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);

        // Bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            setJson((String) bundle.get("JSON"));
            setName((String) bundle.get("NAME"));
        }

        // ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (getName().length() > 0) {
                getSupportActionBar().setTitle(getName());
            }
        }

        // JSON
        JSONParse jsonParse = new JSONParse();
        jsonParse.execute();

        clickListener();
    }

    private void clickListener() {
        getListStation().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StationsActivity.this, ScheduleActivity.class);
                intent.putExtra("JSON", getJsonString());
                intent.putExtra("NAME", getName());
                intent.putExtra("POSITION", position);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public String getJsonString() {
        return jsonString;
    }

    private class JSONParse extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(StationsActivity.this);
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String url = getResources().getString(R.string.url) + getJson();
            String json = "";
            try {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response = client.execute(get);
                HttpEntity entity = response.getEntity();
                json = EntityUtils.toString(entity, HTTP.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                setJsonString(json);
                StationManager stationManager = new StationManager();
                JSONObject routes = new JSONObject(json);
                JSONArray routesArray = routes.optJSONArray("stations");
                for (int i = 0; i < routesArray.length(); i++) {
                    stationManager.addStation(routesArray.getJSONObject(i));
                }
                UpdateUI(stationManager);
            } catch (Exception e) {
                setResult(1);
                finish();
            }
            mProgressDialog.dismiss();
        }
    }

    private void UpdateUI(StationManager stationManager) {
        ArrayList<Station> data = stationManager.getStations();
        mListViewAdapter = new ListViewAdapter(this, data);
        getListStation().setAdapter(mListViewAdapter);
    }

    private ListView getListStation() {
        if (mListStation == null) {
            mListStation = (ListView) findViewById(R.id.listStation);
        }
        return mListStation;
    }
}
