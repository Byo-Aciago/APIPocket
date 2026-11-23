package com.example.apipocket.model.country;

import com.google.gson.annotations.SerializedName;

public class CountryLanguage {
    @SerializedName("name")
    private String name;

    @SerializedName("code")
    private String code;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
