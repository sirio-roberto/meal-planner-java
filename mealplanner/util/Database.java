package mealplanner.util;

import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;
import mealplanner.entities.Plan;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class Database {
    private final String DB_URL = "jdbc:postgresql:meals_db";
    private final String USER = "postgres";
    private final String PASS = "1111";

    private final String CREATE_MEALS_TABLE = """
                CREATE TABLE IF NOT EXISTS meals (
                    meal_id INTEGER PRIMARY KEY,
                    meal VARCHAR(100) NOT NULL,
                    category VARCHAR(20) NOT NULL
                );""";

    private final String CREATE_INGREDIENTS_TABLE = """
                CREATE TABLE IF NOT EXISTS ingredients (
                    ingredient_id INTEGER PRIMARY KEY,
                    ingredient VARCHAR(100) NOT NULL,
                    meal_id INTEGER NOT NULL
                );""";

    private final String CREATE_PLAN_TABLE = """
                CREATE TABLE IF NOT EXISTS plan (
                    plan_id SERIAL PRIMARY KEY,
                    meal_name VARCHAR(100) NOT NULL,
                    meal_category VARCHAR(20) NOT NULL,
                    weekday VARCHAR(20) NOT NULL,
                    meal_id INTEGER NOT NULL
                );""";

    private static final String INSERT_MEAL = """
                INSERT INTO meals (meal, category, meal_id)
                VALUES (?, ?, nextval('meal_id_seq'));""";

    private static final String INSERT_INGREDIENT = """
                INSERT INTO ingredients (ingredient, meal_id, ingredient_id)
                VALUES (?, ?, nextval('ingredient_id_seq'));""";

    private final String INSERT_PLAN = """
                INSERT INTO plan (meal_name, meal_category, weekday, meal_id)
                VALUES (?, ?, ?, ?);""";

    private static final String SELECT_INGREDIENTS = "SELECT * FROM ingredients;";
    private final String SELECT_PLANS = "SELECT * FROM plan;";
    private final String DELETE_ALL_PLANS = "DELETE FROM plan;";

    private Connection conn;

    public Database () {
        try {
            createIdSequences();
            createMealsTable();
            createIngredientsTable();
            createPlanTable();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private Connection getConn() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return conn;
    }

    private void createIdSequences() throws SQLException {
        String mealsQuery = "CREATE SEQUENCE IF NOT EXISTS meal_id_seq;";
        String ingredientsQuery = "CREATE SEQUENCE IF NOT EXISTS ingredient_id_seq";

        Statement statement = getConn().createStatement();
        statement.executeUpdate(mealsQuery);
        statement.executeUpdate(ingredientsQuery);
        closeStatement(statement);
    }

    // TODO: refactor using foreign key
    private void createIngredientsTable() throws SQLException {
        Statement statement = getConn().createStatement();
        statement.executeUpdate(CREATE_INGREDIENTS_TABLE);
        closeStatement(statement);
    }

    private void createMealsTable() throws SQLException {
        Statement statement = getConn().createStatement();
        statement.executeUpdate(CREATE_MEALS_TABLE);
        closeStatement(statement);
    }

    private void createPlanTable() throws SQLException {
        Statement statement = getConn().createStatement();
        statement.executeUpdate(CREATE_PLAN_TABLE);
        closeStatement(statement);
    }

    public void insertPlan(Plan plan) {
        try {
            PreparedStatement st = getConn().prepareStatement(INSERT_PLAN);
            Meal meal = plan.getMeal();

            st.setString(1, meal.getName());
            st.setString(2, meal.getCategory().name());
            st.setString(3, plan.getWeekday().name());
            st.setInt(4, meal.getId());

            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void deleteAllPlans() {
        try {
            Statement st = getConn().createStatement();
            st.executeUpdate(DELETE_ALL_PLANS);
            st.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void insertMeal(Meal meal) {
        try {
            PreparedStatement st = getConn().prepareStatement(INSERT_MEAL, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, meal.getName());
            st.setString(2, meal.getCategory().name());
            
            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int mealId = rs.getInt(1);
                    meal.setId(mealId);
                    insertIngredients(meal.getIngredients(), mealId);
                }
                closeResultSet(rs);
            }
            st.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void insertIngredients(List<Ingredient> ingredients, int mealId) {
        try {
            PreparedStatement st = getConn().prepareStatement(INSERT_INGREDIENT, Statement.RETURN_GENERATED_KEYS);

            for (Ingredient ingredient: ingredients) {
                st.setString(1, ingredient.getName());
                st.setInt(2, mealId);

                int affectedRows = st.executeUpdate();
                if (affectedRows > 0) {
                    ResultSet rs = st.getGeneratedKeys();
                    if (rs.next()) {
                        int ingredientId = rs.getInt(1);
                        ingredient.setId(ingredientId);
                    }
                    closeResultSet(rs);
                }
            }

            st.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public HashSet<Plan> getAllPlans() {
        HashSet<Plan> plans = new LinkedHashSet<>();
        HashSet<Meal> allMeals = getAllMeals();

        try {
            PreparedStatement st = getConn().prepareStatement(SELECT_PLANS);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Plan.Weekday weekday = Plan.Weekday.valueOf(rs.getString("weekday"));
                int mealId = rs.getInt("meal_id");
                Meal meal = getMealById(allMeals, mealId);

                plans.add(new Plan(meal, weekday));
            }
            closeStatement(st);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return plans;
    }

    private Meal getMealById(HashSet<Meal> meals, int mealId) {
        return meals.stream()
                .filter(m -> m.getId() == mealId)
                .findAny()
                .orElse(null);
    }

    public HashSet<Meal> findMealsByCategory(String catName) {
        String query;
        HashSet<Ingredient> ingredientsSet = getAllIngredients();

        HashSet<Meal> meals = new LinkedHashSet<>();

        if (catName != null) {
            query = """
                SELECT meal_id, meal, category
                FROM meals
                WHERE category = ?;""";
        } else {
            query = """
                SELECT meal_id, meal, category
                FROM meals;""";
        }

        try {
            PreparedStatement st = getConn().prepareStatement(query);
            if (catName != null) {
                st.setString(1, catName);
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int mealId = rs.getInt(1);
                List<Ingredient> ingredientList = findIngredientsByMealId(mealId, ingredientsSet);

                String name = rs.getString(2);
                Meal.Category category = Meal.Category.valueOf(rs.getString(3));

                Meal meal = new Meal(name, category, ingredientList, mealId);
                meals.add(meal);
            }
            closeStatement(st);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return meals;
    }

    public HashSet<Meal> getAllMeals() {
        return findMealsByCategory(null);
    }

    private List<Ingredient> findIngredientsByMealId(int mealId, HashSet<Ingredient> ingredientsSet) {
        return ingredientsSet.stream()
                .filter(ing -> ing.getMealId() == mealId)
                .toList();
    }

    private HashSet<Ingredient> getAllIngredients() {
        HashSet<Ingredient> ingredients = new LinkedHashSet<>();

        try {
            PreparedStatement st = getConn().prepareStatement(SELECT_INGREDIENTS);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Ingredient ingredient = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient"),
                        rs.getInt("meal_id"));
                ingredients.add(ingredient);
            }
            closeStatement(st);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return ingredients;
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
