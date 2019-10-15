package com.example.coursera.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Dish {
    String id;
    @Setter String name;
    @Setter String description;

/*    public Dish (String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId () { return id; };

    public String getName () { return name; };
    public void setName(String name) { this.name = name; }

    public String getDescription () { return description; };
    public void setDescription(String description) { this.description = description; }
 */
}
