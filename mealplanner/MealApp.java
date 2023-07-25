package mealplanner;

import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;
import mealplanner.util.Database;
import mealplanner.util.InputHandler;
import mealplanner.util.Utils;

import java.util.*;

public class MealApp {
    private HashSet<Meal> meals;
    private boolean appRunning;
    private final HashMap<String, Command> commands;
    public InputHandler inputHandler = new InputHandler();
    private Database db;

    public MealApp() {
        this.meals = new LinkedHashSet<>();
        db = new Database();
        appRunning = false;

        commands = new LinkedHashMap<>();
        commands.put("add", new AddCommand());
        commands.put("show", new ShowCommand());
        commands.put("exit", new ExitCommand());
    }

    public void executeCommand(String action) {
        if (commands.containsKey(action)) {
            commands.get(action).execute();
        }
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
            while (Utils.isInvalidCategory(categoryStr)) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                categoryStr = inputHandler.getNextString();
            }

            System.out.println("Input the meal's name:");
            String name = inputHandler.getNextString();
            while (Utils.isInvalidName(name)) {
                System.out.println("Wrong format. Use letters only!");
                name = inputHandler.getNextString();
            }

            System.out.println("Input the ingredients:");
            String ingredientsStr = inputHandler.getNextString();
            while (Utils.areInvalidNames(ingredientsStr)) {
                System.out.println("Wrong format. Use letters only!");
                ingredientsStr = inputHandler.getNextString();
            }

            Meal.Category category = Meal.Category.valueOf(categoryStr.toUpperCase());
            List<Ingredient> ingredients = Utils.getIngredientsFromStr(ingredientsStr);
            Meal meal = new Meal(name, category, ingredients);
            meals.add(meal);
            db.insertMeal(meal);

            System.out.println("The meal has been added!");
        }
    }

    class ShowCommand implements Command {
        @Override
        public void execute() {
            if (meals.isEmpty()) {
                System.out.println("No meals saved. Add a meal first.");
            } else {
                for (Meal meal : meals) {
                    System.out.println();
                    System.out.println(meal);
                }
                System.out.println();
            }
        }
    }

    class ExitCommand implements Command{
        @Override
        public void execute() {
            System.out.println("Bye!");
            appRunning = false;
        }
    }
}
