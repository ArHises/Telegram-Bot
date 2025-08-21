package model;

import java.util.*;

public class User {
    private int id;
    private long chatId;
    private String username;

    public User(int id, long chatId, String username) {
        this.id = id;
        this.chatId = chatId;
        this.username = username;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public long getChatId() { return chatId; }
    public void setChatId(long chatId) { this.chatId = chatId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public static List<User> getAllUsers() {
        UsersCsv repo =  new UsersCsv();
        return repo.getAllUsers();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User u)) return false;
        return chatId == u.chatId;
        }
    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }

        @Override
    public String toString() {
        return "username " + this.username+ " id " + this.id + " chat id " + this.chatId;
    }
}
