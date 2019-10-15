package com.example.coursera;

import com.datastax.driver.core.utils.UUIDs;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.example.coursera.resources.CourseraResource;
import com.example.coursera.resources.DishesResource;
import com.example.coursera.resources.DishResource;

import com.example.coursera.health.CourseraHealthCheck;
import com.example.coursera.health.DishesHealthCheck;
import com.example.coursera.health.DishHealthCheck;

import com.example.coursera.model.DishesTable;
import com.example.coursera.model.Dish;

public class CourseraApplication extends Application<CourseraConfiguration> {
    public static void main(String[] args) throws Exception {
        new CourseraApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<CourseraConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(CourseraConfiguration configuration,
                    Environment environment) {
        /* RESOURCES */
        final CourseraResource resource = new CourseraResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );

        final DishesTable dishesTable = new DishesTable("127.0.0.1", 9042, "dishCollection", "dishes");
        final DishesResource dishesResource = new DishesResource(dishesTable);

        final DishResource dishResource = new DishResource(dishesTable);

        /* HEALTH CHECKS */
        final CourseraHealthCheck healthCheck =
                new CourseraHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);

        final DishesHealthCheck dishesHealthCheck = new DishesHealthCheck();
        environment.healthChecks().register("dishes", healthCheck);
        environment.jersey().register(dishesResource);

        final DishHealthCheck dishHealthCheck = new DishHealthCheck();
        environment.healthChecks().register("dish", healthCheck);
        environment.jersey().register(dishResource);


        /*dishesTable.createKeyspace("SimpleStrategy", 1);
        dishesTable.checkKeyspace();
        dishesTable.createTable();
        dishesTable.checkTable();
        final Dish dish = new Dish(UUIDs.timeBased().toString(), "Ice cream", "Test");
        dishesTable.insertTable(dish);
        dish.setDescription("Updated Test");
        dishesTable.updateTable(dish);
        dishesTable.checkDish();
         */
    }

}
