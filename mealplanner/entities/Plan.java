package mealplanner.entities;

public class Plan {
    Meal meal;
    Weekday weekday;

    public Plan(Meal meal, Weekday weekday) {
        this.meal = meal;
        this.weekday = weekday;
    }

    public Meal getMeal() {
        return meal;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public enum Weekday {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

        @Override
        public String toString() {
            return name().toUpperCase().charAt(0) + name().toLowerCase().substring(1);
        }
    }
}
