package com.ticarte.demojavajdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/java", "root", "");

            Statement stmt = connection.createStatement();

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS films (" +
                    "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name LONGTEXT, " +
                    "year INT, " +
                    "director LONGTEXT)");

            stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('The Shawshank Redemption', 1994, 'Frank Darabont')");
            stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('The Godfather', 1972, 'Francis Ford Coppola')");
            stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('The Dark Knight', 2008, 'Christopher Nolan')");
            stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('The Lord of the Rings: The Return of the King', 2003, 'Peter Jackson')");
            stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('Pulp Fiction', 1994, 'Quentin Tarantino')");
            stmt.executeUpdate("INSERT INTO films (name, year, director) " +
                    "VALUES ('Forrest Gump', 1994, 'Robert Zemeckis')");

            stmt.executeUpdate("UPDATE films SET director = 'Frank Darabont (*)' " +
                    "WHERE name = 'The Shawshank Redemption'");

            stmt.executeUpdate("DELETE FROM films " +
                    "WHERE name = 'The Godfather'");

            System.out.println("List of films:");
            ResultSet rs = stmt.executeQuery("SELECT * FROM films");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " +
                        rs.getString("name") + " (" +
                        rs.getInt("year") + ") [" +
                        rs.getString("director") + "]");
            }

            System.out.println("List of films from 1994:");
            rs = stmt.executeQuery("SELECT * FROM films WHERE year = 1994");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " +
                        rs.getString("name") + " (" +
                        rs.getInt("year") + ") [" +
                        rs.getString("director") + "]");
            }

            stmt.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}