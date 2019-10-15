package com.example.coursera.health;

import com.codahale.metrics.health.HealthCheck;

public class DishHealthCheck extends HealthCheck {

    public DishHealthCheck() { }

    @Override
    protected Result check() throws Exception {

        return Result.healthy();
    }
}
