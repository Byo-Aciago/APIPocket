package com.example.apipocket.model.country;

import com.google.gson.annotations.SerializedName;

public class CountryNames {
    @SerializedName("common")
    private String common;

    @SerializedName("official")
    private String official;

    public String getCommon() {
        return common;
    }

    public String getOfficial() {
        return official;
    }
}
