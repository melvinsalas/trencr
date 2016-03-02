package com.melvinsalas.trencr;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by melvin on 27/2/16.
 */
public class RouteManager {
    private ArrayList<Route> routes;

    public RouteManager() {
        routes = new ArrayList<>();
    }

    public void addRoute(JSONObject json) throws JSONException {
        Route route = new Route();
        route.setJson(json.getString("json"));
        route.setName(json.getString("name"));
        route.setDescription(json.getString("desc"));
        routes.add(route);
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }
}
