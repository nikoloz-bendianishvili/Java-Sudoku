# Sudoku and Metropolis Database

A Java Swing project that combines a Sudoku solver with a small database-backed metropolis search tool.

## Features

- Solves 9x9 Sudoku puzzles from plain text input.
- Reports the number of solutions and elapsed solve time.
- Provides a Swing UI for entering and checking Sudoku puzzles.
- Provides a Swing UI for searching and adding metropolises.
- Supports filtering metropolises by city, continent, and population.
- Includes unit tests for the solver and the SQL table model.

## Project Structure

```text
.
|-- src
|   |-- main
|   |   |-- java
|   |   |   |-- Metropolis.java
|   |   |   |-- MetropolisFrame.java
|   |   |   |-- MetropolisTableModel.java
|   |   |   |-- Sudoku.java
|   |   |   `-- SudokuFrame.java
|   |   `-- resources
|   |       `-- db
|   |           `-- metropolises.sql
|   `-- test
|       `-- java
|           |-- SQLTests.java
|           `-- SudokuTests.java
|-- docs
|-- pom.xml
`-- README.md
```

## Requirements

- Java 17 or newer
- Maven 3.9 or newer
- MySQL for the metropolis desktop app

The SQL tests use an in-memory H2 database, so they do not require a local MySQL server.

## Getting Started

Clone the repository and run the tests:

```bash
mvn test
```

Run the Sudoku command-line demo:

```bash
mvn exec:java -Dexec.mainClass=Sudoku
```

Run the Sudoku desktop app:

```bash
mvn exec:java -Dexec.mainClass=SudokuFrame
```

Run the metropolis desktop app:

```bash
mvn exec:java -Dexec.mainClass=MetropolisFrame
```

## Database Setup

The metropolis app expects a MySQL database named `MyDataBase` on `localhost:3306` with user `root` and an empty password. The seed script is available at:

```text
src/main/resources/db/metropolises.sql
```

Create the database first, then run the script against it.

## Tests

The test suite covers Sudoku parsing/solving behavior and database-backed metropolis search/add behavior.

```bash
mvn test
```
