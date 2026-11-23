package com.example.apipocket.model.country;

import com.google.gson.annotations.SerializedName;

public class CountryCapital {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }
}
