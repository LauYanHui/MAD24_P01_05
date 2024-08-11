package sg.edu.np.mad.cookbuddy.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Recipe implements Serializable {
    public String id;

    private String imageName;;
    private List<String> allergies;
    private String cuisine;
    private List<String> ingredients;
    private List<String> instructions;
    private String mainIngredient;
    private String name;
    private Map<String, String> nutritiousFacts;
    private boolean isFavorite;
    public Recipe(String id,String imageName, List<String> allergies, String cuisine, List<String> ingredients, List<String> instructions,
                  String mainIngredient, String name, Map<String, String> nutritiousFacts, boolean isFavorite) {
        this.id = id;
        this.allergies = allergies;
        this.cuisine = cuisine;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.mainIngredient = mainIngredient;
        this.name = name;
        this.imageName = imageName;
        this.nutritiousFacts = nutritiousFacts;
        //this.imageResId = imageResId;
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

    /*
    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
    */


    public boolean isFavorite() {
        return isFavorite;
    }
    public String getImageName(){
        return imageName;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

}