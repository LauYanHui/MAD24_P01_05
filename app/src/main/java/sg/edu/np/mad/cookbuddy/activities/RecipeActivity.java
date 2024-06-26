package sg.edu.np.mad.cookbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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


import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.models.Recipe;
import sg.edu.np.mad.cookbuddy.adapters.RecipeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {
    ArrayList<Recipe> recipeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView backIcon = findViewById(R.id.backIcon);

        // Code for Recycler View
        RecipeAdapter recipeAdapter = new RecipeAdapter(recipeList,this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        SlideInUpAnimator animator = new SlideInUpAnimator();
        animator.setAddDuration(500); // Optional customization
        animator.setRemoveDuration(500); // Optional customization

        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(recipeAdapter);

        // Firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mad-assignment-8c5d2-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // locating the recipes
        DatabaseReference myRef = database.getReference("Recipes");

        // reading data
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            @SuppressWarnings("unchecked")
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    DataSnapshot dataSnapshot = task.getResult();

                    // to get each of the recipe items
                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        if (!recipeSnapshot.exists()) {
                            continue;
                        }

                        Map<String, Object> recipeData = (Map<String, Object>) recipeSnapshot.getValue();

                        if (recipeData == null) {
                            continue;
                        }

                        String id = (String) recipeData.get("ID");
                        String cuisine = (String) recipeData.get("Cuisine");
                        String mainIngredient = (String) recipeData.get("Main ingredient");
                        String name = (String) recipeData.get("Name");
                        List<String> allergies = (List<String>) recipeData.get("Allergies");
                        List<String> instructions = (List<String>) recipeData.get("Instructions");
                        List<String> ingredients = (List<String>) recipeData.get("Ingredients");
                        Map<String, String> nutrients = (Map<String, String>) recipeData.get("Nutritious facts");

                        // find a way to store images on firebase
                        String imageName = (String) recipeData.get("Image");

                        int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());

                        // !- change false to isFavorite
                        Recipe recipe = new Recipe(id, name, cuisine, mainIngredient, ingredients, instructions, allergies, nutrients, imageResId, false);
                        recipeList.add(recipe);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Function for the back icon to go back to home page
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}