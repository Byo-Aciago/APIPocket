package com.example.apipocket.model.country;

import com.google.gson.annotations.SerializedName;

public class CountryDescriptions {
    @SerializedName("short")
    private String shortDescription;

    public String getShortDescription() {
        return shortDescription;
    }
}
