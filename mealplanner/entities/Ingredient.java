package mealplanner.entities;

public class Ingredient {
    private int id;
    private final String name;

    private int mealId;

    public Ingredient(String name) {
        this.name = name;
    }

    public Ingredient(int id, String name, int mealId) {
        this.id = id;
        this.name = name;
        this.mealId = mealId;
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

    public int getMealId() {
        return mealId;
    }

    @Override
    public String toString() {
        return name;
    }
}
