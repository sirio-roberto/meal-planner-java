package mealplanner.entities;

import mealplanner.util.Utils;

public class Ingredient {
    private int id;
    private String name;

    public Ingredient(String name) {
        this.id = Utils.getRandomId();
        this.name = name;
    }

    public Ingredient(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
