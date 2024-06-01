package sg.edu.np.mad.cookbuddy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Recipe implements Serializable {
    public String id;
    private int imageResId;
    public List<String> allergies;
    public String cuisine;
    public List<String> ingredients;
    public List<String> instructions;
    public String mainIngredient;
    public String name;
    public Map<String, String> nutritiousFacts;
    public boolean isFavorite;
    public Recipe(String id,int imageResId, List<String> allergies, String cuisine, List<String> ingredients, List<String> instructions,
                  String mainIngredient, String name, Map<String, String> nutritiousFacts, boolean isFavorite) {
        this.id = id;
        this.allergies = allergies;
        this.cuisine = cuisine;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.mainIngredient = mainIngredient;
        this.name = name;
        this.nutritiousFacts = nutritiousFacts;
        this.imageResId = imageResId;
        this.isFavorite = isFavorite;

    }
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
                ", nutritiousFacts=" + nutritiousFacts +
                '}';
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public List<String> getInstructions(){
        return instructions;
    }
    public String getCuisine(){
        return cuisine;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public Map<String, String> getNutritiousFacts() {
        return nutritiousFacts;
    }

    public String getMainIngredient() {
        return mainIngredient;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
    public Map<String,String> getNutritionFacts(){
        return nutritiousFacts;
    }
    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

}
