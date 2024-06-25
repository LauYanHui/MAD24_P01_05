package sg.edu.np.mad.cookbuddy.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;
import java.util.Map;

import sg.edu.np.mad.cookbuddy.adapters.InstructionAdapter;
import sg.edu.np.mad.cookbuddy.R;
import sg.edu.np.mad.cookbuddy.models.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TextView tvName;
    private TextView tvCuisine;
    private TextView tvMainIngredient;
    private TextView tvIngredients;
    private TextView tvNutrients;
    private ImageView ivRecipeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // back to recipe button
        ivBack = findViewById(R.id.backtohomeBtn);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityName = new Intent(RecipeDetailsActivity.this, RecipeActivity.class);
                startActivity(activityName);
            }
        });

        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("Recipe");

        // get views
        tvName = findViewById(R.id.recipeName);
        tvCuisine = findViewById(R.id.cuisine);
        tvMainIngredient = findViewById(R.id.mainIngredient);
        tvIngredients = findViewById(R.id.ingredient);
        ivRecipeImage = findViewById(R.id.recipeImage);
        tvNutrients = findViewById(R.id.nutritiousFact);

        Map<String, String> nutritiousFacts = recipe.getNutrients();
        StringBuilder nutritionInfo = new StringBuilder();
        for (Map.Entry<String, String> entry : nutritiousFacts.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            nutritionInfo.append(key).append(": ").append(value).append("\n");
        }

        String nutritionText = nutritionInfo.toString();

        List<String> ingredientList = recipe.getIngredients();
        List<String> instructionList = recipe.getInstructions();
        StringBuilder ingredientBuilder = new StringBuilder();
        for (String ingredient : ingredientList) {
            ingredientBuilder.append("• ").append(ingredient).append("\n");
        }
        String ingredientText = ingredientBuilder.toString();

        // set values
        tvName.setText(recipe.getName());
        tvCuisine.setText(recipe.getCuisine());
        tvMainIngredient.setText(recipe.getMainIngredient());
        tvIngredients.setText(ingredientText);
        tvNutrients.setText(nutritionText);
        ivRecipeImage.setImageResource(recipe.getImageResId());

        // slide card view
        ViewPager2 instructionsPager = findViewById(R.id.instructionsPager);
        InstructionAdapter adapter = new InstructionAdapter(instructionList);
        instructionsPager.setAdapter(adapter);
    }
}