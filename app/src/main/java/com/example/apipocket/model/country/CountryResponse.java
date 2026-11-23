package com.example.apipocket.model.country;

import com.google.gson.annotations.SerializedName;

public class CountryResponse {
    @SerializedName("data")
    private CountryData data;

    public CountryData getData() {
        return data;
    }
}
