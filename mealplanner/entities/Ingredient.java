package mealplanner.entities;

public class Ingredient {
    private String name;

    public Ingredient(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
