package mealplanner.entities;

import mealplanner.entities.Ingredient;
import mealplanner.util.Utils;

import java.util.List;

public class Meal {
    private int id;
    private String name;
    private Category category;
    private List<Ingredient> ingredients;

    public Meal(String name, Category category, List<Ingredient> ingredients) {
        this.id = Utils.getRandomId();
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
