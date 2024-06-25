package sg.edu.np.mad.cookbuddy.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Recipe implements Serializable {
    private String id;
    private String name;
    private String cuisine;
    private String mainIngredient;
    private List<String> ingredients;
    private List<String> instructions;
    private List<String> allergies;
    private Map<String, String> nutrients;
    private int imageResId;
    private boolean isFavorite;

    public Recipe(String id, String name, String cuisine, String mainIngredient, List<String> ingredients, List<String> instructions,
        List<String> allergies, Map<String, String> nutrients, int imageResId, boolean isFavorite)
    {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.mainIngredient = mainIngredient;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.allergies = allergies;
        this.nutrients = nutrients;
        this.imageResId = imageResId;
        this.isFavorite = isFavorite;
    }

    @NonNull
    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", allergies=" + allergies +
                ", cuisine='" + cuisine + '\'' +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                ", mainIngredient='" + mainIngredient + '\'' +
                ", name='" + name + '\'' +
                ", nutritiousFacts=" + nutrients +
                '}';
    }

    // getters
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getCuisine(){
        return cuisine;
    }
    public String getMainIngredient() {
        return mainIngredient;
    }
    public List<String> getIngredients() {
        return ingredients;
    }
    public List<String> getInstructions(){
        return instructions;
    }
    public List<String> getAllergies() {
        return allergies;
    }
    public Map<String,String> getNutrients(){
        return nutrients;
    }
    public int getImageResId() {
        return imageResId;
    }
    public boolean isFavorite() {
        return isFavorite;
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }
    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
    public void setNutrients(Map<String, String> nutrients) {
        this.nutrients = nutrients;
    }
    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
