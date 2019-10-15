package com.example.coursera.resources;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.ws.rs.core.Response;

import com.example.coursera.api.GetDish;
import com.example.coursera.model.DishesTable;
import com.example.coursera.model.Dish;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Path("/dishes")
@Produces(MediaType.APPLICATION_JSON)
public class DishesResource {
    private final DishesTable dishesTable;

  /*  public DishesResource(DishesTable dishesTable) {

        this.dishesTable = dishesTable;
    }*/

    @GET
    @Timed
    public List<GetDish> getDishes() {
        List<GetDish> getDishes= new ArrayList<GetDish>();
        List<Dish> dishes = dishesTable.selectAllDishes();
        dishes.forEach(dish -> {
            GetDish getDish = new GetDish(dish.getId(), dish.getName(), dish.getDescription());
            getDishes.add(getDish);
        });

        return getDishes;
    }

    @POST
    @Timed
    /* Returns json object { 'response': 'value' } in the post response */
    /*public PostDish postDishes(DishesBody dishes) {
        final String value = "Will add the dish:" + dishes.name + " with details: " + dishes.description;
        return new PostDish(value);
    }*/

    /* Returns unquoted string in post response */
    public Object postDishes(DishesBody dishes) {
        final String value = "Will add the dish:" + dishes.name + " with details: " + dishes.description;
        return value;
    }

    public static class DishesBody {
        @JsonProperty("name")
        public String name;

        @JsonProperty("description")
        public String description;
    }

    @PUT
    @Timed
    /* Returns unquoted string in put response */
    public Response putDishes() {
        final String value = "PUT operation not supported on /dishes";
        return Response.status(403).entity(value).build();
    }

    @DELETE
    @Timed
    /* Returns unquoted string in put response */
    public Response deleteDishes() {
        final String value = "Deleting all dishes";
        return Response.ok(value).build();
    }
}
