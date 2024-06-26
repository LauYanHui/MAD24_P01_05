package sg.edu.np.mad.cookbuddy.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password;
    private List<String> allergies;

    public User(String username, String password, List<String> allergies) {
        this.username = username;
        this.password = password;
        this.allergies = allergies;
    }

    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public List<String> getAllergies() { return this.allergies; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setAllergies(ArrayList<String> allergies) { this.allergies = allergies; }
}
