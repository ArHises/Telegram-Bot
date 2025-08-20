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
            // Ensure parent directory exists
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            // Create file with header if it doesn't exist
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

    // CREATE
    public void addUser(User user) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath, StandardOpenOption.APPEND)) {
            writer.write(user.getId() + "," + user.getChatId() + "," + safe(user.getUsername()) + "\n");
        } catch (IOException e) {
            throw new RuntimeException("Error adding user", e);
        }
    }

    // READ
    public List<User> getAllUsers() {
        try {
            return Files.lines(filePath)
                    .skip(1) // skip header
                    .filter(line -> !line.isBlank())
                    .map(this::parseUser)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error reading users", e);
        }
    }

    // UPDATE (by chatId)
    public void updateUser(long chatId, String newUsername) {
        List<User> users = new ArrayList<>(getAllUsers());
        for (User u : users) {
            if (u.getChatId() == chatId) {
                u.setUsername(newUsername);
            }
        }
        saveAll(users);
    }

    // DELETE (by chatId)
    public void deleteUser(long chatId) {
        List<User> users = getAllUsers()
                .stream()
                .filter(u -> u.getChatId() != chatId)
                .collect(Collectors.toList());
        saveAll(users);
    }

    // Save entire list back to CSV (overwrites file)
    private void saveAll(List<User> users) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("id,chatId,username\n");
            for (User u : users) {
                writer.write(u.getId() + "," + u.getChatId() + "," + safe(u.getUsername()) + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving users", e);
        }
    }

    // Parse a CSV line into a User
    private User parseUser(String line) {
        String[] parts = line.split(",", -1); // keep empty fields
        int id = Integer.parseInt(parts[0].trim());
        long chatId = Long.parseLong(parts[1].trim());
        String username = parts.length > 2 ? parts[2] : "";
        return new User(id, chatId, username);
    }

    // Simple sanitizer: avoid breaking CSV if username contains newlines or commas
    private String safe(String s) {
        if (s == null) return "";
        return s.replace("\n", " ").replace("\r", " ").replace(",", " ");
    }
}