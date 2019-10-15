package com.example.coursera.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

public class PostDish {
    private String response;

    public PostDish() {
        // Jackson deserialization
    }

    public PostDish(String response) {
        this.response = response;
    }

    @JsonProperty
    public String getResponse() {
        return response;
    }
}
