package com.example.apipocket.model.country;

import com.google.gson.annotations.SerializedName;

public class CountryCodes {
    @SerializedName("alpha_2")
    private String alpha2;

    @SerializedName("alpha_3")
    private String alpha3;

    public String getAlpha2() {
        return alpha2;
    }

    public String getAlpha3() {
        return alpha3;
    }
}
