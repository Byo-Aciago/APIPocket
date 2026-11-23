package com.example.apipocket.model.country;

import com.google.gson.annotations.SerializedName;

public class CountryCurrency {
    @SerializedName("name")
    private String name;

    @SerializedName("code")
    private String code;

    @SerializedName("symbol")
    private String symbol;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }
}
