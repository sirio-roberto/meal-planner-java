package mealplanner.util;

import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {
    public static List<Ingredient> getIngredientsFromStr(String ingredientsStr) {
        String[] ingredientsArray = ingredientsStr.split(",");
        return Arrays.stream(ingredientsArray)
                .map(String::trim)
                .map(Ingredient::new).toList();
    }

    public static boolean isInvalidCategory(String categoryStr) {
        for (Meal.Category category: Meal.Category.values()) {
            if (categoryStr.equals(category.toString())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInvalidName(String name) {
        return !name.matches("(?i)[a-z ]+[a-z ]*[a-z ]+");
    }

    public static boolean areInvalidNames(String ingredientsStr) {
        if (!ingredientsStr.matches("(?i)[a-z][a-z ,]*[a-z]")) {
            return true;
        }
        String[] ingredientsArray = ingredientsStr.split(",");
        for (String ingredient: ingredientsArray) {
            if (isInvalidName(ingredient)) {
                return true;
            }
        }
        return false;
    }
}
