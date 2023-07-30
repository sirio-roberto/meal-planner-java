# Meal Planner Application

The Meal Planner Application is a simple command-line tool that allows users to manage and plan their meals. With this application, users can add new meals, view meals by category, plan meals for weekdays, save shopping lists, and exit the application.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Commands](#commands)
- [Contributing](#contributing)
- [License](#license)

## Features

- Add new meals with ingredients.
- View meals categorized as breakfast, lunch, or dinner.
- Plan meals for each weekday.
- Save the planned meals as a shopping list.
- Easily manage and organize your meal planning.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher.
- PostgreSQL database server (optional if you want to use the database feature).

### Compiling the Application

To compile the Meal Planner Application, follow these steps:

1. Clone the repository: `git clone https://github.com/sirio-roberto/meal-planner-java.git`
2. Navigate to the project directory: `cd meal-planner`
3. Compile the application: `javac -d bin src/mealplanner/Main.java`

### Usage

To run the Meal Planner Application, execute the following command:

```bash
java -cp bin mealplanner.Main
```
### Commands

The Meal Planner Application supports the following commands:

- `add`: Add a new meal with ingredients.
- `show`: View meals categorized as breakfast, lunch, or dinner.
- `plan`: Plan meals for each weekday.
- `save`: Save the planned meals as a shopping list.
- `exit`: Exit the application.

### Contributing

Contributions are welcome! If you find a bug or have any suggestions, please feel free to open an issue or submit a pull request.
### License

The Meal Planner Application is open-source software licensed under the MIT License.