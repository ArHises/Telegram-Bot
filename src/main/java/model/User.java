package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private int id;
    private String username;
    private static final Map<Integer,User> users = new HashMap<>();

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    //getters
    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    //get user by id
    public static User getUserById(int id) {
        return users.get(id);
    }

    //add or return a user
    public static User getOrCreateUser(int id,String username) {
        if(users.containsKey(id)) {
            return users.get(id);
        }
        else {
            User user = new User(id,username);
            users.put(id,user);
            return user;
        }
    }

    //return all users as a list
    public static List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public String toString() {
        return "username " + this.username+ " id " + this.id;
    }
}
