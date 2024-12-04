package main.java.org.qcm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users = new HashMap<>();
    private static final String JSON_FILE_PATH = "src/main/java/org/qcm/users.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    // Constructor: Load existing users from the JSON file
    public UserManager() {
        loadUsersFromFile();
    }

    // Register a new user
    public String registerUser(String username, String email) {
        User user = new User(username, email);
        users.put(user.getId(), user);
        saveUsersToFile();
        return user.getId();
    }

    // Delete a user by ID
    public void deleteUser(String id) {
        users.remove(id);
        saveUsersToFile();
    }

    // Get a user by ID
    public User getUser(String id) {
        return users.get(id);
    }

    // Load users from the JSON file
    private void loadUsersFromFile() {
        File file = new File(JSON_FILE_PATH);
        if (file.exists()) {
            try {
                users = mapper.readValue(file, new TypeReference<Map<String, User>>() {});
            } catch (IOException e) {
                System.err.println("Failed to load users from file: " + e.getMessage());
            }
        }
    }

    // Save users to the JSON file
    private void saveUsersToFile() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE_PATH), users);
        } catch (IOException e) {
            System.err.println("Failed to save users to file: " + e.getMessage());
        }
    }

    public void addQCMToUser(String userId, String qcmId, String nomQCM, int score) {
        User user = users.get(userId);
        if (user != null) {
            user.addCompletedQCM(qcmId, nomQCM, score);
        } else {
            System.err.println("User with ID " + userId + " not found.");
        }
    }
}
