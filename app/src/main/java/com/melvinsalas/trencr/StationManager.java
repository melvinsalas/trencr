package com.melvinsalas.trencr;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Melvin Salas on FEB/29/2016.
 */
public class StationManager {
    private ArrayList<Station> mStations;

    public StationManager() {
        mStations = new ArrayList<>();
    }

    public void addStation(JSONObject json) throws JSONException {
        Station station = new Station();
        station.setName(json.getString("name"));
        station.setListHour(json.getJSONArray("hour"));
        mStations.add(station);
    }

    public ArrayList<Station> getStations() {
        return mStations;
    }

    public int getCount() {
        return mStations.size();
    }

    public String getName(int position) {
        return mStations.get(position).getName();
    }
}
