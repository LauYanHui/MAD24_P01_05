package sg.edu.np.mad.cookbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView backIcon = findViewById(R.id.backIcon);

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recipeAdapter = new RecipeAdapter(recipeList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recipeAdapter);

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
                                Log.i("ezreal", "Recipe List Size: " + recipeList.size());
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing recipe data: " + e.getMessage());
                            }
                        }
                        recipeAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Data snapshot is null");
                    }
                } else {
                    Log.e(TAG, "Error getting data from Firebase: " + task.getException());
                }
            }
        });

        // Back icon click listener
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });
    }
}
