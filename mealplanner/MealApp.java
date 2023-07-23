package mealplanner;

import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;
import mealplanner.util.InputHandler;
import mealplanner.util.Utils;

import java.util.*;

public class MealApp {
    private HashSet<Meal> meals;
    private boolean appRunning;
    private HashMap<String, Command> commands;
    public InputHandler inputHandler = new InputHandler();

    public MealApp() {
        this.meals = new LinkedHashSet<>();
        appRunning = false;

        commands = new LinkedHashMap<>();
        commands.put("add", new AddCommand());
    }

    public void executeCommand(String action) {
        commands.get(action).execute();
    }

    public void run() {
        appRunning = true;
        while (appRunning) {
            showCommandList();
            String action = inputHandler.getNextString();
            executeCommand(action);
        }
    }

    private void showCommandList() {
        StringBuilder sb = new StringBuilder("What would you like to do (");
        for (String key: commands.keySet()) {
            sb.append(key).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")?");

        System.out.println(sb);
    }


    interface Command {
        void execute();
    }

    class AddCommand implements Command {

        @Override
        public void execute() {
            System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
            String categoryStr = inputHandler.getNextString();
            System.out.println("Input the meal's name:");
            String name = inputHandler.getNextString();
            System.out.println("Input the ingredients:");
            String ingredientsStr = inputHandler.getNextString();

            Meal.Category category = Meal.Category.valueOf(categoryStr.toUpperCase());
            List<Ingredient> ingredients = Utils.getIngredientsFromStr(ingredientsStr);
            Meal meal = new Meal(name, category, ingredients);
            meals.add(meal);

            System.out.println("The meal has been added!");
        }
    }
}
