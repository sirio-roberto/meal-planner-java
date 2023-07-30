package mealplanner.util;

import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;

import java.util.*;
import java.util.stream.Collectors;

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

    public static List<Meal> getMealsByCategory(HashSet<Meal> meals, Meal.Category category) {
        return meals.stream()
                            .filter(m -> m.getCategory().equals(category))
                            .sorted(Comparator.comparing(Meal::getName))
                            .toList();
    }

    public static Meal getMealFromName(List<Meal> meals, String mealName) {
        return meals.stream()
                            .filter(m -> m.getName().equals(mealName))
                            .findAny()
                            .orElse(null);
    }
}
