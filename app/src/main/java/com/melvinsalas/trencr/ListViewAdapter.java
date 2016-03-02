package com.melvinsalas.trencr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by msalas on 2/29/2016.
 */
public class ListViewAdapter extends BaseAdapter {
    private ArrayList<Route> data;
    private ArrayList<Station> mStations;
    private LayoutInflater inflater;
    private Type mType;

    public ListViewAdapter(Activity base, ArrayList<Route> data) {
        this.inflater = (LayoutInflater) base.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        mType = Type.ROUTE;
    }

    public ListViewAdapter(StationsActivity base, ArrayList<Station> data) {
        this.inflater = (LayoutInflater) base.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mStations = data;
        mType = Type.STATION;
    }

    @Override
    public int getCount() {
        int count = 0;
        switch (mType) {
            case ROUTE:
                count = data.size();
                break;
            case STATION:
                count = mStations.size();
                break;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        switch (mType) {
            case ROUTE:
                item = data.get(position);
                break;
            case STATION:
                item = mStations.get(position);
                break;
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

        switch (mType) {
            case ROUTE:
                Route route = data.get(position);
                title.setText(route.getName());
                description.setText(route.getDescription());
                break;
            case STATION:
                Station station = mStations.get(position);
                title.setText(station.getName());
                description.setText(station.getListHourString());
                if (position == 0) {
                    icon.setImageResource(R.drawable.ms_up_station);
                } else {
                    if (position + 1 == mStations.size()) {
                        icon.setImageResource(R.drawable.ms_down_station);
                    } else {
                        icon.setImageResource(R.drawable.ms_mid_station);
                    }
                }
                break;
        }

        return convertView;
    }

    private enum Type {
        ROUTE, STATION
    }
}
