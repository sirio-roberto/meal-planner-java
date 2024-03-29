package mealplanner.entities;

import java.util.List;

public class Meal {
    private int id;
    private final String name;
    private final Category category;
    private final List<Ingredient> ingredients;

    public Meal(String name, Category category, List<Ingredient> ingredients) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
    }

    public Meal(String name, Category category, List<Ingredient> ingredients, int id) {
        this(name, category, ingredients);
        this.id = id;
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

    public Category getCategory() {
        return category;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    private String getIngredientsList() {
        StringBuilder builder = new StringBuilder("Ingredients:");
        ingredients.forEach(i -> builder.append("\n").append(i));
        return builder.toString();
    }

    @Override
    public String toString() {
        return String.format("""
                Name: %s
                %s""",
                name,
                getIngredientsList());
    }

    public enum Category {
        BREAKFAST, LUNCH, DINNER;

        public String getTitledName() {
            return name().toUpperCase().charAt(0) + name().toLowerCase().substring(1);
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
