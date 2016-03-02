package com.melvinsalas.trencr;

/**
 * Created by melvin on 27/2/16.
 */
public class Route {
    private String mJson;
    private String mName;
    private String mDescription;

    public String getJson() {
        return mJson;
    }

    public void setJson(String id) {
        this.mJson = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }
}
