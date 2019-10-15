package com.example.coursera.resources;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.example.coursera.api.GetDish;
import com.example.coursera.model.DishesTable;
import com.example.coursera.model.Dish;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import javax.ws.rs.core.Response;

@AllArgsConstructor
@Path("/dishes/{dishId}")
@Produces(MediaType.APPLICATION_JSON)
public class DishResource {
    private final DishesTable dishesTable;

    /*
    public DishResource(DishesTable dishesTable) {
        this.dishesTable = dishesTable;
    }
*/

    @GET
    @Timed
    public GetDish getDish(@PathParam("dishId") String dishId) {
        Dish dish = dishesTable.selectDishById(dishId);

        return new GetDish(dish.getId(), dish.getName(), dish.getDescription());
    }

    @POST
    @Timed
    /* Returns unquoted string in post response */
    public Object postDish(@PathParam("dishId") long dishId) {
        final String value = "POST operation not supported on /dishes/" + dishId;
        return Response.status(403).entity(value).build();
    }

    @PUT
    @Timed
    /* Returns unquoted string in put response */
    public Response putDish(@PathParam("dishId") long dishId, DishBody dish) {
        final String value = "Will update the dish:" + dish.name + " with details: " + dish.description;
        return Response.ok(value).build();
    }

    public static class DishBody {
        @JsonProperty("name")
        public String name;

        @JsonProperty("description")
        public String description;
    }

    @DELETE
    @Timed
    /* Returns unquoted string in put response */
    public Response deleteDish(@PathParam("dishId") long dishId) {
        final String value = "Deleting dish " + dishId;
        return Response.ok(value).build();
    }
}

