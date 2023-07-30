package mealplanner.util;

import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;
import mealplanner.entities.Plan;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

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

    public static String getShoppingList(HashSet<Plan> plans) {
        Map<String, Integer> ingredientsAndQty = new LinkedHashMap<>();

        List<Ingredient> allIngredients = plans.stream()
                .map(Plan::getMeal)
                .map(Meal::getIngredients)
                .flatMap(List::stream)
                .toList();

        for (Ingredient ingredient: allIngredients) {
            String ingName = ingredient.getName();
            if (ingredientsAndQty.containsKey(ingName)) {
                ingredientsAndQty.put(ingName, ingredientsAndQty.get(ingName) + 1);
            } else {
                ingredientsAndQty.put(ingName, 1);
            }
        }

        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Integer> ingredient: ingredientsAndQty.entrySet()) {
            result.append(ingredient.getKey());
            if (ingredient.getValue() > 1) {
                result.append(" x").append(ingredient.getValue());
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static void saveStringToFile(String string, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(string);
            System.out.println("Saved!");
        } catch (IOException ex) {
            System.out.println("Error while saving file!");
        }
    }
}
