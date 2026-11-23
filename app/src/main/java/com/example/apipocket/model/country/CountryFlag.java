package com.example.apipocket.model.country;

import com.google.gson.annotations.SerializedName;

public class CountryFlag {
    @SerializedName("url_png")
    private String urlPng;

    @SerializedName("url_svg")
    private String urlSvg;

    public String getUrlPng() {
        return urlPng;
    }

    public String getUrlSvg() {
        return urlSvg;
    }
}
