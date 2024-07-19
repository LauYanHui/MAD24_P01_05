package sg.edu.np.mad.cookbuddy.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password;
    private List<String> allergies;
    private List<String> favourites;

    public User(String username, String password, List<String> allergies) {
        this.username = username;
        this.password = password;
        this.allergies = allergies;
        this.favourites = new ArrayList<String>();
    }

    public User(String username, String password, List<String> allergies, List<String> favourites) {
        this.username = username;
        this.password = password;
        this.allergies = allergies;
        this.favourites = favourites;
    }

    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public List<String> getAllergies() { return this.allergies; }
    public List<String> getFavourites() { return this.favourites; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setAllergies(List<String> allergies) { this.allergies = allergies; }
    public void setFavourites(List<String> favourites) { this.favourites = favourites; }
}
