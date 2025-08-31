package model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsersCsv {
    private final Path filePath = Paths.get("data/users.csv");

    public UsersCsv() {
        initFile();
    }

    private void initFile() {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                    writer.write("id,chatId,username\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize CSV file at: " + filePath, e);
        }
    }

    public void addUser(User user) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath, StandardOpenOption.APPEND)) {
            writer.write(user.getId() + "," + user.getChatId() + "," + safe(user.getUsername()) + "\n");
        } catch (IOException e) {
            throw new RuntimeException("Error adding user", e);
        }
    }

    public List<User> getAllUsers() {
        try {
            return Files.lines(filePath)
                    .skip(1)
                    .filter(line -> !line.isBlank())
                    .map(this::parseUser)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error reading users", e);
        }
    }

    private User parseUser(String line) {
        String[] parts = line.split(",", -1);
        int id = Integer.parseInt(parts[0].trim());
        long chatId = Long.parseLong(parts[1].trim());
        String username = parts.length > 2 ? parts[2] : "";
        return new User(id, chatId, username);
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replace("\n", " ").replace("\r", " ").replace(",", " ");
    }
}