package com.ticarte.demojavajdbc;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int rows = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello Java JDBC!");

        // Try-with-resources nos evita tener que cerrar las conexiones, statements y resultsets.
        try (
                Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/java", "root", "");
                Statement stmt = connection.createStatement();
        ) {
            System.out.println("Create table of films:");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS films (" +
                    "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name LONGTEXT, " +
                    "year INT, " +
                    "director LONGTEXT)");

            System.out.println("Insert films:");
            rows = stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('The Shawshank Redemption', 1994, 'Frank Darabont')");
            System.out.println("Number of rows inserted: " + rows);
            rows = stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('The Godfather', 1972, 'Francis Ford Coppola')");
            System.out.println("Number of rows inserted: " + rows);
            rows = stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('The Dark Knight', 2008, 'Christopher Nolan')");
            System.out.println("Number of rows inserted: " + rows);
            rows = stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('The Lord of the Rings: The Return of the King', 2003, 'Peter Jackson')");
            System.out.println("Number of rows inserted: " + rows);
            rows = stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('Pulp Fiction', 1994, 'Quentin Tarantino')");
            System.out.println("Number of rows inserted: " + rows);

            System.out.println("Insert films with keyboard and prepared statement:");
            System.out.println("Insert name of film:");
            String nameFilm = scanner.nextLine();
            System.out.println("Insert year of film:");
            int yearFilm = scanner.nextInt();
            System.out.println("Insert director of film:");
            String directorFilm = scanner.nextLine();
            rows = stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('" + nameFilm + "', " + yearFilm + ", '" + directorFilm + "')");
            System.out.println("Number of rows inserted: " + rows);
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO films (name, year, director) " +
                            "VALUES (?, ?, ?)");) {
                preparedStatement.setString(1, nameFilm);
                preparedStatement.setInt(2, yearFilm);
                preparedStatement.setString(3, directorFilm);
                rows = preparedStatement.executeUpdate();
                System.out.println("Number of rows inserted: " + rows);
            } catch (SQLException e) {
                System.out.println("SQLException => " + e.getMessage());
            }

            System.out.println("Insert films and get generated keys:");
            rows = stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('Forrest Gump', 1994, 'Robert Zemeckis')", Statement.RETURN_GENERATED_KEYS);
            System.out.println("Number of rows inserted: " + rows);
            try (ResultSet rsKeys = stmt.getGeneratedKeys();) {
                if (rsKeys.next()) {
                    System.out.println("Generated key: " + rsKeys.getInt(1));
                }
            } catch (SQLException e) {
                System.out.println("SQLException => " + e.getMessage());
            }

            System.out.println("Update films:");
            rows = stmt.executeUpdate("UPDATE films SET director = 'Frank Darabont (*)' " +
                    "WHERE name = 'The Shawshank Redemption'");
            System.out.println("Number of rows updated: " + rows);

            System.out.println("Delete films:");
            rows = stmt.executeUpdate("DELETE FROM films " +
                    "WHERE name = 'The Godfather'");
            System.out.println("Number of rows deleted: " + rows);

            System.out.println("List of films:");
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM films");) {
                if (rs.last()) {
                    System.out.println("Number of films: " + rs.getRow());
                    rs.beforeFirst();
                }
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + ". " +
                            rs.getString("name") + " (" +
                            rs.getInt("year") + ") [" +
                            rs.getString("director") + "]");
                }
            } catch (SQLException e) {
                System.out.println("SQLException => " + e.getMessage());
            }

            System.out.println("List of films from 1994 with raw statement:");
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM films WHERE year = 1994");) {
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + ". " +
                            rs.getString("name") + " (" +
                            rs.getInt("year") + ") [" +
                            rs.getString("director") + "]");
                }
            } catch (SQLException e) {
                System.out.println("SQLException => " + e.getMessage());
            }

            System.out.println("List of films from 1994 with prepared statement:");
            String stmtSelectFilms = "SELECT * FROM films WHERE year >= ?";
            try (PreparedStatement prepareStmt = connection.prepareStatement(stmtSelectFilms);) {
                prepareStmt.setInt(1, 1994);
                // Depurando se puede visualizar la consulta preparada con sus valores.
                try (ResultSet rs = prepareStmt.executeQuery();) {
                    while (rs.next()) {
                        System.out.println(rs.getInt("id") + ". " +
                                rs.getString("name") + " (" +
                                rs.getInt("year") + ") [" +
                                rs.getString("director") + "]");
                    }
                } catch (SQLException e) {
                    System.out.println("SQLException => " + e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println("SQLException => " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("SQLException => " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception => " + e.getMessage());
        }
    }
}