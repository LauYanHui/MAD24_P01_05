package sg.edu.np.mad.cookbuddy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password;
    private ArrayList<String> allergies;
    private ArrayList<String> bookmarks;
    private ArrayList<String> favorites;
    public User(String username, String password, ArrayList<String> allergies) {
        this.username = username;
        this.password = password;
        this.allergies = new ArrayList<String>();
        this.bookmarks = new ArrayList<>();
        this.favorites = new ArrayList<>();
    }
    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public List<String> getAllergies() { return this.allergies; }
    public List<String> getBookmarks() { return bookmarks; }
    public List<String> getFavorites() { return favorites; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setAllergies(ArrayList<String> allergies) { this.allergies = allergies; }
    public void setBookmarks(ArrayList<String> bookmarks) { this.bookmarks = bookmarks; }
    public void setFavorites(ArrayList<String> favorites) { this.favorites = favorites; }
}
