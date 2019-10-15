package com.example.coursera.health;

import com.codahale.metrics.health.HealthCheck;

public class DishesHealthCheck extends HealthCheck {

    public DishesHealthCheck() { }

    @Override
    protected Result check() throws Exception {

        return Result.healthy();
    }
}
