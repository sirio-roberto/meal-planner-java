package mealplanner;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static final Scanner scan = new Scanner(System.in);

  public static void main(String[] args) {
    showMenu();
  }

  private static void showMenu() {
    System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
    String categoryStr = scan.nextLine();
    System.out.println("Input the meal's name:");
    String name = scan.nextLine();
    System.out.println("Input the ingredients:");
    String ingredientsStr = scan.nextLine();
    System.out.println();

    Meal.Category category = Meal.Category.valueOf(categoryStr.toUpperCase());
    List<Ingredient> ingredients = getIngredientsFromStr(ingredientsStr);
    Meal meal = new Meal(name, category, ingredients);

    System.out.println(meal);
    System.out.println("The meal has been added!");
  }

  // TODO: create a generic method for any String?
  private static List<Ingredient> getIngredientsFromStr(String ingredientsStr) {
    String[] ingredientsArray = ingredientsStr.split(",");
    return Arrays.stream(ingredientsArray).map(Ingredient::new).toList();
  }
}