package com.example.apipocket.model.country;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CountryData {
    @SerializedName("objects")
    private List<CountryObject> objects;

    public List<CountryObject> getObjects() {
        return objects;
    }
}
