package com.ticarte.demojavajdbc;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        int rows = 0;

        System.out.println("Hello Java JDBC!");

        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/java", "root", "");

            Statement stmt = connection.createStatement();

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

            System.out.println("Insert films and get generated keys:");
            rows = stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('Forrest Gump', 1994, 'Robert Zemeckis')", Statement.RETURN_GENERATED_KEYS);
            System.out.println("Number of rows inserted: " + rows);
            ResultSet rsKeys = stmt.getGeneratedKeys();
            if (rsKeys.next()) {
                System.out.println("Generated key: " + rsKeys.getInt(1));
            }

            System.out.println("Update films:");
            rows  = stmt.executeUpdate("UPDATE films SET director = 'Frank Darabont (*)' " +
                    "WHERE name = 'The Shawshank Redemption'");
            System.out.println("Number of rows updated: " + rows);

            System.out.println("Delete films:");
            rows = stmt.executeUpdate("DELETE FROM films " +
                    "WHERE name = 'The Godfather'");
            System.out.println("Number of rows deleted: " + rows);

            System.out.println("List of films:");
            ResultSet rs = stmt.executeQuery("SELECT * FROM films");
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

            System.out.println("List of films from 1994 with raw statement:");
            rs = stmt.executeQuery("SELECT * FROM films WHERE year = 1994");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " +
                        rs.getString("name") + " (" +
                        rs.getInt("year") + ") [" +
                        rs.getString("director") + "]");
            }

            System.out.println("List of films from 1994 with prepared statement:");
            String stmtSelectFilms = "SELECT * FROM films WHERE year >= ?";
            PreparedStatement prepareStmt = connection.prepareStatement(stmtSelectFilms);
            prepareStmt.setInt(1, 1994);
            // Depurando se puede visualizar la consulta preparada con sus valores.
            rs = prepareStmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " +
                        rs.getString("name") + " (" +
                        rs.getInt("year") + ") [" +
                        rs.getString("director") + "]");
            }

            rsKeys.close();
            rs.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}