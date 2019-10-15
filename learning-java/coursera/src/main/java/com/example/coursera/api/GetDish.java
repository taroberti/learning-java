package com.example.coursera.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetDish {
    private String id;
    private String name;
    private String description;

    public GetDish() {
        // Jackson deserialization
    }

    /*public GetDish(String id, String dishName, String description) {
        this.id = id;
        this.name = dishName;
        this.description = description;
    }*/

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }
}
