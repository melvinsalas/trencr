package com.melvinsalas.trencr;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by msalas on 2/29/2016.
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
}
