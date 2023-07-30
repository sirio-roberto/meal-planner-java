package mealplanner;

import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;
import mealplanner.entities.Plan;
import mealplanner.util.Database;
import mealplanner.util.InputHandler;
import mealplanner.util.Utils;

import java.util.*;

public class MealApp {
    private final HashSet<Meal> meals;
    HashSet<Plan> plans;
    private boolean appRunning;
    private final HashMap<String, Command> commands;
    public InputHandler inputHandler = new InputHandler();
    private final Database db;

    public MealApp() {
        db = new Database();

        this.meals = db.getAllMeals();
        this.plans = db.getAllPlans();
        appRunning = false;

        commands = new LinkedHashMap<>();
        commands.put("add", new AddCommand());
        commands.put("show", new ShowCommand());
        commands.put("plan", new PlanCommand());
        commands.put("save", new SaveCommand());
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
            System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
            String categoryStr = inputHandler.getNextString();
            while (Utils.isInvalidCategory(categoryStr)) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                categoryStr = inputHandler.getNextString();
            }

            HashSet<Meal> catMeals = db.findMealsByCategory(categoryStr.toUpperCase());
            if (catMeals.isEmpty()) {
                System.out.println("No meals found.");
            } else {
                System.out.printf("Category: %s\n", categoryStr);
                for (Meal meal : catMeals) {
                    System.out.println();
                    System.out.println(meal);
                }
                System.out.println();
            }
        }
    }

    class PlanCommand implements Command {
        @Override
        public void execute() {
            db.deleteAllPlans();
            plans.clear();

            for (Plan.Weekday weekday: Plan.Weekday.values()) {
                System.out.println(weekday);
                for (Meal.Category category: Meal.Category.values()) {
                    List<Meal> orderedMeals = Utils.getMealsByCategory(meals, category);

                    orderedMeals.forEach(m -> System.out.println(m.getName()));
                    System.out.printf("Choose the %s for %s from the list above:\n", category, weekday);
                    String userOption = inputHandler.getNextString();
                    Meal chosenMeal = Utils.getMealFromName(orderedMeals, userOption);
                    while (chosenMeal == null) {
                        System.out.print("This meal doesn't exist. Choose a meal from the list above.\n");
                        userOption = inputHandler.getNextString();
                        chosenMeal = Utils.getMealFromName(orderedMeals, userOption);
                    }
                    Plan plan = new Plan(chosenMeal, weekday);
                    plans.add(plan);
                    db.insertPlan(plan);
                }
                System.out.printf("Yeah! We planned the meals for %s.\n\n", weekday);
            }

            for (Plan.Weekday weekday: Plan.Weekday.values()) {
                System.out.println(weekday);
                for (Plan plan: plans) {
                    if (plan.getWeekday().equals(weekday)) {
                        Meal meal = plan.getMeal();
                        System.out.printf("%s: %s\n", meal.getCategory().getTitledName(), meal.getName());
                    }
                }
                System.out.println();
            }
        }
    }

    class SaveCommand implements Command {

        @Override
        public void execute() {
            if (plans.isEmpty()) {
                System.out.println("Unable to save. Plan your meals first.");
            } else {
                System.out.println("Input a filename:");
                String fileName = inputHandler.getNextString();
                if (!fileName.isBlank()) {
                    String shoppingList = Utils.getShoppingList(plans);
                    Utils.saveStringToFile(shoppingList, fileName);
                } else {
                    System.out.println("Invalid file name!");
                }
            }
        }
    }

    class ExitCommand implements Command {
        @Override
        public void execute() {
            System.out.println("Bye!");
            appRunning = false;
            db.closeConnection();
        }
    }
}
