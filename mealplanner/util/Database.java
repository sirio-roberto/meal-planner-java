package mealplanner.util;

import mealplanner.entities.Ingredient;
import mealplanner.entities.Meal;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class Database {
    private final String DB_URL = "jdbc:postgresql:meals_db";
    private final String USER = "postgres";
    private final String PASS = "1111";
    private Connection conn;

    public Database () {
        try {
            createMealsTable();
            createIngredientsTable();
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

    private void createIngredientsTable() throws SQLException {
        String query = """
                CREATE TABLE IF NOT EXISTS ingredients (
                    ingredient_id SERIAL PRIMARY KEY,
                    ingredient VARCHAR(100) NOT NULL,
                    meal_id integer NOT NULL
                );""";

        Statement statement = getConn().createStatement();
        statement.executeUpdate(query);
        closeStatement(statement);
    }

    private void createMealsTable() throws SQLException {
        String query = """
                CREATE TABLE IF NOT EXISTS meals (
                    meal_id SERIAL PRIMARY KEY,
                    meal VARCHAR(100) NOT NULL,
                    category VARCHAR(20) NOT NULL
                );""";

        Statement statement = getConn().createStatement();
        statement.executeUpdate(query);
        closeStatement(statement);
    }

    public void insertMeal(Meal meal) {
        String insertQuery = """
                INSERT INTO meals (meal, category)
                VALUES (?, ?);""";
        try {
            PreparedStatement st = getConn().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, meal.getName());
            st.setString(2, meal.getCategory().name());
            
            int affectedRows = st.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int mealId = rs.getInt(1);
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
        String insertQuery = """
                INSERT INTO ingredients (ingredient, meal_id)
                VALUES (?, ?);""";
        try {
            PreparedStatement st = getConn().prepareStatement(insertQuery);

            for (Ingredient ingredient: ingredients) {
                st.setString(1, ingredient.getName());
                st.setInt(2, mealId);

                st.executeUpdate();
            }

            st.close();
            closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public HashSet<Meal> getAllMeals() {
        HashMap<Ingredient, Integer> ingredientsMap = getAllIngredients();

        HashSet<Meal> meals = new LinkedHashSet<>();

        String query = """
                SELECT meal_id, meal, category
                FROM meals
                ORDER BY meal_id;""";

        HashMap<Ingredient, Integer> map = new HashMap<>();
        try {
            PreparedStatement st = getConn().prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                List<Ingredient> ingredientList = findIngredientsByMealId(rs.getInt(1), ingredientsMap);

                String name = rs.getString(2);
                Meal.Category category = Meal.Category.valueOf(rs.getString(3));

                Meal meal = new Meal(name, category, ingredientList);
                meals.add(meal);
            }
            closeStatement(st);
            closeConnection();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return meals;
    }

    private List<Ingredient> findIngredientsByMealId(int mealId, HashMap<Ingredient, Integer> ingredientsMap) {
        return ingredientsMap.keySet().stream()
                .filter(k -> ingredientsMap.get(k) == mealId)
                .toList();
    }

    private HashMap<Ingredient, Integer> getAllIngredients() {
        HashMap<Ingredient, Integer> map = new HashMap<>();

        String query = """
                SELECT ingredient, meal_id
                FROM ingredients
                ORDER BY ingredient_id;""";

        try {
            PreparedStatement st = getConn().prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Ingredient ingredient = new Ingredient(rs.getString(1));
                map.put(ingredient, rs.getInt(2));
            }
            closeStatement(st);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return map;
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
