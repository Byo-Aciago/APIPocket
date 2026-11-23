package com.example.apipocket.model.news;

import com.google.gson.annotations.SerializedName;

public class NewsSource {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
