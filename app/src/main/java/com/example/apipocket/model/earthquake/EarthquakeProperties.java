package com.example.apipocket.model.earthquake;

import com.google.gson.annotations.SerializedName;

public class EarthquakeProperties {
    @SerializedName("mag")
    private double magnitude;

    @SerializedName("place")
    private String place;

    @SerializedName("time")
    private long time;

    public double getMagnitude() {
        return magnitude;
    }

    public String getPlace() {
        return place;
    }

    public long getTime() {
        return time;
    }
}
