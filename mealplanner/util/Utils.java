package mealplanner.util;

import mealplanner.entities.Ingredient;

import java.util.Arrays;
import java.util.List;

public class Utils {
    public static List<Ingredient> getIngredientsFromStr(String ingredientsStr) {
        String[] ingredientsArray = ingredientsStr.split(",");
        return Arrays.stream(ingredientsArray)
                .map(String::trim)
                .map(Ingredient::new).toList();
    }
}
