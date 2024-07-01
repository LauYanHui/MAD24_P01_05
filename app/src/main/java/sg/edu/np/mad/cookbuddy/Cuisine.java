package sg.edu.np.mad.cookbuddy;

import java.util.List;

public class Cuisine {
    private String name;
    private List<Recipe> recipeList;

    public Cuisine(String name, List<Recipe> recipeList) {
        this.name = name;
        this.recipeList = recipeList;
    }

    public String getName() {
        return name;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }
}