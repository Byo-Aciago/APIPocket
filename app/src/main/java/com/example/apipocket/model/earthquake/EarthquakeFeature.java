package com.example.apipocket.model.earthquake;

import com.google.gson.annotations.SerializedName;

public class EarthquakeFeature {
    @SerializedName("id")
    private String id;

    @SerializedName("properties")
    private EarthquakeProperties properties;

    public String getId() {
        return id;
    }

    public EarthquakeProperties getProperties() {
        return properties;
    }
}
