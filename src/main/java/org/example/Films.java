package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Films {
    private int id;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static void addFilm(String title) throws SQLException {
        String sql = "INSERT INTO films (title) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
        }
    }

    public static List<Films> getAllFilms() throws SQLException {
        String sql = "SELECT * FROM films";
        List<Films> films = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Films film = new Films();
                film.setId(resultSet.getInt("id"));
                film.setTitle(resultSet.getString("title"));
                films.add(film);
            }
        }
        return films;
    }
}