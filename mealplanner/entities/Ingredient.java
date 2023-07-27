package mealplanner.entities;

import mealplanner.util.Utils;

public class Ingredient implements Comparable<Ingredient> {
    private int id;
    private String name;

    public Ingredient(String name) {
        this.name = name;
    }

    public Ingredient(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Ingredient other) {
        return Integer.compare(this.getId(), other.getId());
    }
}
