package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;

public class TelegramNotesBot extends TelegramLongPollingBot {
    private static final String TOKEN = "8049532712:AAFvHLAOsIVMPVdZ7cPuElvXi57UeTnJV50";
    private static final String BOT_USERNAME = "MyNotes";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            if (messageText.equals("/start")) {
                sendMessage(chatId, "Привет! Я бот для заметок 🤖.\nСохраню:\nФильмы, которые планируешь посмотреть 🎬.\nКниги, которые планируешь прочитать 📚. \nМеста, которые планируешь посетить 🗺.\n\n" +
                        "Команды для добавления информации:\n/add_book\n/add_film\n/add_place\nОбщий вид команды для добавления информации:\nКоманда Пробел Запись\nПример:\n\"/add_book Самый богатый человек в Вавилоне\"\n\n" +
                        "Команды для получения сохраненной информации:\n/get_books\n/get_films\n/get_places\n\nИз запрашиваемой категории отобразится весь список");
            } else {
                try {
                    handleMessage(chatId, messageText);
                } catch (TelegramApiException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleMessage(String chatId, String messageText) throws SQLException, TelegramApiException {
        if (messageText.startsWith("/add_book")) {
            String title = messageText.split(" ", 2)[1];
            Books.addBook(title);
            sendMessage(chatId, "Книга сохранена ✅");
        } else if (messageText.startsWith("/add_film")) {
            String title = messageText.split(" ", 2)[1];
            Films.addFilm(title);
            sendMessage(chatId, "Фильм сохранен ✅");
        } else if (messageText.startsWith("/add_place")) {
            String name = messageText.split(" ", 2)[1];
            if (name.equals("глина")) {
                sendMessage(chatId, "ты че дурак? не запомню это ебанутое место");
            } else {
                Places.addPlace(name);
                sendMessage(chatId, "Место сохранено ✅");
            }
        } else if (messageText.startsWith("/get_books")) {
            List<Books> books = Books.getAllBooks();
            if (books.isEmpty()) {
                sendMessage(chatId, "Книги еще не были добавлены🤷‍♂️");
            } else {
                StringBuilder sb = new StringBuilder("Books:\n");
                for (Books book : books) {
                    sb.append(book.getId()).append(" --- ").append(book.getTitle()).append("\n");
                }
                sendMessage(chatId, sb.toString());
            }
        } else if (messageText.startsWith("/get_films")) {
            List<Films> films = Films.getAllFilms();
            if (films.isEmpty()) {
                sendMessage(chatId, "Фильмы еще не были добавлены🤷‍♂️");
            } else {
                StringBuilder sb = new StringBuilder("Films:\n");
                for (Films film : films) {
                    sb.append(film.getId()).append(" --- ").append(film.getTitle()).append("\n");
                }
                sendMessage(chatId, sb.toString());
            }
        } else if (messageText.startsWith("/get_places")) {
            List<Places> places = Places.getAllPlaces();
            if (places.isEmpty()) {
                sendMessage(chatId, "Места еще не были добавлены🤷‍♂️");
            } else {
                StringBuilder sb = new StringBuilder("Places:\n");
                for (Places place : places) {
                    sb.append(place.getId()).append(" --- ").append(place.getName()).append("\n");
                }
                sendMessage(chatId, sb.toString());
            }
        } else {
            sendMessage(chatId, "UNKNOWN MESSAGE🤷‍♂️");
        }
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}