package sg.edu.np.mad.cookbuddy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";
    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private ArrayList<Recipe> filteredRecipeList = new ArrayList<>();
    private ArrayList<String> cuisineList = new ArrayList<>();
    private RecipeAdapter recipeAdapter;
    private CuisineAdapter cuisineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView cuisineRecyclerView = findViewById(R.id.cuisineRecyclerView);
        RecyclerView recipeRecyclerView = findViewById(R.id.recyclerView);

        recipeAdapter = new RecipeAdapter(filteredRecipeList, this);
        cuisineAdapter = new CuisineAdapter(cuisineList, this, new CuisineAdapter.OnCuisineClickListener() {
            @Override
            public void onCuisineClick(String cuisine) {
                filterByCuisine(cuisine);
            }
        });

        cuisineRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cuisineRecyclerView.setAdapter(cuisineAdapter);

        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeRecyclerView.setAdapter(recipeAdapter);

        // Firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Recipes");

        // Retrieve data from Firebase
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot != null) {
                        // Clear previous data
                        recipeList.clear();
                        cuisineList.clear();

                        // Add "All Recipes" tag
                        cuisineList.add("All Recipes");

                        for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                            try {
                                // Parse recipe data
                                Map<String, Object> recipeData = (Map<String, Object>) recipeSnapshot.getValue();
                                String id = (String) recipeData.get("ID");
                                String cuisine = (String) recipeData.get("Cuisine");
                                String mainIngredient = (String) recipeData.get("Main ingredient");
                                String name = (String) recipeData.get("Name");
                                List<String> allergies = (List<String>) recipeData.get("Allergies");
                                List<String> instructions = (List<String>) recipeData.get("Instructions");
                                List<String> ingredients = (List<String>) recipeData.get("Ingredients");

                                // Handling nutrition facts
                                Map<String, String> nutritionFacts = new HashMap<>();
                                Map<String, Object> nutritionFactsData = (Map<String, Object>) recipeData.get("Nutritious facts");
                                if (nutritionFactsData != null) {
                                    for (Map.Entry<String, Object> entry : nutritionFactsData.entrySet()) {
                                        nutritionFacts.put(entry.getKey(), String.valueOf(entry.getValue()));
                                    }
                                }

                                String imageName = (String) recipeData.get("Image");
                                int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());

                                boolean favourite = false;
                                Recipe recipe = new Recipe(id, imageResId, allergies, cuisine, ingredients, instructions, mainIngredient, name, nutritionFacts, favourite);
                                recipeList.add(recipe);

                                // Add cuisine to the cuisine list if not already present
                                if (!cuisineList.contains(cuisine)) {
                                    cuisineList.add(cuisine);
                                }

                                Log.i(TAG, "Recipe List Size: " + recipeList.size());
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing recipe data: " + e.getMessage());
                            }
                        }

                        // Notify adapters of data changes
                        cuisineAdapter.notifyDataSetChanged();
                        filterByCuisine("All Recipes"); // Default to "All Recipes" tag
                    } else {
                        Log.e(TAG, "Data snapshot is null");
                    }
                } else {
                    Log.e(TAG, "Error getting data from Firebase: " + task.getException());
                }
            }
        });

        // Search functionality
        EditText searchInput = findViewById(R.id.searchInput);
        ImageView searchIcon = findViewById(R.id.searchIcon);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchInput.getText().toString();
                Log.d(TAG, "Search Icon clicked, search text: " + searchText);
                filter(searchText);
                searchInput.setText("");
            }
        });

        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchText = searchInput.getText().toString();
                Log.d(TAG, "IME_ACTION_SEARCH triggered, search text: " + searchText);
                filter(searchText);
                return true;
            }
            return false;
        });
    }

    private void filterByCuisine(String cuisine) {
        filteredRecipeList.clear();
        if (cuisine.equals("All Recipes")) {
            filteredRecipeList.addAll(recipeList);
        } else {
            for (Recipe recipe : recipeList) {
                if (recipe.getCuisine().equalsIgnoreCase(cuisine)) {
                    filteredRecipeList.add(recipe);
                }
            }
        }
        recipeAdapter.updateRecipeList(filteredRecipeList);
        Log.d(TAG, "Filtered by cuisine: " + cuisine + ", filtered list size: " + filteredRecipeList.size());
    }

    private void filter(String text) {
        filteredRecipeList.clear();
        for (Recipe recipe : recipeList) {
            if (recipe.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredRecipeList.add(recipe);
            }
        }
        recipeAdapter.updateRecipeList(filteredRecipeList);
        Log.d(TAG, "Filter applied, filtered list size: " + filteredRecipeList.size());
    }
}
