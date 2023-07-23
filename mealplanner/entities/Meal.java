package mealplanner.entities;

import mealplanner.entities.Ingredient;

import java.util.List;

public class Meal {
    private String name;
    private Category category;
    private List<Ingredient> ingredients;

    public Meal(String name, Category category, List<Ingredient> ingredients) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
    }

    private String getIngredientsList() {
        StringBuilder builder = new StringBuilder("Ingredients:");
        ingredients.forEach(i -> builder.append("\n").append(i));
        return builder.toString();
    }

    @Override
    public String toString() {
        return String.format("""
                Category: %s
                Name: %s
                %s""",
                category.toString(),
                name,
                getIngredientsList());
    }

    public enum Category {
        BREAKFAST, LUNCH, DINNER;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
