package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Books {
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

    public static void addBook(String title) throws SQLException {
        String sql = "INSERT INTO books (title) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
        }
    }

    public static List<Books> getAllBooks() throws SQLException {
        String sql = "SELECT * FROM books";
        List<Books> books = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Books book = new Books();
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                books.add(book);
            }
        }
        return books;
    }
}