package com.melvinsalas.trencr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by msalas on 2/29/2016.
 */
public class Station {
    private String mName;
    private ArrayList<String> mListHour;

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public ArrayList<String> getListHour() {
        return mListHour;
    }

    public void setListHour(JSONArray array) throws JSONException {
        mListHour = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i<array.length(); i++){
                mListHour.add(array.get(i).toString());
            }
        }
    }

    public String getListHourString() {
        String string = "";
        for (int i = 0; i < mListHour.size(); i++){
            if(mListHour.get(i).length() > 0) {
                string += mListHour.get(i) + "  ~  ";
            }
        }
        string = string.substring(0, string.length() - 3);
        return string;
    }
}
